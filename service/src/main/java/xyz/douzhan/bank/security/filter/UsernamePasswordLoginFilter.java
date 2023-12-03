package xyz.douzhan.bank.security.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import xyz.douzhan.bank.security.handler.MyAuthenticationException;
import xyz.douzhan.bank.security.token.JwtAuthenticationToken;
import xyz.douzhan.bank.utils.HttpUtils;
import xyz.douzhan.bank.utils.RedisUtils;

import java.io.IOException;

/**
 * 一些声明信息
 * Description: 账号密码登录拦截器
 * date: 2023/11/28 12:35
 *
 * @author 斗战圣洋
 * @since JDK 17
 */

public class UsernamePasswordLoginFilter extends UsernamePasswordAuthenticationFilter {
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/common/auth/login/up", "POST");

    private boolean postOnly=true;
    public UsernamePasswordLoginFilter(AuthenticationManager authenticationManager,
                                       AuthenticationSuccessHandler successHandler,
                                       AuthenticationFailureHandler failureHandler,
                                       ApplicationEventPublisher eventPublisher){
        //设置拦截的路径
        setRequiresAuthenticationRequestMatcher(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        //设置交给哪个安全管理器验证
        setAuthenticationManager(authenticationManager);
        //设置认证成功处理器
        setAuthenticationSuccessHandler(successHandler);
        //设置认证失败处理器
        setAuthenticationFailureHandler(failureHandler);
        //设置事件发布器
        setApplicationEventPublisher(eventPublisher);
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //父类doFilter（）会调用下面的attemptAuthentication（）进行验证，来判断放不放行或报错
        super.doFilter(request, response, chain);
    }

    /**
     * 账号密码登录 以及 验证码验证
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //从请求中获取请求体
        if (!this.postOnly||!request.getMethod().equals("POST")){
            throw new AuthenticationServiceException("认证方法不支持: " + request.getMethod());
        }
        if (!request.getContentType().contains(MediaType.APPLICATION_JSON.toString())){
            throw new AuthenticationServiceException("认证参数不支持: " + request.getMethod());
        }
        //首先获取用户验证码
        String body = HttpUtils.getBody(request);
        JSONObject jsonObject = JSON.parseObject(body);

        String uuid = jsonObject.getString("uuid");
        String imgCode = jsonObject.getString("imgCode");
        //从redis查询先前生成的验证码
        String redisImgCode = (String) RedisUtils.get("user:imgcode:"+uuid);
//        //清除redis里的验证码
//        RedisUtils.del(uuid);
        //是否获取过验证码
        if (StrUtil.isBlank(redisImgCode)){
            throw new MyAuthenticationException("验证码不存在或过期");
        }
        //比较验证码
        if (StrUtil.isEmpty(imgCode)||!StrUtil.equals(imgCode,redisImgCode,false)){
            throw new MyAuthenticationException("验证码不正确");
        }
        //验证码通过再获取用户名和密码
        String username = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        //封装JwtAuthenticationToken交给manager验证
        JwtAuthenticationToken authRequest = new JwtAuthenticationToken(username.trim(), password);
        setDetails(request,authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
