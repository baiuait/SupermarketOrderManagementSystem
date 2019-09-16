package cn.smbms.controller;

import cn.smbms.pojo.User;
import cn.smbms.service.user.UserService;
import cn.smbms.tools.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequestMapping("/login")
@Controller
public class LoginController {
    private Logger logger = LogManager.getLogger(this.getClass());

    @Resource(name = "userService")
    private UserService userService;
    //前往登陆页面
    @RequestMapping("/login")
    public String login(HttpSession session){
        logger.debug("UserController welcome SMBMS==========JREBEL");
        //判断是否已经登录成功
        if(null != session.getAttribute(Constants.USER_SESSION)){
            return "redirect:/login/main";
        }
        return "login";
    }

    //验证登陆操作
    @RequestMapping(value = "/dologin", method = RequestMethod.POST)
    public String doLogin(@RequestParam String userCode, @RequestParam String userPassword, HttpServletRequest request){
        logger.debug("doLogin=========================JREBEL");
        //判断用户名是否存在
        User user = userService.selectUserCodeExist(userCode);
        //判断是否存在
        if(null != user){
            //判断密码是否正确
            if(user.getUserPassword().equals(userPassword)){
                HttpSession session = request.getSession();
                //将登陆成功的用户存入session
                session.setAttribute(Constants.USER_SESSION, user);
            }else{
                throw new RuntimeException("密码输入错误!"); //抛出异常来提示密码错误
            }
        }else{
            throw new RuntimeException("用户名不存在!"); //抛出异常来提示用户名不存在
        }
        return "redirect:/login/main"; //重定向 redirect:  转发forward: 默认
    }

    //局部异常处理 当该类中的方法异常为RuntimeException时,调用该方法
//    @ExceptionHandler(value = {RuntimeException.class})
//    public String handlerException(RuntimeException exception, HttpServletRequest request){
//        request.setAttribute("exception",exception);
//        return "login"; //将错误信息传入登录页面提示
//    }

    //前往主页面
    @RequestMapping("/main")
    public String main(HttpSession session){
        //判断是否登陆(直接通过URL进入主页)
        if(null == session.getAttribute(Constants.USER_SESSION)){
            return "redirect:/login/login";
        }
        return "frame";
    }

    //登出操作
    @RequestMapping("/loginOut")
    public String loginOut(HttpSession session){
        //清除session中的用户数据
        session.removeAttribute(Constants.USER_SESSION);
        //重定向至登录页面
        return "redirect:/login/login";
    }
}
