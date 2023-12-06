package xyz.douzhan.bank.controller.common;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.core.lang.UUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import xyz.douzhan.bank.dto.FaceAuthDTO;
import xyz.douzhan.bank.dto.LoginDTO;
import xyz.douzhan.bank.enums.VerifyCodeType;
import xyz.douzhan.bank.result.Result;
import xyz.douzhan.bank.utils.RedisUtils;
import xyz.douzhan.bank.utils.VerifyCodeUtils;
import xyz.douzhan.bank.vo.ImgVerifyCodeVO;

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
@PreAuthorize("hasRole('USER')")
public class AuthController {

    @PostMapping("/face")
    @Operation(summary = "人脸认证")
    public Result faceAuth(@RequestBody @Validated FaceAuthDTO faceAuthDTO){

        //TODO 接入人脸识别
        return Result.success();
    }
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
            @RequestParam(name = "mobile", required = false)
            @Pattern(regexp = "^1[3456789]\\d{9}$", message = "手机号格式不合法")
            @Parameter(name = "mobile", description = "手机号")
            String mobile
    ) {
        //获取验证码
        AbstractCaptcha captcha = VerifyCodeUtils.genVerifyCode(VerifyCodeType.LINE);
        String code = captcha.getCode();

        //类型为短信验证码
        if (VerifyCodeType.SMS.ordinal() == type) {
            RedisUtils.setWithExpire("user:" + "smscode:" + mobile, mobile, exp, TimeUnit.SECONDS);
            //TODO 调用短信服务
            return Result.success().message("短信发送成功");
        }
        //类型为图形验证码
        //生成验证码序号
        String uuid = UUID.randomUUID().toString();
        //将验证码存储到缓存
        RedisUtils.setWithExpire("user:" + "imgcode:" + uuid, code, exp, TimeUnit.SECONDS);
        return Result.success(new ImgVerifyCodeVO().uuid(uuid).imageBase64Data(captcha.getImageBase64Data()));
    }

    @PostMapping("/login/*")
    @Operation(summary = "用户登录",description = "没什么用")
    public Result login(
            @RequestBody(required = false)
            @Parameter(name = "LoginDTO", description = "没什么用的登录实体")
            LoginDTO loginDTO
    ) {
        return Result.success().message("登录成功");
    }

    @GetMapping("/logout")
    @Operation(summary = "用户退出登录",description = "没什么用")
    public Result logout() {
        return Result.success().message("登出成功");
    }

}
