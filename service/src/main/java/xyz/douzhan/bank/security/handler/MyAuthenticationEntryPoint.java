package xyz.douzhan.bank.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import xyz.douzhan.bank.result.Result;
import xyz.douzhan.bank.utils.HttpUtils;

import java.io.IOException;

/**
 * 一些声明信息
 * Description: 身份认证异常处理
 * date: 2023/11/29 21:08
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Result result = Result.error().message("认证异常" + authException.getMessage());
        HttpUtils.sendMessage(response,result);
    }
}
