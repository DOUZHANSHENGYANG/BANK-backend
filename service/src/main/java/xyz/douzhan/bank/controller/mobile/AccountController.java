package xyz.douzhan.bank.controller.mobile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.douzhan.bank.po.Account;
import xyz.douzhan.bank.result.Result;
import xyz.douzhan.bank.service.AccountService;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Tag(name = "银行账户")
@RestController
@RequestMapping("/bank/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/has")
    @Operation(summary = "根据手机号判断是否存在I类账户")
    public Result getHasAccount(
            @RequestParam("phoneNumber") @Parameter(description = "手机号")String phoneNumber
    ){
        Account account = accountService.lambdaQuery().eq(Account::getPhoneNumber, phoneNumber).one();
        if (account==null){
            return Result.error().message("未曾在本行开户，请前往线下办理卡户");
        }
        return Result.success();
    }
}
