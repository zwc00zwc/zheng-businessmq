package example.controller;

import businessmq.SpringProductProvide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by alan.zheng on 2017/2/27.
 */
@Controller
public class HomeController {
    @Autowired
    private SpringProductProvide springProductProvide;

    @ResponseBody
    @RequestMapping(value = "/send")
    public String send(){
        springProductProvide.send("send");
        return "send";
    }
}
