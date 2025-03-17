package com.oneflow.prm.advice.interceptor;

import com.oneflow.prm.core.utils.TokenUtil;
import com.oneflow.prm.entity.dao.sys.SysUser;
import com.oneflow.prm.entity.sys.LoginUser;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 注意：定义了Interceptor后要将其注册到WebMvcConfigurer中
 *
 * 参考：https://blog.csdn.net/weixin_46649054/article/details/118355986
 */
public class ContextInterceptor implements HandlerInterceptor {

    // 在请求发生前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SysUser sysUser = TokenUtil.getLoginUser(request);
        assert sysUser != null;
        LoginUser loginUser = new LoginUser().setUserId(sysUser.getUserId()).setUserName(sysUser.getUserName());
        LoginUserContext.setThreadLocal(loginUser);
        return true;
    }

    // 当preHandle方法返回值为true的时候才会执行。
    // 重写postHandle方法，在请求完成后执行。
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("------postHandle执行了------");
    }

    // 当preHandle方法返回值为true的时候才会执行。
    // 在DispatcherServlet完全处理完请求后被调用，可用于清理资源等，返回处理（已经渲染了页面）
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginUserContext.remove();
    }
}
