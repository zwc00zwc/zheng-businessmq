package businessmq.config;

import businessmq.base.ExchangeType;
import businessmq.db.DbConfig;

import java.util.Map;

/**
 * Created by alan.zheng on 2017/2/10.
 */
public class ConsumerConfig extends MqConfig {
    /**
     * 处理业务类名
     */
    private String javaClass;
    /**
     * 消费队列
     */
    private String consumerQueue;
    /**
     * 交换机名
     */
    private String exchangeName;
    /**
     * 交换机类型
     */
    private ExchangeType exchangeType;
    /**
     * 路由数组
     */
    private String[] routingKey;

    /**
     * 负载均衡节点map
     */
    private Map<Integer,DbConfig> blanceNode;

    public String getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(String javaClass) {
        this.javaClass = javaClass;
    }

    public String getConsumerQueue() {
        return consumerQueue;
    }

    public void setConsumerQueue(String consumerQueue) {
        this.consumerQueue = consumerQueue;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public ExchangeType getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(ExchangeType exchangeType) {
        this.exchangeType = exchangeType;
    }

    public String[] getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String[] routingKey) {
        this.routingKey = routingKey;
    }

    public Map<Integer, DbConfig> getBlanceNode() {
        return blanceNode;
    }

    public void setBlanceNode(Map<Integer, DbConfig> blanceNode) {
        this.blanceNode = blanceNode;
    }
}
