package xyz.douzhan.bank.exception;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/8 19:30
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class AuthenticationException extends BaseException{
    public AuthenticationException(String message) {
        super("认证异常："+message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super("认证异常："+message, cause);
    }
}
