package application.controller;

import businessmq.SpringProductProvide;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by XR on 2016/12/9.
 */
@RestController
@SpringBootApplication
public class HomeController {
    @Autowired
    private SpringProductProvide springProductProvide;

    @RequestMapping(value = "index")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "send")
    public String send(){
        springProductProvide.send("aaa");
        return "send";
    }
}
