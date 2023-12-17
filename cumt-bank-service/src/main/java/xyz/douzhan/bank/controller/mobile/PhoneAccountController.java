package xyz.douzhan.bank.controller.mobile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.douzhan.bank.context.UserContext;
import xyz.douzhan.bank.dto.LoginDTO;
import xyz.douzhan.bank.dto.RegisterDTO;
import xyz.douzhan.bank.dto.UpdatePWDDTO;
import xyz.douzhan.bank.dto.result.ResponseResult;
import xyz.douzhan.bank.service.PhoneAccountService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Tag(name = "手机银行账户")
@RestController
@RequestMapping("/bank/mobile/account")
@RequiredArgsConstructor
public class PhoneAccountController {

    private final PhoneAccountService phoneAccountService;

    @DeleteMapping("/destroy")
    @Operation(summary = "注销账户")
    public ResponseResult deleteAccount(@Parameter(description = "手机银行账户id") @RequestParam("phoneAccountId") Long phoneAccountId) {
        phoneAccountService.deleteAccount(phoneAccountId);
        return ResponseResult.success();
    }

    @PutMapping("/pwd/modify")
    @Operation(summary = "修改密码")
    public ResponseResult modifyPassword(@Parameter(description = "密码对象") @RequestBody UpdatePWDDTO updatePWDDTO) {
        return phoneAccountService.modifyPassword(updatePWDDTO.getPhoneAccountId(), updatePWDDTO.getType(), updatePWDDTO.getPassword(), updatePWDDTO.getOldPassword());
    }

    @PutMapping("/pwd/reset")
    @Operation(summary = "重置密码")
    public ResponseResult resetPassword(@RequestBody @Parameter(description = "密码对象") UpdatePWDDTO updatePWDDTO) {
        return phoneAccountService.resetPassword(updatePWDDTO.getPhoneAccountId(), updatePWDDTO.getType(), updatePWDDTO.getPassword());
    }

    @PutMapping("/phone-number")
    @Operation(summary = "修改账户手机号")
    public ResponseResult updatePhone(
            @RequestParam("phoneAccountId") @Parameter(description = "手机银行账户id") Long phoneAccountId,
            @RequestParam("phoneNumber") @Parameter(description = "手机号") String phoneNumber
    ) {
        return phoneAccountService.updatePhone(phoneAccountId, phoneNumber);
    }


    @PostMapping("/avatar")
    @Operation(summary = "用户上传头像")
    public ResponseResult register(@RequestParam("file") @Parameter(description = "用户头像文件") MultipartFile file) {
        String url = phoneAccountService.upload(file);
        return ResponseResult.success(url);
    }

    @PostMapping("/register")
    @Operation(summary = "注册")
    public ResponseResult register(@RequestBody RegisterDTO registerDTO) {
        String token = phoneAccountService.register(registerDTO);
        return ResponseResult.success(token);
    }

    @PostMapping("/login/sms")
    @Operation(summary = "手机号登录")
    public ResponseResult smsLogin(@RequestBody @Parameter(description = "类型0 手机号短信登录") LoginDTO loginDTO) {
        String token = phoneAccountService.smsLogin(loginDTO);
        return ResponseResult.success(token);
    }

    @PostMapping("/login/up")
    @Operation(summary = "账号密码登录")
    public ResponseResult usernamePWDLogin(@RequestBody @Parameter(description = "类型 1为手机号密码登录 2为证件号密码登录 ") LoginDTO loginDTO) {
        String token = phoneAccountService.usernamePWDLogin(loginDTO);
        return ResponseResult.success(token);
    }


    @GetMapping("/logout")
    @Operation(summary = "退出登录")
    public ResponseResult logout() {
        UserContext.removeContext();
        return ResponseResult.success();
    }
}
