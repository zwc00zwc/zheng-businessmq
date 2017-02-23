package businessmq.base;

import businessmq.config.MqConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * mq链接注册中心
 * Created by alan.zheng on 2017/2/8.
 */
public class MqRegistryManeger {
    private static MqRegistryManeger mqRegistryManeger;

    private static Map<String,Connection> connectionMap=new HashMap<>();

    private static Map<String,Channel> channelMap=new HashMap<>();

    public static MqRegistryManeger instance(){
        if (mqRegistryManeger==null){
            mqRegistryManeger=new MqRegistryManeger();
        }
        return mqRegistryManeger;
    }

    public static Channel getMqChannel(MqConfig config) throws IOException, TimeoutException {
        String key=config.getHost()+":"+config.getPort()+"/"+config.getUserName();
        Channel channel= channelMap.get(key);
        if (channel==null){
            Connection connection= connectionMap.get(key);
            if (connection==null){
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost(config.getHost());
                factory.setPort(config.getPort());
                factory.setUsername(config.getUserName());
                factory.setPassword(config.getPassword());
                connection = factory.newConnection();
                connectionMap.put(key,connection);
            }
            channel=connection.createChannel();
            channelMap.put(key,channel);
        }
        return channel;
    }
}
