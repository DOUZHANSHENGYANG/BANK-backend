package xyz.douzhan.bank.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
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
public enum AccountType {
    FIRST(0,"I类"),
    SECOND(1,"II类"),
    THIRD(2,"III类")
    ;
    @EnumValue
    private final int value;
    @JsonValue
    private final String desc;

    AccountType(int value, String desc){
        this.value=value;
        this.desc=desc;
    }

}
