package xyz.douzhan.bank.exception;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/11 13:25
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class TransactionException extends BaseException{
    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
