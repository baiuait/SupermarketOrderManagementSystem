package cn.smbms.interceptor;

import cn.smbms.pojo.User;
import cn.smbms.tools.Constants;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 定义拦截器,判断用户session
 */
public class SysInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        System.out.println("=================执行拦截===================");
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(Constants.USER_SESSION);
        if(null == user){
            response.sendRedirect(request.getContextPath()+"/401.jsp");
            return false;
        }
        return true;
    }
}
