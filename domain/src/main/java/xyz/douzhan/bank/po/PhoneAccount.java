package xyz.douzhan.bank.po;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import xyz.douzhan.bank.handler.EncodeTypeHandler;

/**
 * <p>
 * 
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Getter
@Setter
@TableName(value = "phone_account",autoResultMap = true)
@Schema(description = "PhoneAccount对象")
public class PhoneAccount implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "手机账户id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户信息id")
    @TableField("user_info_id")
    private Long userInfoId;

    @Schema(description = "开通银行账户的手机号码")
    @TableField("phone_number")
    private String phoneNumber;


    @Schema(description = "账户密码  长度8-20位 区分大小写 至少包含一个字母和一个数字，支持键盘可见字符（不包含空格，不包括汉字），不允许全角字符 不应该使用生日，身份证号，简单字母数字")
    @TableField(value = "account_pwd",typeHandler = EncodeTypeHandler.class)
    private String accountPWD;

    @Schema(description = "支付密码")
    @TableField(value = "pay_pwd",typeHandler = EncodeTypeHandler.class)
    private String payPWD;

    @Schema(description = "证书url")
    @TableField("certificate")
    private String certificate;

    @Schema(description = "头像url")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "更新时间")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime createTime;
}
