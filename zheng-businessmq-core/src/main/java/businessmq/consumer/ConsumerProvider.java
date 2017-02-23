package businessmq.consumer;

import businessmq.base.Context;
import businessmq.base.Message;
import businessmq.base.MqRegistryManeger;
import businessmq.config.ConsumerConfig;
import businessmq.config.MessageType;
import businessmq.db.DbConfig;
import businessmq.db.dal.BusinessMqDal;
import businessmq.log.MqLogManager;
import businessmq.reg.listener.ConnectListener;
import businessmq.reg.zookeeper.ZookeeperRegistryCenter;
import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

/**
 * 消费者提供类
 * Created by alan.zheng on 2017/2/8.
 */
public class ConsumerProvider {
    private Context context;
    private final ZookeeperRegistryCenter zookeeperRegistryCenter;

    public ConsumerProvider(ZookeeperRegistryCenter _zookeeperRegistryCenter,Context _context){
        zookeeperRegistryCenter=_zookeeperRegistryCenter;
        context=_context;
    }

    public void receiveMessage(ConsumerConfig consumerConfig,AbstractConsumer abstractConsumer){
        try {
            Channel channel= MqRegistryManeger.getMqChannel(consumerConfig);
            if (!StringUtils.isEmpty(consumerConfig.getExchangeName())&&consumerConfig.getExchangeType()!=null){
                channel.exchangeDeclare(consumerConfig.getExchangeName(),consumerConfig.getExchangeType().getType());
                if (!StringUtils.isEmpty(consumerConfig.getConsumerQueue())){
                    channel.queueDeclare(consumerConfig.getConsumerQueue(), true, false, false, null);
                }
                if (consumerConfig.getRoutingKey()!=null&&consumerConfig.getRoutingKey().length>0){
                    for (int i=0;i<consumerConfig.getRoutingKey().length;i++){
                        channel.queueBind(consumerConfig.getConsumerQueue(),consumerConfig.getExchangeName(),consumerConfig.getRoutingKey()[i]);
                    }
                }
            }else {
                if (!StringUtils.isEmpty(consumerConfig.getConsumerQueue())){
                    channel.queueDeclare(consumerConfig.getConsumerQueue(), true, false, false, null);
                }
            }
            QueueingConsumer consumer = new QueueingConsumer(channel);
            // 指定消费队列
            if (StringUtils.isEmpty(consumerConfig.getConsumerQueue())){
                channel.basicConsume(channel.queueDeclare().getQueue(), true, consumer);
            }else {
                channel.basicConsume(consumerConfig.getConsumerQueue(), true, consumer);
            }

            try {
                if (!zookeeperRegistryCenter.isExisted("/mq/"+consumerConfig.getJavaClass()+"")){
                    CuratorFramework curatorFramework=(CuratorFramework) zookeeperRegistryCenter.getRawClient();

                    curatorFramework.getConnectionStateListenable().addListener(new ConnectListener());
                    String remark="监听"+"【"+consumerConfig.getExchangeName()+"】【"+consumerConfig.getRoutingKey()+"】【"+consumerConfig.getConsumerQueue();
                    zookeeperRegistryCenter.createEphemeral("/mq/"+consumerConfig.getJavaClass()+"",remark);
                }
            } catch (Exception e) {
                MqLogManager.log(consumerConfig.getExchangeName()+consumerConfig.getRoutingKey()+consumerConfig.getConsumerQueue()+consumerConfig.getJavaClass(),
                        e.toString(),new Date());
            }
            //注册心跳守护
            Timer timer=new Timer();
            timer.schedule(new HeartTask(consumerConfig.getJavaClass()),0,5000);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            BusinessMqDal businessMqDal=new BusinessMqDal();
            while (context.isListener()) {
                try {
                    QueueingConsumer.Delivery delivery = consumer.nextDelivery();
                    String message = new String(delivery.getBody());
                    Message msg= JSON.parseObject(message, Message.class);
                    if (MessageType.MYSQL.getType().equals(msg.getMsgType())){
                        Map<Integer,DbConfig> map= consumerConfig.getBlanceNode();
                        if (map.containsKey(msg.getDbId())){
                            try {
                                businessMqDal.updateMqStatus(map.get(msg.getDbId()),msg.getId(),1);
                            } catch (Exception e) {
                                MqLogManager.log(consumerConfig.getExchangeName()+consumerConfig.getRoutingKey()+consumerConfig.getConsumerQueue()+consumerConfig.getJavaClass(),
                                        msg.getDbId()+"节点"+msg.getId()+"消息异常:"+e.toString(),new Date());
                                map.remove(msg.getDbId());
                                consumerConfig.setBlanceNode(map);
                            }
                        }else {
                            MqLogManager.log(consumerConfig.getExchangeName()+consumerConfig.getRoutingKey()+consumerConfig.getConsumerQueue()+consumerConfig.getJavaClass(),
                                    msg.getDbId()+"节点【"+msg.getId()+"】找不到分区节点",new Date());
                        }
                    }
                    abstractConsumer.work(message);
                } catch (InterruptedException e) {
                    MqLogManager.log(consumerConfig.getExchangeName()+consumerConfig.getRoutingKey()+consumerConfig.getConsumerQueue()+consumerConfig.getJavaClass(),
                            "消息消费异常+"+e.toString(),new Date());
                }
            }
        } catch (IOException e) {
            MqLogManager.log(consumerConfig.getExchangeName()+consumerConfig.getRoutingKey()+consumerConfig.getConsumerQueue()+consumerConfig.getJavaClass(),
                    e.toString(),new Date());
        } catch (TimeoutException e) {
            MqLogManager.log(consumerConfig.getExchangeName()+consumerConfig.getRoutingKey()+consumerConfig.getConsumerQueue()+consumerConfig.getJavaClass(),
                    e.toString(),new Date());
        }
    }

    class HeartTask extends TimerTask{
        private final String listenName;
        public HeartTask(String _listenName){
            listenName=_listenName;
        }
        @Override
        public void run() {
            try {
                if (zookeeperRegistryCenter.isExisted("/mq/"+listenName+"")){
                    context.setListener(true);
                }else{
                    context.setListener(false);
                }
            } catch (Exception e) {
                context.setListener(false);
            }
        }
    }
}
