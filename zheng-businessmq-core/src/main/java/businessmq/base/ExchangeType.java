package businessmq.base;

/**
 * Exchange
 * Created by alan.zheng on 2017/2/8.
 */
public enum ExchangeType {
    /**
     * 路由精确查找
     */
    DIRECT("direct"),
    /**
     * 不处理路由
     */
    FANOUT("fanout"),
    /**
     * 路由模糊查找
     */
    TOPIC("topic")
    ;
    private String type;

    private ExchangeType(String _type){
        type=_type;
    }

    public String getType() {
        return type;
    }
}
