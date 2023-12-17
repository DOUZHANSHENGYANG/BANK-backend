package xyz.douzhan.bank.constants;


/**
 * 一些声明信息
 * Description: 业务校验常量
 * date: 2023/12/9 12:32
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class AuthExceptionConstant {
    public static final String ACCOUNT_PASSWORD_REGEX="^(?=.*[a-zA-Z])(?=.*\\d).{8,20}$";
    public static final String SIMPLE_DIGITAL_AND_CHARACTER= "(01234|12345|23456|34567|45678|56789|abcde|bcdef|cdefg|defgh|efghi|fghij|ABCDE|BCDEF|CDEFG|DEFGH|EFGHI|FGHIJ)";
    public static final String AUTHENTICATION_PARAMETERS_ARE_NOT_SUPPORTED="认证参数不支持";
    public static final String AUTHENTICATION_PARAMETER_IS_INVALID="认证参数不合法";
    public static final String INVALID_PAY_PWD = "支付错误";
    public static final String VERFIY_CODE_ERROR = "验证码错误或已过期";
    public static final String DOCUMENTS_ERROR = "证件号码错误";
}
