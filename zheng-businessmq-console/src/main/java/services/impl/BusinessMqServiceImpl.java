package services.impl;

import businessmq.db.dal.BusinessMqNodeDal;
import businessmq.db.model.PageModel;
import businessmq.db.model.businessmq.BusinessMq;
import businessmq.db.model.businessmq.BusinessMqLog;
import businessmq.db.model.businessmq.BusinessMqNode;
import businessmq.db.model.businessmq.query.BusinessMqLogQuery;
import businessmq.db.model.businessmq.query.BusinessMqNodeQuery;
import businessmq.db.model.businessmq.query.BusinessMqQuery;
import businessmq.reg.zookeeper.ZookeeperConfig;
import businessmq.reg.zookeeper.ZookeeperRegistryCenter;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import common.mongodb.MongodbManager;
import common.utility.DateUtility;
import common.utility.PropertiesUtility;
import org.bson.Document;
import org.springframework.stereotype.Service;
import services.BusinessMqService;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by alan.zheng on 2017/2/23.
 */
@Service
public class BusinessMqServiceImpl implements BusinessMqService {
    @Resource
    private BusinessMqNodeDal businessMqNodeDal;

    public PageModel<BusinessMq> queryBusinessMqPage(BusinessMqQuery query) {
        ZookeeperConfig zookeeperConfig=new ZookeeperConfig();
        PropertiesUtility propertiesUtility=new PropertiesUtility("zookeeper.properties");
        zookeeperConfig.setServerLists(propertiesUtility.getProperty("zkmq.serverList"));
        zookeeperConfig.setNamespace(propertiesUtility.getProperty("zkmq.namespace"));
        zookeeperConfig.setAuth(propertiesUtility.getProperty("zkmq.auth"));
        ZookeeperRegistryCenter zookeeperRegistryCenter= null;
        try {
            zookeeperRegistryCenter = new ZookeeperRegistryCenter(zookeeperConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
        zookeeperRegistryCenter.init();
        List<String> keys= zookeeperRegistryCenter.getChildrenKeys("/mq");
        List<BusinessMq> list=new ArrayList<BusinessMq>();
        for (int i=0;i<keys.size();i++){
            BusinessMq businessmq=new BusinessMq();
            businessmq.setMqName(keys.get(i));
            businessmq.setMqRemark(zookeeperRegistryCenter.get("/mq/"+keys.get(i)));
            businessmq.setStatus(1);
            list.add(businessmq);
        }
        zookeeperRegistryCenter.close();
        return new PageModel<BusinessMq>(list,query.getCurrPage(),keys.size(),query.getPageSize());
    }

    public PageModel<BusinessMqNode> queryBusinessMqNodePage(BusinessMqNodeQuery query) {
        List<BusinessMqNode> list = businessMqNodeDal.queryPageList(query);
        Integer count= businessMqNodeDal.queryCountPage(query);
        PageModel<BusinessMqNode> pageModel=new PageModel<BusinessMqNode>(list,query.getCurrPage(),count,query.getPageSize());
        return pageModel;
    }

    public PageModel<BusinessMqLog> queryBusinessMqLogPage(BusinessMqLogQuery query) {
        String collectionname="";
        if (query.getQueryDate()==null){
            collectionname= DateUtility.getStrFromDate(new Date(),"yyyyMMdd")+"_log";
        }else {
            collectionname= DateUtility.getStrFromDate(query.getQueryDate(),"yyyyMMdd")+"_log";
        }
        MongoCollection collection= MongodbManager.getDatabase("BusinessMqLog").getCollection(collectionname);
        List<BusinessMqLog> list=new ArrayList<BusinessMqLog>();
        BasicDBObject basicDBObject=new BasicDBObject();
        BasicDBObject timebasesic=new BasicDBObject();
        Calendar calendar  =  new GregorianCalendar();
        if (query.getStartTime()!=null){
            calendar.setTime(query.getStartTime());
            calendar.add(Calendar.HOUR,8);//时区关系加8小时
            timebasesic.append("$gte",calendar.getTime());
        }
        if (query.getEndTime()!=null){
            calendar.setTime(query.getEndTime());
            calendar.add(Calendar.HOUR,8);//时区关系加8小时
            timebasesic.append("$lte",calendar.getTime());
        }
        if (timebasesic.size()>0){
            basicDBObject.put("createTime",timebasesic);
        }
        MongoCursor mongoCursor = collection.find(basicDBObject).sort(new BasicDBObject("createTime", -1)).skip(query.getStartRow()).limit(query.getPageSize()).iterator();
        while (mongoCursor.hasNext()){
            Document document=(Document) mongoCursor.next();
            BusinessMqLog businessMqLog= new BusinessMqLog();
            businessMqLog.setLogLabel(document.get("logLabel").toString());
            businessMqLog.setLog(document.get("log").toString());
            calendar.setTime((Date) document.get("createTime"));
            calendar.add(Calendar.HOUR,-8);//时区关系加8小时
            businessMqLog.setCreateTime(calendar.getTime());
            list.add(businessMqLog);
        }
        int i=0;
        MongoCursor mongoCursor1 = collection.find(basicDBObject).sort(new BasicDBObject("createTime", -1)).iterator();
        while (mongoCursor1.hasNext()){
            i++;
            mongoCursor1.next();
        }
        PageModel<BusinessMqLog> pageModel=new PageModel<BusinessMqLog>(list,query.getCurrPage(),i,query.getPageSize());
        return pageModel;
    }

    public boolean insertNode(BusinessMqNode businessMqNode) {
        return businessMqNodeDal.insertNode(businessMqNode);
    }
}
