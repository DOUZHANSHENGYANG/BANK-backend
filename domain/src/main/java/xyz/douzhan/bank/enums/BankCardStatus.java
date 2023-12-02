package xyz.douzhan.bank.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 一些声明信息
 * Description: 银行卡状态
 * date: 2023/11/24 20:57
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Getter
public enum BankCardStatus {
    NOT_ACTIVE(0,"未激活"),
    ACTIVE_UNLOCKED(1,"已激活未锁定"),
    ACTIVE_LOCKED(2,"已激活已锁定")
    ;
    @EnumValue
    private final int value;
    private final String desc;

    BankCardStatus(int value, String desc){
        this.value=value;
        this.desc=desc;
    }
}
