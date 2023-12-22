package xyz.douzhan.bank.controller.bank;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.douzhan.bank.po.UserInfo;
import xyz.douzhan.bank.dto.result.ResponseResult;
import xyz.douzhan.bank.service.UserInfoService;
import xyz.douzhan.bank.utils.CypherUtil;

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
@RequestMapping("/bank/bank/user-info")
@RequiredArgsConstructor
public class UserInfoController {
    private final UserInfoService userInfoService;


    @GetMapping("")
    @Operation(summary = "获取用户信息")
    public ResponseResult getUserInfo() {
        UserInfo userInfo = userInfoService.getInfo();
        if (userInfo==null){
            return ResponseResult.error();
        }
        userInfo.setPhoneNumber(CypherUtil.decrypt(userInfo.getPhoneNumber()));
        userInfo.setDocumentsNum(CypherUtil.decrypt(userInfo.getDocumentsNum()));
        userInfo.setName(CypherUtil.decrypt(userInfo.getName()));
        userInfo.setPinYin(CypherUtil.decrypt(userInfo.getPinYin()));
        return ResponseResult.success(userInfo);
    }


    @PutMapping("")
    @Operation(summary = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody @Parameter(description = "用户信息id") UserInfo userInfo) {
        userInfoService.updateUserInfo(userInfo);
        return ResponseResult.success();
    }
}
