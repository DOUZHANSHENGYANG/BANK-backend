package xyz.douzhan.bank.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 一些声明信息
 * Description: 交易状态
 * date: 2023/11/24 21:23
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Getter
public enum TransactionStatus {
    ONGOING(0,"交易进行中"),
    SUCCESS(1,"交易成功"),
    ERROR(2,"交易异常")
    ;
    @EnumValue
    private final int value;
    private final String desc;

    TransactionStatus(int value, String desc){
        this.value=value;
        this.desc=desc;
    }
}

