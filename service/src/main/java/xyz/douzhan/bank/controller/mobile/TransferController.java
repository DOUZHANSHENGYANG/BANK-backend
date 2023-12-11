package xyz.douzhan.bank.controller.mobile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.douzhan.bank.dto.TransactionDTO;
import xyz.douzhan.bank.result.Result;
import xyz.douzhan.bank.service.TransferService;
import xyz.douzhan.bank.vo.SupportBankVO;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Tag(name = "转账交易")
@RestController
@RequestMapping("/bank/transfer")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping("/create")
    @Operation(summary = "创建转账订单")
    public Result createTransfer(@RequestBody @Parameter(description = "转账DTO") TransactionDTO transactionDTO) {
        Long transferId = transferService.createTransfer(transactionDTO.getTransfer(), transactionDTO.getPhoneAccountId());
        return Result.success(transferId);
    }
//        @PostMapping
//        @Operation(summary = "后置转账",description = "转账分两步，这是第二步，校验验证码，状态为成功，转账才结束")
//        public Result transfer(@RequestBody@Parameter(description = "短信验证码校验")PostTransactionDTO postTransactionDTO) {
//        transferService.postTransfer(postTransactionDTO.getSmsCode(),postTransactionDTO.getPhoneNumber(),postTransactionDTO.getTransactionId());
//        return Result.success();
//    }


    @GetMapping("/bank")
    @Operation(summary = "获取支持的转账银行")
    public Result getSupportBank() {
        List<SupportBankVO> supportBankVOS = transferService.getSupportBank();
        return Result.success(supportBankVOS);
    }

    @PutMapping("/complete")
    @Operation(summary = "完成订单")
    public Result complete(
            @RequestParam("id") @Parameter(description = "转账订单id") Long id,
            @RequestParam("status") @Parameter(description = "状态 1成功结束 2异常结束") Integer status,
            @RequestParam("money") @Parameter(description = "钱") Double money
    ) {
        transferService.complete(id,status,money);
        return Result.success();
    }

}
