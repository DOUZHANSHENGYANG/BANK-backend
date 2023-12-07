package xyz.douzhan.bank.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import xyz.douzhan.bank.result.Result;
import xyz.douzhan.bank.security.user.CustomUserDetails;
import xyz.douzhan.bank.security.user.JWTUserRedis;
import xyz.douzhan.bank.utils.HttpUtils;
import xyz.douzhan.bank.utils.JWTUtils;
import xyz.douzhan.bank.utils.RedisUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 一些声明信息
 * Description: 自定义成功认证 生成token放缓存并返回
 * date: 2023/11/29 21:08
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String token = JWTUtils.genToken(String.valueOf(userDetails.getPhoneAccountId()));

        JWTUserRedis jwtUserRedis = new JWTUserRedis()
                        .phoneAccountId(userDetails.getPhoneAccountId())
                        .authorities(userDetails.getAuthorities())
                        .loginIp(HttpUtils.getRemoteHostIP(request))
                        .loginTime(LocalDateTime.now())
                        .loginLocation(request.getHeader("UserInfo-Agent"));
        //放入缓存
        RedisUtils.setWithExpire("user:jwt:"+token,jwtUserRedis,JWTUtils.getJwtProperties().getTtl(), TimeUnit.DAYS);
        //返回结果
        Result result = Result.success(token).message("登录成功");
        HttpUtils.sendMessage(response,result);
    }
}
