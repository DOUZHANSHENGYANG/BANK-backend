package xyz.douzhan.bank.security.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import xyz.douzhan.bank.security.handler.MyAuthenticationException;
import xyz.douzhan.bank.security.token.SmsCodeAuthenticationToken;
import xyz.douzhan.bank.utils.HttpUtils;
import xyz.douzhan.bank.utils.RedisUtils;

import java.io.IOException;

/**
 * 一些声明信息
 * Description: 手机号登录拦截器
 * date: 2023/11/28 19:57
 *
 * @author 斗战圣洋
 * @since JDK 17
 */

public class SMSCodeLoginFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_MOBILE_KEY = "mobile";
    public static final String SPRING_SECURITY_CODE_KEY = "smsCode";
    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/common/auth/login/mobile", "POST");
    private final String mobileParameter = SPRING_SECURITY_MOBILE_KEY;
    private final String codeParameter = SPRING_SECURITY_CODE_KEY;

    private final boolean postOnly = true;


    public SMSCodeLoginFilter(AuthenticationManager authManager,
                              AuthenticationSuccessHandler successHandler,
                              AuthenticationFailureHandler failureHandler,
                              ApplicationEventPublisher eventPublisher) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        setAuthenticationManager(authManager);
        setAuthenticationSuccessHandler(successHandler);
        setAuthenticationFailureHandler(failureHandler);
        setApplicationEventPublisher(eventPublisher);
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (!postOnly||!request.getMethod().equals("POST")){
            throw new AuthenticationServiceException("认证方法不支持: " + request.getMethod());
        }
        if (!request.getContentType().contains(MediaType.APPLICATION_JSON.toString())){
            throw new AuthenticationServiceException("认证参数不支持");
        }
        //获取手机号和验证码
        String body = HttpUtils.getBody(request);
        JSONObject jsonObject = JSON.parseObject(body);
        String mobile = jsonObject.getString(mobileParameter);
        String code = jsonObject.getString(codeParameter);

        if (mobile==null){
            mobile="";
        }
        if (code==null){
            code="";
        }

        String redisCode = (String) RedisUtils.get("user:mobile:"+mobile);
        //TODO 重新恢复
//        RedisUtils.del(mobile);

//        if (StrUtil.isEmpty(redisCode)){
//            throw new MyAuthenticationException("验证码不存在或已过期");
//        }
        if (StrUtil.isBlank(code)||StrUtil.equals(code,redisCode,true)){
            throw new MyAuthenticationException("验证码错误");
        }

        SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(mobile,code);
        this.setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
    protected void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
