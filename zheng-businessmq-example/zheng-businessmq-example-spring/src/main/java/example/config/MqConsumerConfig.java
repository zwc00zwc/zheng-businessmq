package example.config;

import businessmq.ConsumerListener;
import businessmq.SpringConsumerListener;
import businessmq.config.ConsumerConfig;
import businessmq.db.DbConfig;
import businessmq.reg.zookeeper.ZookeeperRegistryCenter;
import example.business.Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;

/**
 * Created by alan.zheng on 2017/2/27.
 */
@Configuration
public class MqConsumerConfig {
    @Resource
    private ZookeeperRegistryCenter zookeeperRegistryCenter;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private DbConfig dbConfig;

    @Bean(name = "consumer")
    public Consumer consumer() {
        return new Consumer();
    }

    @Bean(name = "consumerConfig")
    public ConsumerConfig consumerConfig(final Consumer consumer){
        ConsumerConfig consumerConfig=new ConsumerConfig();
        consumerConfig.setHost("192.168.0.51");
        consumerConfig.setPort(5672);
        consumerConfig.setUserName("root");
        consumerConfig.setPassword("root");
        consumerConfig.setConsumerQueue("command");
        consumerConfig.setJavaClass(consumer.getClass().getCanonicalName());
        return consumerConfig;
    }

    @Bean(initMethod = "init",name = "TestListen")
    public ConsumerListener consumerListener(final Consumer consumer,final ConsumerConfig consumerConfig) {
        return new SpringConsumerListener(consumerConfig,dbConfig,zookeeperRegistryCenter,consumer,threadPoolTaskExecutor);
    }
}
