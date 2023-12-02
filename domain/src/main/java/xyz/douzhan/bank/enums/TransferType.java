package xyz.douzhan.bank.enums;

/**
 * 一些声明信息
 * Description: 转账类型
 * date: 2023/11/24 21:30
 *
 * @author 斗战圣洋
 * @since JDK 17
 */

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;
@Getter
public enum TransferType {
    NORMAL_RECEIVE(0,"普通转出"),
    NORMAL_PAYMENT(1,"普通转入"),
    QRCODE_RECEIVE(2,"二维码转出"),
    QRCODE_PAYMENT(3,"二维码转入"),
    ;
    @EnumValue
    private final int value;
    private final String desc;

    TransferType(int value, String desc){
        this.value=value;
        this.desc=desc;
    }
}
