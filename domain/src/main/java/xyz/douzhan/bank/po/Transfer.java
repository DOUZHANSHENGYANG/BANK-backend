package xyz.douzhan.bank.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Builder;
import lombok.Data;
import xyz.douzhan.bank.enums.BankCardStatus;
import xyz.douzhan.bank.enums.TransactionStatus;
import xyz.douzhan.bank.enums.TransferType;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 一些声明信息
 * Description:转账实体类
 * date: 2023/11/24 20:54
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Builder
@TableName("transfer")
public class Transfer implements Serializable {
    @Serial
    private static final long serialVersionUID=1L;
    //主键
    @TableId(type = IdType.AUTO)
    private Long id;
    //类型（0普通转入1普通转出2二维码转入3二维码转出）
    private TransferType type;
    //交易状态（0进行中1成功2异常）
    private TransactionStatus state;
    //转账人id
    private Long transferorId;
    //被装账人id
    private String transfereeId;
    //交易记录id
    private String transactionId;
    //金额
    private String money;
    //备注
    private String remark;
    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    //创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
}
