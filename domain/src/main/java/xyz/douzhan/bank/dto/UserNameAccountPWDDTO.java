package xyz.douzhan.bank.dto;

import lombok.Data;

/**
 * 一些声明信息
 * Description:
 * date: 2023/11/26 0:05
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
public class UserNameAccountPWDDTO {
    //用户名
    private String username;
    //账号密码
    private String accountPWD;
}
