package xyz.douzhan.bank.controller.mobile;

import com.alibaba.fastjson2.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.douzhan.bank.constants.BankConstant;
import xyz.douzhan.bank.po.Account;
import xyz.douzhan.bank.result.Result;
import xyz.douzhan.bank.service.AccountService;
import xyz.douzhan.bank.vo.AccountVO;

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

    @PutMapping("/status")
    @Operation(summary = "修改账户状态")
    public Result getAccountNumber(
            @RequestParam("accountId")@Parameter(description = "账户id") Long accountId,
            @RequestParam("status")@Parameter(description = "状态类型 0开户1正常2挂失3冻结4注销5休眠") Integer status
    ){
        accountService.updateStatus(accountId,status);
        return Result.success();
    }

    @PostMapping("/create")
    @Operation(summary = "开通银行账户")
    public Result getAccountNumber(
            @RequestBody@Parameter(description = "账户实体") Account account,
            @RequestParam("phoneAccountId")@Parameter(description = "手机账户id") Long phoneAccountId
    ){
        accountService.createAccount(account,phoneAccountId);
        return Result.success();
    }

    @GetMapping("/number")
    @Operation(summary = "根据手机账户id查询账户卡号和手机号")
    public Result createAccount(
            @RequestParam("id") @Parameter(description = "手机账户id")Long id,
            @RequestParam("type") @Parameter(description = "类型 0全查 1为查I类 2为查II类 3为查III类")Integer type
    ){
        JSONObject result= accountService.getAccountNumber(id,type);
        return Result.success(result);

    }
    @GetMapping("/hasfirst")
    @Operation(summary = "根据手机号查询是否有I类账户")
    public Result getHasAccount(
            @RequestParam("phoneNumber") @Parameter(description = "手机号")String phoneNumber
    ){
        Account account = accountService.lambdaQuery().eq(Account::getPhoneNumber, phoneNumber).one();
        if (account==null){
            return Result.error().message(BankConstant.REFUSE_TO_REGISTER_A_RESPONSE);
        }
        return Result.success();
    }
    @GetMapping("/info")
    @Operation(summary = "获取账户信息")
    public Result getAccountInfo(@RequestParam("id") @Parameter(description = "银行账户id")Long id){
        AccountVO accountVO =accountService.getAccountInfo(id);
        return Result.success(accountVO);
    }
}
