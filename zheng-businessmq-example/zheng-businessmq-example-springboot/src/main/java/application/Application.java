package application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;

/**
 * Created by Administrator on 2016/12/12.
 */
@SpringBootApplication
public class Application implements EmbeddedServletContainerCustomizer {
    public static void main(String[] args){
        SpringApplication springApplication= new SpringApplication(Application.class);
        springApplication.run(args);
    }

    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        configurableEmbeddedServletContainer.setPort(8086);
    }
}
