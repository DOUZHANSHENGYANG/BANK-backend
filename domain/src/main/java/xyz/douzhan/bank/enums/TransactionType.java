package xyz.douzhan.bank.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
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
public enum TransactionType {
    NORMAL_RECEIVE(0,"普通转入"),
    NORMAL_PAYMENT(1,"普通转出"),
    QRCODE_RECEIVE(2,"二维码转出入"),
    QRCODE_PAYMENT(3,"二维码转出"),
    ;

    @EnumValue
    private final int value;
    @JsonValue
    private final String desc;

    TransactionType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}

