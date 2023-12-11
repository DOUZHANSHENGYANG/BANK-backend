package xyz.douzhan.bank.controller.auth;

import cn.hutool.core.codec.Base64;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.douzhan.bank.constants.AuthConstant;
import xyz.douzhan.bank.dto.FaceAuthDTO;
import xyz.douzhan.bank.dto.OCRDTO;
import xyz.douzhan.bank.dto.ValidateVerifyCodeDTO;
import xyz.douzhan.bank.result.Result;
import xyz.douzhan.bank.service.AuthService;
import xyz.douzhan.bank.utils.BaiduAIUtils;
import xyz.douzhan.bank.vo.ImgVerifyCodeVO;

import java.io.IOException;
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
@RequestMapping("/common/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/vc")
    @Operation(summary = "根据类型获取验证码")
    public Result getVerifyCode(
            @RequestParam(name = "type") @Parameter(name = "type", description = "0为图形验证码，1为短信验证码")
            Integer type,
            @RequestParam(name = "exp", required = false, defaultValue = "60") @Parameter(name = "exp", description = "过期时间,默认为60s")
            Integer exp,
            @RequestParam(name = "phoneNumber", required = false) @Parameter(name = "phoneNumber", description = "手机号")
            String phoneNumber
    )  {
        ImgVerifyCodeVO imgVerifyCodeVO=authService.getVerifyCode(type,exp,phoneNumber);
        return Result.success(imgVerifyCodeVO);
    }

    @PostMapping("/vc")
    @Operation(summary = "验证码校验")
    public Result validateVerifyCode(
            @RequestBody @Parameter(description = "ValidateVerifyCodeDTO") ValidateVerifyCodeDTO verifyCodeDTO
    ) {
        authService.validateVerifyCode(verifyCodeDTO);
        return  Result.success();
    }

    @PostMapping("/ocr")
    @Operation(summary = "图像识别")
    public Result validateVerifyCode(
            @RequestBody @Parameter(description = "OCR实体") OCRDTO ocrdto,
            @RequestParam("file") @Parameter(description = "图片文件") MultipartFile file
    ) throws IOException {
        //将图片文件编码成base64字符串
        String base64Img = Base64.encode(file.getInputStream());
        if (ocrdto.getType() == 0) {
            String side = "";
            if (ocrdto.getSide() == 0) {
                side = AuthConstant.IMG_FRONT;
            } else {
                side = AuthConstant.IMG_BACK;
            }
            //TODO 身份证信息识别 待完善图片
            Map<String, Object> ocrResult = BaiduAIUtils.ocr(ocrdto.getType(), base64Img, side);
            return Result.success(ocrResult);
        }
        return Result.success();
    }

    @PostMapping("/face")
    @Operation(summary = "人脸认证")
    public Result faceAuth(@RequestBody FaceAuthDTO faceAuthDTO) {
        Boolean result = BaiduAIUtils.faceAuth(faceAuthDTO.getBase64Person(), faceAuthDTO.getBase64IDCard());
        //TODO 人脸识别检验等待商榷
        if (result) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @GetMapping("/certificate")
    @Operation(summary = "获取证书")
    public Result getCertificate() {
        //TODO 证书生成
        String data = "证书";
        return Result.success(data);
    }
    @PutMapping("/comparepaypwd")
    @Operation(summary = "比较交易密码")
    public Result comparePayPwd(
            @RequestParam("id") @Parameter(description = "手机银行账户id") Long id ,
            @RequestParam("paypwd") @Parameter(description = "手机银行账户id")  String payPwd
    ) {
        authService.comparePayPwd(id,payPwd);
        return Result.success();
    }

}
