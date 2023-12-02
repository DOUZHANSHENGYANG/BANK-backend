package xyz.douzhan.bank.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 一些声明信息
 * Description:银行卡类型
 * date: 2023/11/24 20:24
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Getter
public enum BankType {
    First(0,"一类卡"),
    Second(1,"二类卡"),
    third(2,"三类卡")
    ;
    @EnumValue
    private final int value;
    private final String desc;

    BankType(int value, String desc){
        this.value=value;
        this.desc=desc;
    }

}
