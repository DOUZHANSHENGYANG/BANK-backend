package xyz.douzhan.bank.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 一些声明信息
 * Description: 交易类型
 * date: 2023/11/24 21:08
 *
 * @author 斗战圣洋
 * @since JDK 17
 */

@Getter
public enum TranscationType {
    INCOME(0, "收入"),
    OUTCOME(1,"支出")
    ;

    @EnumValue
    private final int value;
    private final String desc;

    TranscationType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}

