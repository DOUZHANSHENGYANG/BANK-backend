package xyz.douzhan.bank.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;
import xyz.douzhan.bank.enums.BankCardStatus;
import xyz.douzhan.bank.enums.BankType;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 一些声明信息
 * Description:银行卡实体类
 * date: 2023/11/24 20:54
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Builder
@TableName("bank_card")
public class BankCard implements Serializable {
    @Serial
    private static final long serialVersionUID=1L;
    //主键
    @TableId(type = IdType.AUTO)
    private Long id;
    //用户id
    private Long userId;
    //卡类型
    private BankType type;
    //银行卡号
    private String number;
    //余额
    private String money;
    //别名
    private String alias;
    //银行卡状态（0未激活1已激活未锁定2已激活已锁定）
    private BankCardStatus status;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    //创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
}
