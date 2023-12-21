package xyz.douzhan.bank.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Description:
 * date: 2023/12/19 19:13
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Schema(description = "上传证件DTO")
public class UploadDocsDTO {
    @Schema(description = "证件正面图片")
    private MultipartFile frontFile;
    @Schema(description = "证件反面图片")
    private MultipartFile backFile;
    @Schema(description = "I类账户id")
    private Long firstAccountId;
}
