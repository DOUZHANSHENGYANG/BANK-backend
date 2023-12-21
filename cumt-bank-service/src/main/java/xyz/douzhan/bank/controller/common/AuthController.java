package xyz.douzhan.bank.controller.common;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.douzhan.bank.dto.OCRDTO;
import xyz.douzhan.bank.dto.ValidateVerifyCodeDTO;
import xyz.douzhan.bank.dto.result.ResponseResult;
import xyz.douzhan.bank.service.AuthService;
import xyz.douzhan.bank.vo.ImgVerifyCodeVO;

import java.util.Map;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/3 13:03
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Tag(name = "用户认证")
@RestController
@RequestMapping("/bank/common/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/vc")
    @Operation(summary = "根据类型获取验证码")
    public ResponseResult getVerifyCode(
            @RequestParam(name = "type") @Parameter(description = "0为图形验证码，1为短信验证码") Integer type,
            @RequestParam(name = "exp", required = false, defaultValue = "60") @Parameter(description = "过期时间,默认为60s") Integer exp,
            @RequestParam(name = "phoneNumber", required = false) @Parameter(description = "手机号") String phoneNumber
    ) {
        ImgVerifyCodeVO imgVerifyCodeVO = authService.getVerifyCode(type, exp, phoneNumber);
        return ResponseResult.success(imgVerifyCodeVO);
    }

    @PostMapping("/vc")
    @Operation(summary = "验证码校验")
    public ResponseResult validateVerifyCode(
            @RequestBody @Parameter(description = "ValidateVerifyCodeDTO") ValidateVerifyCodeDTO verifyCodeDTO
    ) {
        authService.validateVerifyCode(verifyCodeDTO);
        return ResponseResult.success();
    }

    @PostMapping("/upload-doc")
    @Operation(summary = "上传证件")
    public ResponseResult uploadDocuments(
            @RequestParam("frontFile") @Parameter(description = "证件正面图片") MultipartFile frontFile,
            @RequestParam("backFile") @Parameter(description = "证件反面图片") MultipartFile backFile,
            @RequestParam("firstAccountId") @Parameter(description = "I类账户id") Long firstAccountId) {
        JSONObject result = authService.uploadDocuments(frontFile, backFile, firstAccountId);
        return ResponseResult.success(result);
    }

    @PostMapping("/ocr")
    @Operation(summary = "图像识别")
    public ResponseResult ocr(
            @RequestBody @Parameter(description = "OCR实体") OCRDTO ocrdto,
            @RequestParam("file") @Parameter(description = "图片文件") MultipartFile file
    ) {
        Map<String, Object> ocrResult = authService.ocr(ocrdto, file);
        return ResponseResult.success(ocrResult);
    }

    @PostMapping("/face-temp")
    @Operation(summary = "注册前的人脸认证 主要是没这样的接口只能笨着来")
    public ResponseResult faceAuthTemp(
            @RequestParam("frontFile") @Parameter(description = "证件正面照片") MultipartFile frontFile,
            @RequestParam("liveFile") @Parameter(description = "实时人脸照片") MultipartFile liveFile
    ) {
        boolean result = authService.faceAuthTemp(frontFile, liveFile);
        if (result) {
            return ResponseResult.success();
        } else {
            return ResponseResult.error();
        }
    }
    @PostMapping("/face")
    @Operation(summary = "注册后的人脸认证")
    public ResponseResult faceAuth(
            @RequestParam("liveFile") @Parameter(description = "实时人脸照片") MultipartFile liveFile
    ) {
        Boolean result = authService.faceAuth(liveFile);
        if (result) {
            return ResponseResult.success();
        } else {
            return ResponseResult.error();
        }
    }


}
