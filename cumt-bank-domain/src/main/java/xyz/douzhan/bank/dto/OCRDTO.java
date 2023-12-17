package xyz.douzhan.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/6 22:55
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Schema(description = "图像识别DTO")
public class OCRDTO {
    @Schema(description = "类型 0为银行卡 1身份证")
    private Integer type;
    @Schema(description = "证件的朝向 0正面 1反面")
    private Integer side;
}
