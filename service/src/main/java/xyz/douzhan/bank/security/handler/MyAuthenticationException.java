package xyz.douzhan.bank.security.handler;

import org.springframework.security.core.AuthenticationException;

/**
 * 一些声明信息
 * Description: 自定义认证异常
 * date: 2023/11/28 20:10
 *
 * @author 斗战圣洋
 * @since JDK 17
 */

public class MyAuthenticationException extends AuthenticationException {

    public MyAuthenticationException(String msg) {
        super(msg);
    }

}
