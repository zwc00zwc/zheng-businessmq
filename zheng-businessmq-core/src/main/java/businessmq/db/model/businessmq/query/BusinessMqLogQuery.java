package businessmq.db.model.businessmq.query;


import businessmq.db.model.QueryPageModel;

import java.util.Date;

/**
 * Created by alan.zheng on 2017/2/13.
 */
public class BusinessMqLogQuery extends QueryPageModel {
    /**
     * 查询日期
     */
    private Date queryDate;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    public Date getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(Date queryDate) {
        this.queryDate = queryDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
