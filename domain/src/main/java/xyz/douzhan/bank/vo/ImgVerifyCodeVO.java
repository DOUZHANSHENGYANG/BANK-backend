package xyz.douzhan.bank.vo;



import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "图像验证码")
public class ImgVerifyCodeVO {
    @Schema(description = "唯一标识")
    private String uuid;
    @Schema(description = "图像验证码Base64")
    private String imageBase64Data;
}
