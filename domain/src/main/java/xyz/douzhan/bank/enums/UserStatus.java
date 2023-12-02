package xyz.douzhan.bank.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 一些声明信息
 * Description:用户状态
 * date: 2023/11/24 20:01
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Getter
public enum UserStatus {
    NORMAL(0,"正常"),
    FREEZE(1,"冻结"),
    GOODBYE(2,"注销")
    ;
    @EnumValue
    private final int value;
    private final String desc;

    UserStatus(int value, String desc){
        this.value=value;
        this.desc=desc;
    }
}
