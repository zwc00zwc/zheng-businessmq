package application.config;

import businessmq.ConsumerListener;
import businessmq.SpringConsumerListener;
import businessmq.config.ConsumerConfig;
import businessmq.db.DbConfig;
import businessmq.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;

/**
 * Created by alan.zheng on 2017/2/10.
 */
@Configuration
public class TestMqConfig {
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor threadPoolExecutor=new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(5);
        threadPoolExecutor.setMaxPoolSize(10);
        threadPoolExecutor.setQueueCapacity(25);
        return new ThreadPoolTaskExecutor();
    }

    @Resource
    private ZookeeperRegistryCenter zookeeperRegistryCenter;

    @Bean(name = "testConsumer")
    public TestConsumer zhengJob() {
        return new TestConsumer();
    }

    @Bean(initMethod = "init",name = "TestListen")
    public ConsumerListener consumerListener(final TestConsumer testConsumer, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        ConsumerConfig consumerConfig=new ConsumerConfig();
        consumerConfig.setHost("192.168.0.51");
        consumerConfig.setPort(5672);
        consumerConfig.setUserName("root");
        consumerConfig.setPassword("root");
        consumerConfig.setConsumerQueue("command");
        consumerConfig.setJavaClass(testConsumer.getClass().getCanonicalName());
        DbConfig dbConfig=new DbConfig();
        dbConfig.setDriver("com.mysql.jdbc.Driver");
        dbConfig.setUrl("jdbc:mysql://localhost:3306/com.zwc?useUnicode=true&amp;characterEncoding=UTF-8");
        dbConfig.setUsername("root");
        dbConfig.setPassword("root");
        return new SpringConsumerListener(consumerConfig,dbConfig,zookeeperRegistryCenter,testConsumer,threadPoolTaskExecutor);
    }
}
