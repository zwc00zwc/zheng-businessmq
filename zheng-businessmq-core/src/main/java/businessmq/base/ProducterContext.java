package businessmq.base;

import businessmq.db.DbConfig;

import java.util.Map;

/**
 * Created by alan.zheng on 2017/2/14.
 */
public class ProducterContext {
    /**
     * 负载均衡节点map
     */
    private Map<Integer,DbConfig> blanceNode;
    /**
     * 当前节点
     */
    private Integer node;

    public Map<Integer, DbConfig> getBlanceNode() {
        return blanceNode;
    }

    public void setBlanceNode(Map<Integer, DbConfig> blanceNode) {
        this.blanceNode = blanceNode;
    }

    public Integer getNode() {
        return node;
    }

    public void setNode(Integer node) {
        this.node = node;
    }
}
