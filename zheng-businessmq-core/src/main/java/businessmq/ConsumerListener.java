package businessmq;

import businessmq.base.Context;
import businessmq.config.ConsumerConfig;
import businessmq.consumer.AbstractConsumer;
import businessmq.consumer.ConsumerProvider;
import businessmq.db.DbConfig;
import businessmq.db.dal.BusinessMqNodeDal;
import businessmq.db.model.businessmq.BusinessMqNode;
import businessmq.reg.zookeeper.ZookeeperRegistryCenter;
import com.google.common.base.Optional;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alan.zheng on 2017/2/10.
 */
public class ConsumerListener {
    private final ConsumerConfig consumerConfig;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final ZookeeperRegistryCenter zookeeperRegistryCenter;
    public ConsumerListener(final ConsumerConfig _config,DbConfig dbConfig,final ThreadPoolTaskExecutor threadPoolTaskExecutor,final ZookeeperRegistryCenter zookeeperRegistryCenter){
        BusinessMqNodeDal businessMqNodeDal=new BusinessMqNodeDal();
        List<BusinessMqNode> nodeList= businessMqNodeDal.queryNodeList(dbConfig);
        Map<Integer,DbConfig> map=new HashMap<>();
        for (BusinessMqNode node:nodeList) {
            DbConfig nodeConfig=new DbConfig();
            nodeConfig.setDriver(node.getDriver());
            nodeConfig.setUrl(node.getUrl());
            nodeConfig.setUsername(node.getUsername());
            nodeConfig.setPassword(node.getPassword());
            map.put(node.getNode(),nodeConfig);
        }
        _config.setBlanceNode(map);
        this.consumerConfig=_config;
        this.threadPoolTaskExecutor=threadPoolTaskExecutor;
        this.zookeeperRegistryCenter=zookeeperRegistryCenter;
    }

    /**
     * 初始化消息监听
     */
    public void init(){
        Context context=new Context();
        ConsumerProvider consumerProvider=new ConsumerProvider(zookeeperRegistryCenter,context);
        Optional<AbstractConsumer> abstractConsumerOptional= createBaseJobInstance();
        if (abstractConsumerOptional.isPresent()){
            execute(consumerProvider,consumerConfig,abstractConsumerOptional.get());
        }else {
            try {
                AbstractConsumer abstractConsumer=(AbstractConsumer) Class.forName(consumerConfig.getJavaClass()).newInstance();
                execute(consumerProvider,consumerConfig,abstractConsumer);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    protected Optional<AbstractConsumer> createBaseJobInstance() {
        return Optional.absent();
    }

    private void execute(final ConsumerProvider consumerProvider, final ConsumerConfig config, final AbstractConsumer abstractConsumer){
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                consumerProvider.receiveMessage(config,abstractConsumer);
            }
        });
    }
}
