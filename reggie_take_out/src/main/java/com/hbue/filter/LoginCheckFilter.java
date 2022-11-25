package com.hbue.filter;


import com.alibaba.fastjson.JSON;
import com.hbue.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //路径匹配器,支持通配符
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取本次请求的URI
        String uri = request.getRequestURI();
        log.info("拦截到了请求:{}", uri);
        //定义不需要处理的路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"
        };
        //判断本次的请求是否需要处理
        boolean flag = check(urls, uri);
        //如果flag为true,则表示不需要做任何处理,放行
        if (flag) {
            log.info("本次请求{}不作处理", uri);
            filterChain.doFilter(request, response);
            return;
        }
        //如果不是直接放行的路径,则需要先判断是否处于登录
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已登录,用户id为:{}", request.getSession().getAttribute("employee"));
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //如果用户没有登录,通过输入流的方式向页面输出数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配,判断当前路径是否需要放行
     *
     * @param urls
     * @param uri
     * @return
     */
    private boolean check(String[] urls, String uri) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, uri);
            //如果跟可以放行的路径匹配,返回true
            if (match) {
                return true;
            }
        }
        return false;
    }
}
