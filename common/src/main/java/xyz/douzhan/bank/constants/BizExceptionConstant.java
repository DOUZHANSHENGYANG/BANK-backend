package xyz.douzhan.bank.constants;

/**
 * 一些声明信息
 * Description: 业务异常消息常量
 * date: 2023/12/9 12:22
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public interface BizExceptionConstant {
    String ACCOUNT_BALANCE_NOT_ENOUGH = "账户余额不足";
    String BUSINESS_PARAMETERS_ARE_INVALID = "业务参数不合法";
    String BUSINESS_PARAMETERS_ARE_NOT_NULL = "业务参数不能为空";
    String CAN_NOT_INCLUDE_BIRTHDAY = "密码不能包含生日";
    String UNEXPECTED_BUSINESS_ANOMALIES = "意料之外的业务异常";
    String INVALID_NAME = "姓名或拼音填写错误";
    String INVALID_ACCOUNT_NUM = "账户号填写错误";
    String INVALID_ACCOUNT_TEMPLATE="您的账户当前为%s状态,,请先%s再进行此操作";
    String INVALID_BANKCARD_TEMPLATE="您的银行卡当前为%s状态,,请先%s再进行此操作";
    String SEND_SHORT_MESSAGE_FAILED="发送短信失败";
}
