package xyz.douzhan.bank.dto;



import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 一些声明信息
 * Description: 验证码DTO
 * date: 2023/11/30 12:33
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Accessors(fluent = true,chain = true)
//@Schema(name = "验证码")
public class VerifyCodeDTO {
//    @Schema(name = "验证码类型 0为图形 1为短信")
    private Integer type;
//    @Schema(name = "缓存id")
    private String id;
//    @Schema(name = "验证码")
    private String code;

    private String imageBase64Data;
}
