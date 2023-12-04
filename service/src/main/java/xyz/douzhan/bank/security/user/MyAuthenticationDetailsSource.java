package xyz.douzhan.bank.security.user;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.stereotype.Component;

/**
 * 一些声明信息
 * Description: 自定义认证附加信息源
 * date: 2023/12/4 13:12
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Component
public class MyAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, MyAuthenticationDetails> {

    public MyAuthenticationDetailsSource() {
    }

    public MyAuthenticationDetails buildDetails(HttpServletRequest request) {
        return new MyAuthenticationDetails(request);
    }
}
