package businessmq.db.model.businessmq;


import businessmq.db.model.BaseModel;

/**
 * Created by alan.zheng on 2017/2/13.
 */
public class BusinessMq extends BaseModel {
    /**
     * 消息监听名
     */
    private String mqName;
    /**
     * 描述
     */
    private String mqRemark;
    /**
     * 状态
     */
    private Integer status;

    public String getMqName() {
        return mqName;
    }

    public void setMqName(String mqName) {
        this.mqName = mqName;
    }

    public String getMqRemark() {
        return mqRemark;
    }

    public void setMqRemark(String mqRemark) {
        this.mqRemark = mqRemark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
