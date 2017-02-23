package controller;

import common.AuthUser;
import common.Constants;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2016/8/21.
 */
@Controller
public class LoginController extends BaseController {
    @RequestMapping(value = "/login",method = {RequestMethod.GET})
    public String login(String msgcode,Model model){
        if ("-1".equals(msgcode)){
            model.addAttribute("msg","用户名密码错误");
        }
        return "login";
    }

    @RequestMapping(value = "/httplogin",method = {RequestMethod.POST})
    public String httplogin(Model model, @RequestParam(value = "username") String username, @RequestParam(value = "password") String password,
                                  HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView=new ModelAndView();
        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            model.addAttribute("msgcode","-1");
            return "redirect:/login";
        }
        if ("root".equals(username)&&"root".equals(password)){
            AuthUser authUser=new AuthUser();
            authUser.setId(1L);
            authUser.setUserName("root");
            authUser.setDisplayName("root");
            request.getSession().setAttribute(Constants.SESSION_USER_KEY,authUser);
            return "redirect:/";
        }
        return "redirect:/login";
    }

    @RequestMapping(value = "/loginout")
    public RedirectView loginout(HttpSession httpSession){
        httpSession.removeAttribute(Constants.SESSION_USER_KEY);
        return new RedirectView("/login", true);
    }
}
