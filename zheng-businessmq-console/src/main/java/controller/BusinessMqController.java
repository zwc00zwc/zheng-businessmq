package controller;

import annotation.Auth;
import businessmq.db.dal.BusinessMqNodeDal;
import businessmq.db.model.PageModel;
import businessmq.db.model.businessmq.BusinessMq;
import businessmq.db.model.businessmq.BusinessMqLog;
import businessmq.db.model.businessmq.BusinessMqNode;
import businessmq.db.model.businessmq.query.BusinessMqLogQuery;
import businessmq.db.model.businessmq.query.BusinessMqNodeQuery;
import businessmq.db.model.businessmq.query.BusinessMqQuery;
import common.JsonResult;
import common.utility.DateUtility;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import services.BusinessMqService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2017/2/12.
 */
@Controller
public class BusinessMqController extends BaseController {
    @Autowired
    private BusinessMqService businessMqService;

    @Auth(rule ="/businessmq/index")
    @RequestMapping(value = "/businessmq/index")
    public String index(Model model,HttpSession httpSession){
        BusinessMqQuery query=new BusinessMqQuery();
        PageModel<BusinessMq> pageModel= businessMqService.queryBusinessMqPage(query);
        model.addAttribute("mqs",pageModel);
        model.addAttribute("user",getAuthUser(httpSession));
        return "/businessmq/index";
    }

    @Auth(rule ="/businessmq/node")
    @RequestMapping(value = "/businessmq/node")
    public String node(Model model,HttpSession httpSession){
        BusinessMqNodeQuery query=new BusinessMqNodeQuery();
        PageModel<BusinessMqNode> pageModel= businessMqService.queryBusinessMqNodePage(query);
        model.addAttribute("pageModel",pageModel);
        model.addAttribute("user",getAuthUser(httpSession));
        return "/businessmq/node";
    }

    @Auth(rule ="/businessmq/node/add")
    @RequestMapping(value = "/businessmq/node/add")
    public String nodeAdd(){
        return "/businessmq/nodeadd";
    }

    @Auth(rule ="/businessmq/node/add")
    @ResponseBody
    @RequestMapping(value = "/businessmq/node/adding")
    public JsonResult nodeAdding(BusinessMqNode businessMqNode){
        try {
            if (businessMqService.insertNode(businessMqNode)){
                return jsonResult(1,"新增成功");
            }
        } catch (Exception e) {
            return jsonResult(-1,"新增失败");
        }
        return jsonResult(-1,"新增失败");
    }

    @Auth(rule ="/businessmq/log")
    @RequestMapping(value = "/businessmq/log")
    public String log(Model model,String queryDate,String startTime,String endTime,Integer currPage,HttpSession httpSession){
        BusinessMqLogQuery query=new BusinessMqLogQuery();
        query.setQueryDate(DateUtility.getDateFromStr(queryDate,"yyyy-MM-dd"));
        if (currPage!=null&&currPage>0){
            query.setCurrPage(currPage);
        }
        if (StringUtils.isNotEmpty(startTime)){
            query.setStartTime(DateUtility.getDateFromStr(queryDate +" "+ startTime,"yyyy-MM-dd hh:mm:ss"));
        }
        if (StringUtils.isNotEmpty(endTime)){
            query.setEndTime(DateUtility.getDateFromStr(queryDate +" "+ endTime,"yyyy-MM-dd hh:mm:ss"));
        }
        PageModel<BusinessMqLog> logPageModel= businessMqService.queryBusinessMqLogPage(query);
        model.addAttribute("queryDate",queryDate);
        model.addAttribute("startTime",startTime);
        model.addAttribute("endTime",endTime);
        model.addAttribute("mqlogs",logPageModel);
        model.addAttribute("user",getAuthUser(httpSession));
        return "/businessmq/log";
    }
}
