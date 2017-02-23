package services;

import businessmq.db.model.PageModel;
import businessmq.db.model.businessmq.BusinessMq;
import businessmq.db.model.businessmq.BusinessMqLog;
import businessmq.db.model.businessmq.BusinessMqNode;
import businessmq.db.model.businessmq.query.BusinessMqLogQuery;
import businessmq.db.model.businessmq.query.BusinessMqNodeQuery;
import businessmq.db.model.businessmq.query.BusinessMqQuery;

/**
 * Created by alan.zheng on 2017/2/23.
 */
public interface BusinessMqService {
    PageModel<BusinessMq> queryBusinessMqPage(BusinessMqQuery query);

    PageModel<BusinessMqNode> queryBusinessMqNodePage(BusinessMqNodeQuery query);

    PageModel<BusinessMqLog> queryBusinessMqLogPage(BusinessMqLogQuery query);

    public boolean insertNode(BusinessMqNode businessMqNode);
}
