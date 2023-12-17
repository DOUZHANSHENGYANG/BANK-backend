package xyz.douzhan.bank.controller.bank;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.douzhan.bank.context.UserContext;
import xyz.douzhan.bank.dto.result.ResponseResult;
import xyz.douzhan.bank.po.Bankcard;
import xyz.douzhan.bank.service.BankCardService;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Tag(name = "银行账户")
@RestController
@RequestMapping("/bank/bank/bankcard")
@RequiredArgsConstructor
public class BankCardController {
    private final BankCardService bankCardService;

    @PutMapping("/status")
    @Operation(summary = "修改账户状态")
    public ResponseResult getAccountNumber(
            @RequestParam("accountId") @Parameter(description = "银行卡账户id") Long bankcardId,
            @RequestParam("status") @Parameter(description = "状态类型 0开户1正常2挂失3冻结4注销5休眠") Integer status
    ) {
        bankCardService.updateStatus(bankcardId, status);
        return ResponseResult.success();
    }

    @PostMapping("/create")
    @Operation(summary = "开通银行卡账户")
    public ResponseResult createAccount(@RequestBody @Parameter(description = "银行卡账户实体") Bankcard bankcard) {
        bankCardService.createBankcardAccount(bankcard, UserContext.getContext());
        return ResponseResult.success();
    }

//    @GetMapping("/number")
//    @Operation(summary = "根据手机账户id查询卡号和手机号")
//    public ResponseResult getCardNumAndPhoneNumber(
//            @RequestParam("type") @Parameter(description = "类型 0全查 1为查I类 2为查II类 3为查III类") Integer type
//    ) {
//        JSONObject result = bankCardService.getCardNumAndPhoneNumber(UserContext.getContext(), type);
//        return ResponseResult.success(result);
//    }

    @GetMapping("/number")
    @Operation(summary = "根据银行卡id查询完整卡号")
    public ResponseResult getBankcardNumber(
            @RequestParam("bankcardId") @Parameter(description = "银行卡id") Long bankcardId
    ) {
        return bankCardService.getBankcardNumber(bankcardId);
    }


    @GetMapping("/first")
    @Operation(summary = "根据手机号查询查询I类账户id")
    public ResponseResult getFirstAccount(
            @RequestParam("phoneNumber") @Parameter(description = "手机号") String phoneNumber
    ) {
        return bankCardService.getFirstAccount(phoneNumber);
    }

    @GetMapping("/info")
    @Operation(summary = "根据银行卡id集合查询账户信息")
    public ResponseResult getAccountInfoByIds(
            @RequestParam("bankcardIds") @Parameter(description = "银行卡id集合") List<Long> bankcardIds) {
        return ResponseResult.success(bankCardService.getByIds(bankcardIds));
    }
}
