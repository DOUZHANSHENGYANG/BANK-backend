package xyz.douzhan.bank.constants;

/**
 * 一些声明信息
 * Description: 业务异常消息常量
 * date: 2023/12/9 12:22
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class BizExceptionConstant {
    public static final String FILE_TRANSFER_ERROR="文件传输异常";
    public static final String TRANSFER_RECORD_SELECT_FAILED="转账记录查询失败";
    public static final String NOT_SUPPORT_THE_BIZ_CURRENTLY = "该业务暂不支持，请敬请期待";
    public static final String INVALID_PWD_FORMAT = "密码格式不合法";
    public static final String OLD_ACCOUNT_PWD_ERROR = "旧账户密码错误";
    public static final String OLD_PAY_PWD_ERROR = "旧支付密码错误";
    public static final String PAY_PWD_ERROR = "支付密码错误";
    public static final String ACCOUNT_BALANCE_NOT_ENOUGH = "账户余额不足";
    public static final String BUSINESS_PARAMETERS_ARE_INVALID = "业务参数不合法";
    public static final String BUSINESS_PARAMETERS_ARE_NOT_NULL = "业务参数不能为空";
    public static final String CAN_NOT_INCLUDE_BIRTHDAY = "密码不能包含生日";
    public static final String UNEXPECTED_BUSINESS_ANOMALIES = "意料之外的业务异常";
    public static final String INVALID_NAME = "姓名或拼音填写错误";
    public static final String INVALID_ACCOUNT_NUM = "账户号填写错误";
    public static final String INVALID_ACCOUNT_TEMPLATE = "您的账户当前为%s状态,,请先%s再进行此操作";
    public static final String INVALID_BANKCARD_TEMPLATE = "您的银行卡当前为%s状态,,请先%s再进行此操作";
    public static final String SEND_SHORT_MESSAGE_FAILED = "发送短信失败";
    public static final String INVALID_ACCOUNT_STATUS = "账户状态不合法";
    public static final String INVALID_ACCOUNT_PARAMETER = "非法的查询参数";
    public static final String INVALID_INSTITUTION_CODE = "非法的机构代码";
    public static final String ORDER_STATUS_UPDATE_FAILED = "订单状态已修改或失败";

}
