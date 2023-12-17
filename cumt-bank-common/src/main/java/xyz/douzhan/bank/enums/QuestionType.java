package xyz.douzhan.bank.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 一些声明信息
 * Description:问题类型
 * date: 2023/11/24 21:08
 *
 * @author 斗战圣洋
 * @since JDK 17
 */

@Getter
public enum QuestionType {
    ACCOUNT(0, "账户"),
    TRANSFER(1, "转账"),
    SELECT(1, "查询"),
    SERVICE(1, "便捷服务"),
    OTHER(1, "其他")
    ;

    @EnumValue
    private final int value;
    @JsonValue
    private final String desc;

    QuestionType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}

