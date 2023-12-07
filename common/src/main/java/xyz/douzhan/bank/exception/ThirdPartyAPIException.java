package xyz.douzhan.bank.exception;

/**
 * 一些声明信息
 * Description:第三方接口调用异常
 * date: 2023/12/7 19:13
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class ThirdPartyAPIException extends BaseException{
    public ThirdPartyAPIException(String message) {
        super(message);
    }

    public ThirdPartyAPIException(String message, Throwable cause) {
        super(message, cause);
    }
}
