package xyz.douzhan.bank.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

/**
 * 一些声明信息
 * Description:
 * date: 2023/11/26 0:00
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Schema(description = "人脸认证DTO")
public class FaceAuthDTO {
    @Schema(description = "姓名")
    private String name;
    @Schema(description = "身份证号")
    private String ICNum;
}
