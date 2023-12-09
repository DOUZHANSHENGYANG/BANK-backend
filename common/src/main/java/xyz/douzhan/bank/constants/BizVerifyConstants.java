package xyz.douzhan.bank.constants;

import java.util.List;

/**
 * 一些声明信息
 * Description: 业务校验常量
 * date: 2023/12/9 12:32
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public interface BizVerifyConstants {
    String ACCOUNT_PASSWORD_REGEX="^(?=.*[a-zA-Z])(?=.*\\d).{8,20}$";
    String SIMPLE_DIGITAL_AND_CHARACTER= "(01234|12345|23456|34567|45678|56789|abcde|bcdef|cdefg|defgh|efghi|fghij|ABCDE|BCDEF|CDEFG|DEFGH|EFGHI|FGHIJ)";
}
