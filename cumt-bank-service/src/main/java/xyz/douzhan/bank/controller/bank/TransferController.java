package xyz.douzhan.bank.controller.bank;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.douzhan.bank.dto.DigitalReceiptDTO;
import xyz.douzhan.bank.dto.result.PageResponseResult;
import xyz.douzhan.bank.dto.result.ResponseResult;
import xyz.douzhan.bank.po.Transfer;
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
@RequestMapping("/bank/bank/transfer")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping("/create")
    @Operation(summary = "创建转账订单")
    public ResponseResult createOrder(@RequestBody @Parameter(description = "转账实体") Transfer transfer) {
        Long orderId = transferService.createOrder(transfer);
        return ResponseResult.success(orderId);
    }


    @GetMapping("/bank")
    @Operation(summary = "获取支持的转账银行")
    public ResponseResult getSupportBank() {
        List<SupportBankVO> supportBankVOS = transferService.getSupportBank();
        return ResponseResult.success(supportBankVOS);
    }

    @PutMapping("/complete")
    @Operation(summary = "完成订单")
    public ResponseResult completeOrder(
            @RequestParam("orderId") @Parameter(description = "转账订单id") Long orderId,
            @RequestParam("status") @Parameter(description = "状态 1成功结束 2异常结束") Integer status,
            @RequestParam("money") @Parameter(description = "钱钱") Long money
    ) {
        transferService.completeOrder(orderId,status, money);
        return ResponseResult.success();
    }

    @GetMapping("/receipts")
    @Operation(summary = "获取电子回单")
    public PageResponseResult getHistoryRecords(@RequestBody@Parameter(description = "电子回单DTO") DigitalReceiptDTO receiptDTO) {
        return transferService.getHistoryRecord(receiptDTO);
    }

    @PostMapping("/receipts")
    @Operation(summary = "验证电子回单")
    public ResponseResult validateHistoryRecord(
            @RequestParam("orderNum")@Parameter(description = "回单号") String orderNum,
            @RequestParam("transfereeName")@Parameter(description = "收款人姓名") String name
    ) {
        Boolean isTrue = transferService.validateHistoryRecord(orderNum, name);
        if (isTrue){
            return ResponseResult.success();
        }
        return ResponseResult.error();
    }

}
