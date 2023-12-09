package xyz.douzhan.bank.controller.mobile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Tags;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.douzhan.bank.po.Account;
import xyz.douzhan.bank.result.Result;
import xyz.douzhan.bank.service.BankPhoneBankRefService;
import xyz.douzhan.bank.vo.AccountVO;
import xyz.douzhan.bank.vo.BankCardVO;

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
    @GetMapping("/asset")
    @Operation(summary = "查询我的总资产")
    public Result getAsset(@RequestParam("id")@Parameter(description = "手机账户id")Long id){
        String asset=bankPhoneBankRefService.getAsset(id);
        return Result.success(asset);
    }
    @GetMapping("/account")
    @Operation(summary = "查询已关联的账户")
    public Result getAccount(@RequestParam("id")@Parameter(description = "手机账户id")Long id){
        List<AccountVO> asset=bankPhoneBankRefService.getAccount(id);
        return Result.success(asset);
    }

}
