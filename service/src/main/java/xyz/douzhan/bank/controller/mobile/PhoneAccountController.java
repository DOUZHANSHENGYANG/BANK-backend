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
import xyz.douzhan.bank.result.Result;
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
//@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
public class PhoneAccountController {

    private final PhoneAccountService phoneAccountService;
    @DeleteMapping("destroy")
    @Operation(summary = "注销账户")
    public Result deleteAccount(@Parameter(description = "手机银行账户id")@RequestParam("id") Long id) {
        phoneAccountService.deleteAccount(id);

        return Result.success();
    }
    @PutMapping("/pwd/modify")
    @Operation(summary = "修改密码")
    public Result modifyPassword(@Parameter(description = "密码对象")@RequestBody UpdatePWDDTO updatePWDDTO) {
        boolean isUpdated = phoneAccountService.modifyPassword(updatePWDDTO.getId(),updatePWDDTO.getType(),updatePWDDTO.getPassword(),updatePWDDTO.getOldPassword());
        if (!isUpdated){
            return Result.error();
        }
        return Result.success();
    }
    @PutMapping("/pwd/reset")
    @Operation(summary = "重置密码")
    public Result resetPassword(@RequestBody@Parameter(description = "密码对象") UpdatePWDDTO updatePWDDTO) {
        boolean isUpdated = phoneAccountService.resetPassword(updatePWDDTO.getId(),updatePWDDTO.getType(),updatePWDDTO.getPassword());
        if (!isUpdated){
            return Result.error();
        }
        return Result.success();
    }

    @PutMapping("/pn")
    @Operation(summary = "修改账户手机号")
    public Result updatePhone(
            @RequestParam("id")@Parameter(description = "用户id")Long id,
            @RequestParam("phoneNumber")@Parameter(description = "手机号")String phoneNumber
    ) {
        boolean isUpdated = phoneAccountService.updatePhone(id,phoneNumber);
        if (!isUpdated){
            return Result.error();
        }
        return Result.success();
    }


    @PostMapping("/avatar")
    @Operation(summary = "用户上传头像")
    public Result register(@RequestParam("file")@Parameter(description = "用户头像文件") MultipartFile file) {
        String url=phoneAccountService.upload(file);
        return Result.success(url);

    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result register(@RequestBody RegisterDTO registerDTO) {
        Long accountId = phoneAccountService.register(registerDTO);
        return Result.success(accountId);
    }

    @PostMapping("/login/sms")
    @Operation(summary = "用户手机号登录")
    public Result smsLogin(@RequestBody @Parameter(description = "只接受类型为0 手机号短信登录") LoginDTO loginDTO) {
        String token = phoneAccountService.smsLogin(loginDTO);
        return Result.success().message("登录成功").data(token);
    }
    @PostMapping("/login/up")
    @Operation(summary = "用户账号密码登录")
    public Result usernamePWDLogin(@RequestBody @Parameter(description = "根据类型动态判断登录类型 1为手机号密码登录 2为证件号密码登录 ") LoginDTO loginDTO) {
        String token = phoneAccountService.usernamePWDLogin(loginDTO);
        return Result.success().message("登录成功").data(token);
    }


    @GetMapping("/logout")
    @Operation(summary = "退出登录")
    public Result logout() {
        UserContext.removeContext();
        return Result.success().message("登出成功");
    }
}
