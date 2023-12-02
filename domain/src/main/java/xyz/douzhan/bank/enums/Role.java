package xyz.douzhan.bank.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 一些声明信息
 * Description:用户角色枚举类
 * date: 2023/11/25 16:46
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Getter
public enum Role {
    USER(0, "用户"),
    ADMIN(1, "管理员")
    ;

    @EnumValue
    private int value;
    private String desc;

    Role(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
