package xyz.douzhan.bank.exception;

/**
 * 一些声明信息
 * Description:业务异常
 * date: 2023/12/7 19:12
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class BizException extends BaseException{
    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }
}
