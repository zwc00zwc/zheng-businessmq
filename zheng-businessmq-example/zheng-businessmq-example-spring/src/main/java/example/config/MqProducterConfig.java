package example.config;

import businessmq.SpringProductProvide;
import businessmq.config.ProducterConfig;
import businessmq.db.DbConfig;
import businessmq.producter.ProducterProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Created by alan.zheng on 2017/2/27.
 */
@Configuration
public class MqProducterConfig {
    @Resource
    private ProducterConfig producterConfig;

    @Resource
    private DbConfig dbConfig;

    @Bean
    public ProducterProvider producterProvider(){
        return new ProducterProvider();
    }

    @Bean
    public SpringProductProvide provide(ProducterProvider producterProvider) {
        return new SpringProductProvide(producterConfig,dbConfig,producterProvider);
    }
}
