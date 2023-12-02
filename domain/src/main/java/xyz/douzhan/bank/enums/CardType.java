package xyz.douzhan.bank.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 一些声明信息
 * Description:证件类型
 * date: 2023/11/24 20:24
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Getter
public enum CardType {
    IC(0,"身份证"),
    PASSPORT(1,"护照"),
    PRPF(2,"外国人永久居留身份证"),
    PRHKMR(3,"港澳居民居住证"),
    PRT(4,"台湾居民身份证"),

    ;
    @EnumValue
    private final int value;
    private final String desc;

    CardType(int value, String desc){
        this.value=value;
        this.desc=desc;
    }

}
