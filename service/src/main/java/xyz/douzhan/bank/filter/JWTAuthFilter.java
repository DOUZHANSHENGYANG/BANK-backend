package xyz.douzhan.bank.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import xyz.douzhan.bank.constants.AuthConstant;
import xyz.douzhan.bank.context.UserContext;
import xyz.douzhan.bank.exception.AuthenticationException;
import xyz.douzhan.bank.redis.LoginInfoRedis;
import xyz.douzhan.bank.utils.HttpUtils;
import xyz.douzhan.bank.utils.JWTUtils;
import xyz.douzhan.bank.utils.RedisUtils;

import java.io.IOException;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/8 19:13
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Slf4j
@WebFilter(urlPatterns = "/*")
public class JWTAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;
        //是否匹配放行白名单
        Boolean isMatch = HttpUtils.match(AuthConstant.WHITE_LIST, request);
        if (isMatch){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }


        //1.从请求头尝试获取token
        String authHeader = request.getHeader(JWTUtils.getJwtProperties().getTokenName());
        //2.没有token,则可能是去登录，放行
        if (authHeader==null||!authHeader.startsWith("Bearer ")){
            throw new AuthenticationException("未进行登录,请登录");
        }
        //3.有就获取token
        String token = authHeader.substring(7);
        //4.从缓存尝试获取token
        LoginInfoRedis userRedis = (LoginInfoRedis) RedisUtils.get("user:jwt:" + token);


        if (userRedis==null){
           throw new AuthenticationException("未进行登录，请登录");
        }

        Long phoneAccountId  = (Long) userRedis.phoneAccountId();
        UserContext.setContext(phoneAccountId);

        filterChain.doFilter(servletRequest,servletResponse);
    }
}
