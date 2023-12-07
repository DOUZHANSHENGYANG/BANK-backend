package xyz.douzhan.bank.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import xyz.douzhan.bank.result.Result;
import xyz.douzhan.bank.utils.HttpUtils;

import java.io.IOException;


/**
 * 一些声明信息
 * Description:自定义认证失败处理
 * date: 2023/11/29 21:08
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        Result result = Result.error().message(exception.getMessage());
        HttpUtils.sendMessage(response,result);
    }
}
