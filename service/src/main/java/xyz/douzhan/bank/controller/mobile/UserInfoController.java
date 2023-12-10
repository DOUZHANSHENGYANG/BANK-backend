package xyz.douzhan.bank.controller.mobile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.douzhan.bank.po.UserInfo;
import xyz.douzhan.bank.result.Result;
import xyz.douzhan.bank.service.UserInfoService;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Tag(name="用户信息")
@RestController
@RequestMapping("/bank/user")
@RequiredArgsConstructor
public class UserInfoController {
    private final UserInfoService userInfoService;


    @GetMapping("/info")
    @Operation(summary = "获取用户信息")
    public Result getUserInfo(@RequestParam("id")@Parameter(description = "手机号账户id") Long id) {
        UserInfo userInfo = userInfoService.getInfo(id);
        if (userInfo==null){
            return Result.error();
        }
        return Result.success(userInfo);
    }


    @PutMapping("/info")
    @Operation(summary = "更新用户信息")
    public Result updateUserInfo(@RequestBody @Parameter(description = "用户信息id") UserInfo userInfo) {
        boolean isUpdated = userInfoService.updateById(userInfo);
          if (!isUpdated){
            return Result.error();
        }
        return Result.success();
    }
}
