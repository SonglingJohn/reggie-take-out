package top.sljiang.reggietakeout.filter;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import top.sljiang.reggietakeout.common.BaseContext;
import top.sljiang.reggietakeout.common.R;

import java.io.IOException; // 必须导入IOException

@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    public static final AntPathMatcher PATTERN_MATCHER = new AntPathMatcher();
    //  定义不需要处理的请求路径
    String[] urls = new String[]{
            "/employee/login",
            "/employee/logout",
            "/backend/**",
            "/front/**",
            "/common/**",
            "/user/sendMsg",
            "/user/login"
    };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException { // 声明抛出异常
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}", requestURI);


        //2.判断本次请求是否需要处理
        boolean check = check(urls, requestURI);

        //3.不需要处理则直接放行
        if (check) {
            log.info("本次请求{}不需要处理", requestURI);
            filterChain.doFilter(request, response);
            return ;
        }

        //4-1.判断登录状态，如果已登录则直接放行
        if (request.getSession().getAttribute("employee") != null) {
            Long empId =(Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            log.info("用户已登录，用户id为：{}", request.getSession().getAttribute("employee"));
            long id =Thread.currentThread().getId();
            log.info("线程id为：{}", id);
            filterChain.doFilter(request, response);
            return;
        }

        //4-2.判断登录状态，如果已登录则直接放行
        if (request.getSession().getAttribute("user") != null) {
            Long userId =(Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request, response);
            return;
        }

        log.info("用户未登录");

        //5.如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        // 未登录通常返回401 Unauthorized

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

        return;
    }
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATTERN_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}