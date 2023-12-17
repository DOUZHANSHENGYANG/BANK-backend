package xyz.douzhan.bank.exception;

/**
 * 一些声明信息
 * Description: 自定义异常父类
 * date: 2023/12/7 19:08
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class BaseException extends RuntimeException{


    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
