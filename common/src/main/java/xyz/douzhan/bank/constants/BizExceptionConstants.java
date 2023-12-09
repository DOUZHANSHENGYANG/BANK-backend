package xyz.douzhan.bank.constants;

/**
 * 一些声明信息
 * Description: 业务异常消息常量
 * date: 2023/12/9 12:22
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public interface BizExceptionConstants {
    String BUSINESS_PARAMETERS_ARE_INVALID = "业务参数不合法";
    String BUSINESS_PARAMETERS_ARE_NOT_NULL = "业务参数不能为空";
    String CAN_NOT_INCLUDE_BIRTHDAY = "密码不能包含生日";
    String UNEXPECTED_BUSINESS_ANOMALIES = "意料之外的业务异常";
}
