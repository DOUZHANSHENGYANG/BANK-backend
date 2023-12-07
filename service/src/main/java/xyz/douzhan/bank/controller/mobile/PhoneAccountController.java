package xyz.douzhan.bank.controller.mobile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import xyz.douzhan.bank.dto.LoginDTO;
import xyz.douzhan.bank.dto.RegisterDTO;
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
@RestController
@RequestMapping("/bank/mobile/account")
//@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
@Tag(name = "手机银行账户")
public class PhoneAccountController {

    private final PhoneAccountService phoneAccountService;

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result register(@RequestBody RegisterDTO registerDTO) {
        Long accountId = phoneAccountService.register(registerDTO);
        return Result.success(accountId);
    }

    @PostMapping("/login/*")
    @Operation(summary = "用户登录")
    public Result login(@RequestBody(required = false) @Parameter(description = "根据类型动态判断登录类型") LoginDTO loginDTO) {
        return Result.success().message("登录成功");
    }

    @GetMapping("/logout")
    @Operation(summary = "退出登录", description = "没什么用")
    public Result logout() {
        return Result.success().message("登出成功");
    }
}
