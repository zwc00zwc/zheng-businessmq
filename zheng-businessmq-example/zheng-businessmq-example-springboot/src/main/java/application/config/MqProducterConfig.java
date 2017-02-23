package application.config;

import businessmq.SpringProductProvide;
import businessmq.config.ProducterConfig;
import businessmq.db.DbConfig;
import businessmq.producter.ProducterProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alan.zheng on 2017/2/14.
 */
@Configuration
public class MqProducterConfig {
    @Bean
    public ProducterProvider producterProvider(){
        return new ProducterProvider();
    }

    @Bean
    public ProducterConfig config(){
        ProducterConfig producterConfig=new ProducterConfig();
        producterConfig.setNode(1);
        producterConfig.setHost("127.0.0.1");
        producterConfig.setPort(5672);
        producterConfig.setUserName("guest");
        producterConfig.setPassword("guest");
        Map queuemap=new HashMap();
        queuemap.put("command",null);
        producterConfig.setQueueRoutingKey(queuemap);
        return producterConfig;
    }
    @Bean
    public SpringProductProvide provide(ProducterConfig producterConfig, ProducterProvider producterProvider) {
        DbConfig dbConfig=new DbConfig();
        dbConfig.setDriver("com.mysql.jdbc.Driver");
        dbConfig.setUrl("jdbc:mysql://localhost:3306/com.zwc?useUnicode=true&amp;characterEncoding=UTF-8");
        dbConfig.setUsername("root");
        dbConfig.setPassword("root");
        return new SpringProductProvide(producterConfig,dbConfig,producterProvider);
    }
}
