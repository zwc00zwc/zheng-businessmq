package common.utility;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by alan.zheng on 2017/2/8.
 */
public class PropertiesUtility {

    private static Properties props = new Properties();
    public PropertiesUtility(String file){
        try {
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String property){
        return props.getProperty(property);
    }
}
