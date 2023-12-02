package xyz.douzhan.bank.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 一些声明信息
 * Description:性别
 * date: 2023/11/24 20:35
 *
 * @author 斗战圣洋
 * @since JDK 17
 */

@Getter
public enum Gender {
    male(0,"男"),
    female(1,"女"),
    ;
    @EnumValue
    private final int value;
    private final String desc;

    Gender(int value, String desc){
        this.value=value;
        this.desc=desc;
    }
}
