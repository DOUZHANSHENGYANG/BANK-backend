package xyz.douzhan.bank.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.douzhan.bank.dto.result.ResponseResult;
import xyz.douzhan.bank.service.BankPhoneBankRefService;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Tag(name = "银行手机关联")
@RestController
@RequestMapping("/bank/mobile/ref")
@RequiredArgsConstructor
public class BankPhoneBankRefController {
    private final BankPhoneBankRefService bankPhoneBankRefService;

    @GetMapping
    @Operation(summary = "查询已绑定的银行卡账户id")
    public ResponseResult getBankcardIdByPhoneAccountId() {
        List<Long> bankcardIds = bankPhoneBankRefService.getBankcardIds();
        return ResponseResult.success(bankcardIds);
    }

    @PutMapping("/compare")
    @Operation(summary = "比较手机银行交易密码")
    public ResponseResult comparePayPwd(
            @RequestParam("phoneAccountId") @Parameter(description = "手机银行账户id") Long phoneAccountId,
            @RequestParam("paypwd") @Parameter(description = "手机交易密码") String payPwd
    ) {
        bankPhoneBankRefService.comparePayPwd(phoneAccountId, payPwd);
        return ResponseResult.success();
    }

    @GetMapping("/asset")
    @Operation(summary = "查询我的总资产")
    public ResponseResult getAsset(){
        Long asset = bankPhoneBankRefService.getAsset();
        return ResponseResult.success(asset);
    }


    @PutMapping ("/bind")
    @Operation(summary = "绑定默认账户")
    public ResponseResult bindDefaultCard(
            @RequestParam("oldBankcardId")@Parameter(description = "旧绑定银行卡账户id") Long oldBankcardId,
            @RequestParam("newBankcardId")@Parameter(description = "新银行卡账户id") Long newBankcardId
    ){
        bankPhoneBankRefService.bindDefaultCard(oldBankcardId,newBankcardId);
        return ResponseResult.success();
    }

}
