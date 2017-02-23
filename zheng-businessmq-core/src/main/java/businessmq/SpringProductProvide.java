package businessmq;

import businessmq.config.ProducterConfig;
import businessmq.db.DbConfig;
import businessmq.db.dal.BusinessMqNodeDal;
import businessmq.db.model.businessmq.BusinessMqNode;
import businessmq.producter.ProducterProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alan.zheng on 2017/2/14.
 */
public class SpringProductProvide {
    private final ProducterConfig producterConfig;
    private final ProducterProvider producterProvider;

    public SpringProductProvide(ProducterConfig _producterConfig, DbConfig dbConfig, ProducterProvider producterProvider){
        BusinessMqNodeDal businessMqNodeDal=new BusinessMqNodeDal();
        List<BusinessMqNode> nodeList= businessMqNodeDal.queryNodeList(dbConfig);
        Map<Integer,DbConfig> map=new HashMap<>();
        for (BusinessMqNode node:nodeList) {
            DbConfig nodeConfig=new DbConfig();
            nodeConfig.setDriver(node.getDriver());
            nodeConfig.setUrl(node.getUrl());
            nodeConfig.setUsername(node.getUsername());
            nodeConfig.setPassword(node.getPassword());
            map.put(node.getNode(),nodeConfig);
        }
        _producterConfig.setBlanceNode(map);
        producterConfig=_producterConfig;
        this.producterProvider=producterProvider;
    }
    /**
     * 初始化消息监听
     */
    public void send(String msg){
        producterProvider.sendMessage(producterConfig,msg);
    }
}
