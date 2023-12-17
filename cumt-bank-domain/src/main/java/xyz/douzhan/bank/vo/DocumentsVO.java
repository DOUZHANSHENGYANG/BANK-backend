package xyz.douzhan.bank.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/6 22:58
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Schema(description = "证件信息VO")
public class DocumentsVO {
    @Schema(description = "姓名")
    private String name;
    @Schema(description = "证件号")
    private String IDNumber;
    @Schema(description = "签发机构")
    private String issuingAuthority;
    @Schema(description = "证件有效期")
    private String validity;
}
