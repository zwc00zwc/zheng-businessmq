package services.impl;

import businessmq.db.dal.BusinessMqNodeDal;
import businessmq.db.model.PageModel;
import businessmq.db.model.businessmq.BusinessMq;
import businessmq.db.model.businessmq.BusinessMqLog;
import businessmq.db.model.businessmq.BusinessMqNode;
import businessmq.db.model.businessmq.query.BusinessMqLogQuery;
import businessmq.db.model.businessmq.query.BusinessMqNodeQuery;
import businessmq.db.model.businessmq.query.BusinessMqQuery;
import org.springframework.stereotype.Service;
import services.BusinessMqService;

import javax.annotation.Resource;

/**
 * Created by alan.zheng on 2017/2/23.
 */
@Service
public class BusinessMqServiceImpl implements BusinessMqService {
    @Resource
    private BusinessMqNodeDal businessMqNodeDal;

    public PageModel<BusinessMq> queryBusinessMqPage(BusinessMqQuery query) {
        return null;
    }

    public PageModel<BusinessMqNode> queryBusinessMqNodePage(BusinessMqNodeQuery query) {
        return null;
    }

    public PageModel<BusinessMqLog> queryBusinessMqLogPage(BusinessMqLogQuery query) {
        return null;
    }

    public boolean insertNode(BusinessMqNode businessMqNode) {
        return false;
    }
}
