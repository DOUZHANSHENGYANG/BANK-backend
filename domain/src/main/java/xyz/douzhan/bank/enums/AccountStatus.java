package xyz.douzhan.bank.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 一些声明信息
 * Description: 账户状态
 * date: 2023/12/9 20:14
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Getter
public enum AccountStatus {
    NOT_ACTIVATED(0, "未激活"),
    NORMAL(1, "正常"),
    LOSS(2, "挂失"),
    FREEZE(3, "冻结"),
    LOGOUT(4, "注销"),
    SLEEP(5, "休眠");


    //0开户1正常2挂失3冻结4注销5休眠
    @EnumValue
    private final int value;
    @JsonValue
    private final String desc;

    AccountStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
