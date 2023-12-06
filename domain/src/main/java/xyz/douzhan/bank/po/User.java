package xyz.douzhan.bank.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

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
@TableName("user")
@Schema(description ="User对象")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "个人信息id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "姓名")
    @TableField("name")
    private String name;

    @Schema(description = "姓名拼音")
    @TableField("pin_yin")
    private String pinYin;

    @Schema(description = "性别(0男1女)")
    @TableField("gender")
    private String gender;

    @Schema(description = "证件类型（0代表身份证）")
    @TableField("card_type")
    private String cardType;

    @Schema(description = "证件号码")
    @TableField("card_num")
    private String cardNum;

    @Schema(description = "证件起始日")
    @TableField("card_start_date")
    private LocalDate cardStartDate;

    @Schema(description = "证件到期日")
    @TableField("card_exp_date")
    private LocalDate cardExpDate;

    @Schema(description = "国家/地区")
    @TableField("country")
    private String country;

    @Schema(description = "省/市/区")
    @TableField("region")
    private String region;

    @Schema(description = "民族")
    @TableField("ethnicity")
    private String ethnicity;

    @Schema(description = "职业")
    @TableField("profession")
    private String profession;

    @Schema(description = "手机号码 联系方式 	大部分国家的手机号码通常是10到12位数。	美国和加拿大的手机号是10位数，中国的手机号是11位数，英国的手机号是11位数。")
    @TableField("phone_number")
    private String phoneNumber;

    @Schema(description = "工作单位/学校")
    @TableField("organization")
    private String organization;

    @Schema(description = "详细地址（住所细化到门牌栋号）")
    @TableField("address")
    private String address;

    @Schema(description = "婚姻状况 0未婚1已婚 ")
    @TableField("marital_status")
    private String maritalStatus;

    @Schema(description = "家庭状况 不得超过200字")
    @TableField("family_status")
    private String familyStatus;

    @Schema(description = "个人照片 url 网络存储")
    @TableField("personal_photo")
    private String personalPhoto;

    @Schema(description = "创建时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @Schema(description = "更新时间")
    @TableField("create_time")
    private LocalDateTime createTime;
}
