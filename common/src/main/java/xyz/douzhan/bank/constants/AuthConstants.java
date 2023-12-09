package xyz.douzhan.bank.constants;

/**
 * 一些声明信息
 * Description: 认证常量 特意从业务常量中分开
 * date: 2023/12/8 20:07
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public interface AuthConstants {
    String AUTHENTICATION_PARAMETERS_ARE_NOT_SUPPORTED="认证参数不支持";
    String AUTHENTICATION_PARAMETER_IS_INVALID="认证参数不合法";

    String SMS_REDIS_PREFIX="user:smscode:";
    String IMG_REDIS_PREFIX="user:imgcode:";
    String USR_JWT_PREFIX="user:jwt:";
}
