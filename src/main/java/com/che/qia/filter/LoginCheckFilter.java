package com.che.qia.filter;

import com.alibaba.fastjson.JSON;
import com.che.qia.common.BaseContext;
import com.che.qia.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiaoluyouqu
 * #Description 检查用户是否完成登录
 * #Date: 2022/8/18 14:49
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //通配符 路径匹配器
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        log.info("拦截到请求: {}",request.getRequestURI());
        //1获取本次请求的URI
        String requestURI = request.getRequestURI();
        //2定义不需要处理的请求路径
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/user/logout"

        };
        //3判断本次请求是否需要处理
        boolean flag = check(urls, requestURI);
        //如果不需要处理就放行
        if(flag){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //判断登录状态，如果已经登录，放行
        if(request.getSession().getAttribute("employee")!=null){
            log.info("用户已经登录，用户id为：{}",request.getSession().getAttribute("employee"));
            Long employee = (Long) request.getSession().getAttribute("employee");
            BaseContext.setThreadLocal(employee);
            filterChain.doFilter(request,response);
            return;
        }
        //判断登录状态，如果已经登录，放行
        if(request.getSession().getAttribute("user")!=null){
            log.info("用户已经登录，用户id为：{}",request.getSession().getAttribute("user"));
            Long employee = (Long) request.getSession().getAttribute("user");
            BaseContext.setThreadLocal(employee);
            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户未登录");
        //如果未登录返回未登录结果，通过输出流的方式向客户端响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }
    /**
     * @description:路径匹配，是否放行
     * @author: che
     * @date: 2022/8/18 15:08
     * @param:
     * @return:
     **/
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
