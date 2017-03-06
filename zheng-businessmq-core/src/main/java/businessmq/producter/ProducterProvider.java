package businessmq.producter;

import businessmq.base.ExchangeType;
import businessmq.base.Message;
import businessmq.base.MqRegistryManeger;
import businessmq.config.MessageType;
import businessmq.config.ProducterConfig;
import businessmq.db.DbConfig;
import businessmq.db.dal.BusinessMqDal;
import businessmq.log.MqLogManager;
import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生成者提供类
 * Created by alan.zheng on 2017/2/8.
 */
public class ProducterProvider {
    private Lock lock = new ReentrantLock();// 锁对象
    /**
     * 发送消息
     * @param producterConfig
     * @param msg
     */
    public void sendMessage(ProducterConfig producterConfig, String msg){
        Message message=new Message();
        message.setDbId(producterConfig.getNode());
        message.setMsg(msg);
        message.setMsgType(MessageType.MYSQL.getType());
        try {
            lock.lock();
            DbConfig dbConfig = getLoadBalanceNodeInfo(producterConfig);
            Long id= null;
            int errorcount=0;
            boolean sendresult=false;
            while (errorcount < 3 && !sendresult){
                try {
                    id= persistenceMessage(dbConfig,msg);
                    sendresult=true;
                } catch (Exception e) {
                    Integer removeNode=failover(producterConfig);
                    MqLogManager.log("【"+removeNode+"】节点异常", e.toString(),new Date());
                    errorcount++;
                }
            }
            message.setId(id);
        } catch (Exception e) {
            MqLogManager.log(producterConfig.getExchangeName()+"发送消息异常",
                    e.toString(),new Date());
        }finally {
            lock.unlock();
        }
        String sendmsg= JSON.toJSONString(message);
        trySend(producterConfig,sendmsg);
    }

    /**
     * 顺序轮询节点获取节点及分区,从而达到负载均衡的目的
     * @param producterConfig
     * @return
     */
    private DbConfig getLoadBalanceNodeInfo(ProducterConfig producterConfig){
        Map<Integer,DbConfig> dbConfigMap= producterConfig.getBlanceNode();
        if (!dbConfigMap.isEmpty()){
            Integer node= producterConfig.getNode();
            while (!dbConfigMap.containsKey(producterConfig.getNode())){
                if (node+1>dbConfigMap.size()){
                    node=1;
                }else {
                    node++;
                }
            }
            if (node+1>dbConfigMap.size()){
                producterConfig.setNode(1);
            }else {
                producterConfig.setNode(node+1);
            }
            return dbConfigMap.get(node);
        }
        return null;
    }

    /**
     * 故障转移，移除异常分区节点
     * @param producterConfig
     */
    private Integer failover(ProducterConfig producterConfig){
        Integer removeNode=null;
        if (producterConfig.getNode()-1<1){
            removeNode=producterConfig.getBlanceNode().size();
        }else {
            removeNode=producterConfig.getNode()-1;
        }
        if (producterConfig.getBlanceNode().containsKey(removeNode)){
            producterConfig.getBlanceNode().remove(removeNode);
        }
        return removeNode;
    }

    /**
     * 消息持久化
     * @param dbConfig
     * @param msg
     * @return
     */
    private Long persistenceMessage(DbConfig dbConfig,String msg){
        BusinessMqDal dal=new BusinessMqDal();
        Long id= null;
        id = dal.insertMq(dbConfig,msg);
        if (id!=null){
            return id;
        }
        return null;
    }

    /**
     * 发送消息
     * @param producterConfig
     * @param sendmsg
     */
    private void trySend(ProducterConfig producterConfig,String sendmsg){
        try {
            Channel channel= MqRegistryManeger.getMqChannel(producterConfig);
            Map<String,Set<String>> map= producterConfig.getQueueRoutingKey();
            if (!StringUtils.isEmpty(producterConfig.getExchangeName())&&producterConfig.getExchangeType()!=null){
                channel.exchangeDeclare(producterConfig.getExchangeName(),producterConfig.getExchangeType().getType());
                if (!map.isEmpty()){
                    for (Map.Entry<String, Set<String>> entry: map.entrySet()){
                        channel.queueDeclare(entry.getKey(), true, false, false, null);
                        Set<String> set= entry.getValue();
                        if (set!=null&&set.size()>0){
                            for (String routing:set){
                                channel.queueBind(entry.getKey(),producterConfig.getExchangeName(),routing);
                            }
                        }
                    }
                }
                if (ExchangeType.FANOUT.equals(producterConfig.getExchangeType())){
                    channel.basicPublish(producterConfig.getExchangeName(),"", MessageProperties.PERSISTENT_TEXT_PLAIN,sendmsg.getBytes());
                }else {
                    for (Map.Entry<String, Set<String>> entry: map.entrySet()){
                        Set<String> set= entry.getValue();
                        if (set!=null&&set.size()>0){
                            for (String routing:set){
                                channel.basicPublish(producterConfig.getExchangeName(),routing, MessageProperties.PERSISTENT_TEXT_PLAIN,sendmsg.getBytes());
                            }
                        }
                    }
                }
            }else {
                if (!map.isEmpty()){
                    for (Map.Entry<String, Set<String>> entry: map.entrySet()){
                        channel.queueDeclare(entry.getKey(), true, false, false, null);
                        channel.basicPublish("",entry.getKey(), MessageProperties.PERSISTENT_TEXT_PLAIN,sendmsg.getBytes());
                    }
                }
            }

        } catch (IOException e) {
            MqLogManager.log(producterConfig.getExchangeName()+"发送消息异常",
                    e.toString(),new Date());
        } catch (TimeoutException e) {
            MqLogManager.log(producterConfig.getExchangeName()+"发送消息异常",
                    e.toString(),new Date());
        }
    }
}
