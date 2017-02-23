package businessmq.base;

import java.io.Serializable;

/**
 * 消息封装类
 * Created by alan.zheng on 2017/2/14.
 */
public class Message implements Serializable {
    /**
     * 消息持久化id
     */
    private Long id;
    /**
     * 数据库分区id
     */
    private Integer dbId;
    /**
     * 消息
     */
    private String msg;
    /**
     * 消息持久类型
     */
    private String msgType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDbId() {
        return dbId;
    }

    public void setDbId(Integer dbId) {
        this.dbId = dbId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
