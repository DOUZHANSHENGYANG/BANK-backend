package xyz.douzhan.bank.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
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
public enum BankcardStatus {
    NOT_ACTIVE(0,"未激活"),
    NORMAL(1,"正常"),
    LOCK(2,"锁定")
    ;
    @EnumValue
    private final int value;
    @JsonValue
    private final String desc;

    BankcardStatus(int value, String desc){
        this.value=value;
        this.desc=desc;
    }
}
