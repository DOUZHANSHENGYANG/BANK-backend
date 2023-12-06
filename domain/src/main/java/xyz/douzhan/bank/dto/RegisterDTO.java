package xyz.douzhan.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 一些声明信息
 * Description:注册DTO
 * date: 2023/12/4 15:26
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
public class RegisterDTO {
    @NotBlank(message = "名字不能为空")
    @Schema(description = "姓名")
    private String name;
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$",message = "身份证号格式错误")
    @Schema(description = "身份证号")
    private String ICNum;
//    private String
}
