package xyz.douzhan.bank.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import xyz.douzhan.bank.result.Result;
import xyz.douzhan.bank.utils.HttpUtils;

import java.io.IOException;

/**
 * 一些声明信息
 * Description: 登录退出处理器
 * date: 2023/11/29 21:09
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {
        //返回结果
        Result result = Result.success().message("登出成功");
        HttpUtils.sendMessage(response,result);
    }
}
