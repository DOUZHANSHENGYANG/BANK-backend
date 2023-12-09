package xyz.douzhan.bank.controller.common;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.douzhan.bank.dto.FaceAuthDTO;
import xyz.douzhan.bank.dto.OCRDTO;
import xyz.douzhan.bank.dto.ValidateVerifyCodeDTO;
import xyz.douzhan.bank.enums.VerifyCodeType;
import xyz.douzhan.bank.exception.ThirdPartyAPIException;
import xyz.douzhan.bank.result.Result;
import xyz.douzhan.bank.utils.AliYunUtils;
import xyz.douzhan.bank.utils.BaiduAIUtils;
import xyz.douzhan.bank.utils.RedisUtils;
import xyz.douzhan.bank.utils.VerifyCodeUtils;
import xyz.douzhan.bank.vo.ImgVerifyCodeVO;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
@Validated
@ApiResponse
//@PreAuthorize("hasRole('USER')")
public class AuthController {

    @GetMapping("/vc")
    @Operation(summary = "根据类型获取验证码")
    public Result getVerifyCode(
            @RequestParam(name = "type")
            @Min(value = 0, message = "类型数值错误") @Max(value = 1, message = "类型数值错误")
            @Parameter(name = "type", description = "0为图形验证码，1为短信验证码")
            Integer type,
            @RequestParam(name = "exp", required = false, defaultValue = "60")
            @Min(value = 1, message = "过期时间不合法")
            @Parameter(name = "exp", description = "过期时间,默认为60s") Integer exp,
            @RequestParam(name = "phoneNumber", required = false)
            @Pattern(regexp = "^1[3456789]\\d{9}$", message = "手机号格式不合法")
            @Parameter(name = "phoneNumber", description = "手机号")
            String phoneNumber
    ) {
        //获取验证码
        AbstractCaptcha captcha = VerifyCodeUtils.genVerifyCode(VerifyCodeType.PURENUM);
        String code = captcha.getCode();

        //类型为短信验证码
        if (VerifyCodeType.SMS.ordinal() == type) {
            if (phoneNumber==null){
                return Result.error();
            }
            RedisUtils.setWithExpire("user:" + "smscode:" + phoneNumber, code, exp, TimeUnit.SECONDS);
            //TODO  发送短信 不只支持数字
            Boolean isSuccess=false;
            try{
                 isSuccess= AliYunUtils.sendSM(code);
            }catch (Exception e){
                throw new ThirdPartyAPIException(e.getMessage());
            }
           if (isSuccess){
               return Result.success().message("短信发送成功");
           }
            return Result.error().message("短信发送失败");
        }
        //类型为图形验证码
        //生成验证码序号
        String uuid = UUID.randomUUID().toString();
        //将验证码存储到缓存
        RedisUtils.setWithExpire("user:" + "imgcode:" + uuid, code, exp, TimeUnit.SECONDS);
        return Result.success(new ImgVerifyCodeVO().uuid(uuid).imageBase64Data(captcha.getImageBase64Data()));
    }

    @PostMapping("/vc")
    @Operation(summary = "验证码校验")
    public Result validateVerifyCode(@RequestBody @Parameter(description = "ValidateVerifyCodeDTO") ValidateVerifyCodeDTO verifyCodeDTO){
        Object code=null;
        //图形验证码
        if (verifyCodeDTO.getType()==VerifyCodeType.PIC.ordinal()){
             code= RedisUtils.get("user:imgcode:" + verifyCodeDTO.getUuid());
            RedisUtils.del("user:imgcode:" + verifyCodeDTO.getUuid());
            if (code==null){
                return Result.error().message("验证码已过期，请重新获取");
            }
            if (!StrUtil.equals(verifyCodeDTO.getImgCode(), code.toString(),false)){
                return Result.error().message("验证码错误");
            }
            return Result.success();

        }
        //短信验证码
        code = RedisUtils.get("user:smscode:" + verifyCodeDTO.getPhoneNumber());
        RedisUtils.del("user:smscode:" + verifyCodeDTO.getPhoneNumber());
        if (code==null){
            return Result.error().message("验证码已过期，请重新获取");
        }
        if (!StrUtil.equals(verifyCodeDTO.getSmsCode(), code.toString(),false)){
            return Result.error().message("验证码错误");
        }
        return Result.success();
    }

    @PostMapping("/ocr")
    @Operation(summary = "图像识别")
    public Result validateVerifyCode(
            @RequestBody @Parameter(description = "OCR实体") OCRDTO ocrdto,
            @RequestParam("file")@Parameter(description = "图片文件") MultipartFile file
    ) throws IOException {
        //将图片文件编码成base64字符串
        String base64Img = Base64.encode(file.getInputStream());
        if (ocrdto.getType()==0){
                String side="";
                if (ocrdto.getSide()==0){
                    side="front";
                }else {
                    side="back";
                }
                //TODO 身份证信息识别 待完善图片
                Map<String, Object> ocrResult = BaiduAIUtils.ocr(ocrdto.getType(), base64Img, side);
                return Result.success(ocrResult);
            }
            return Result.success();
    }
    @PostMapping("/face")
    @Operation(summary = "人脸认证")
    public Result faceAuth(@RequestBody FaceAuthDTO faceAuthDTO){
        Boolean result = BaiduAIUtils.faceAuth(faceAuthDTO.getBase64Person(), faceAuthDTO.getBase64IDCard());
        //TODO 人脸识别检验等待商榷
        if (result){
            return Result.success();
        }else {
            return Result.error();
        }
    }

    @GetMapping("/certificate")
    @Operation(summary = "获取证书")
    public Result getCertificate(){
        //TODO 证书生成
        String data="证书";
      return Result.success(data);
    }



}
