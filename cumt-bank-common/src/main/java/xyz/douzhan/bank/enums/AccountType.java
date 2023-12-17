package xyz.douzhan.bank.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * Description:
 * date: 2023/12/16 11:18
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Getter
public enum AccountType {
    FIRST(0, "I类"),
    SECOND(1, "II类"),
    THIRD(2, "III类")
    ;
    @EnumValue

    private final int value;


    private final String desc;
    AccountType(int value, String desc){
        this.value=value;
        this.desc=desc;
    }

}
