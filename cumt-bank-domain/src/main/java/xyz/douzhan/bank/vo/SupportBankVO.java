package xyz.douzhan.bank.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/10 17:09
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Accessors(fluent = true,chain = true)
@Schema(description = "支持转账银行实体")
public class SupportBankVO {
    @Schema(description = "银行名称")
    String bankName;
    @Schema(description = "银行身份证")
    String swiftCode;
}
