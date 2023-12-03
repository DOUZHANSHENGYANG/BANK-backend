package xyz.douzhan.bank.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import xyz.douzhan.bank.result.Result;
import xyz.douzhan.bank.utils.HttpUtils;

import java.io.IOException;

/**
 * 一些声明信息
 * Description: 处理权限不足异常
 * date: 2023/11/29 21:07
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        Result result = Result.error().message("权限不足" + accessDeniedException.getMessage());
        HttpUtils.sendMessage(response,result);
    }
}
