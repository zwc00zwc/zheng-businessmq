package businessmq.db.model.businessmq;

import domain.model.BaseModel;

import java.util.Date;

/**
 * Created by alan.zheng on 2017/2/13.
 */
public class BusinessMqLog extends BaseModel {
    /**
     * log label
     */
    private String logLabel;
    /**
     * 日志信息
     */
    private String log;
    /**
     * 日志创建时间
     */
    private Date createTime;

    public String getLogLabel() {
        return logLabel;
    }

    public void setLogLabel(String logLabel) {
        this.logLabel = logLabel;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
