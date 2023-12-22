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

    @GetMapping("/status")
    @Operation(summary = "查询订单状态)")
    public ResponseResult queryOrderStatus(@RequestParam("orderId") @Parameter(description = "转账订单id") Long orderId){
        Integer status = transferService.queryOrderStatus(orderId);
        return ResponseResult.success(status);
    }

    @PostMapping("/create")
    @Operation(summary = "创建转账订单")
    public ResponseResult createOrder(@RequestBody @Parameter(description = "转账实体") Transfer transfer) {
        Long orderId = transferService.createOrder(transfer);
        return ResponseResult.success(orderId);
    }



    @PutMapping("/complete")
    @Operation(summary = "完成转账订单")
    public ResponseResult completeOrder(
            @RequestParam("orderId") @Parameter(description = "转账订单id") Long orderId,
            @RequestParam("status") @Parameter(description = "状态 1成功结束 2异常结束") Integer status
    ) {
        transferService.completeOrder(orderId,status);
        return ResponseResult.success();
    }


//    @PostMapping("/qrcode/create")
//    @Operation(summary = "创建二维码转账订单")
//    public ResponseResult createQrcodeOrder(@RequestBody @Parameter(description = "转账实体") Transfer transfer
//            ,@RequestParam("uuid")@Parameter(description = "转账二维码唯一标识") String uuid) {
//        Long orderId = transferService.createOrder(transfer,uuid);
//        return ResponseResult.success(orderId);
//    }

//    @GetMapping("/qrcode/credit")
//    @Operation(summary = "获取收款码 一分钟一次")
//    public ResponseResult getCreditCode(@RequestParam("bankcardId") @Parameter(description = "银行卡id") Long bankcardId){
//        transferService.getCreditCode(bankcardId);
//        return ResponseResult.success();
//    }
//    @GetMapping("/qrcode/remit")
//    @Operation(summary = "获取付款码 一分钟一次")
//    public ResponseResult getRemitCode(@RequestParam("bankcardId") @Parameter(description = "银行卡id") Long bankcardId){
//        transferService.getRemitCode(bankcardId);
//        return ResponseResult.success();
//    }
//
//
//    @GetMapping("/qrcode/complete")
//    @Operation(summary = "二维码付款码主扫方关闭订单")
//    public ResponseResult completeQRCodeOrder(@RequestBody Transfer transfer){
//        transferService.completeQRCodeOrder(transfer);
//        return ResponseResult.success();
//    }
//
//    @PutMapping("/qrcode/status")
//    @Operation(summary = "二维码付款码被扫方设置订单状态")
//    public ResponseResult setQRCodeOrderStatus(@RequestBody Transfer transfer){
//        transferService.setQRCodeOrderStatus(transfer);
//        return ResponseResult.success();
//    }
//    @PostMapping("/get/receipts")
//    @Operation(summary = "获取电子回单")
//    public PageResponseResult getHistoryRecords(@RequestBody@Parameter(description = "电子回单DTO") DigitalReceiptDTO receiptDTO) {
//        return transferService.getHistoryRecord(receiptDTO);
//    }
//
//    @PostMapping("/validate/receipts")
//    @Operation(summary = "验证电子回单")
//    public ResponseResult validateHistoryRecord(
//            @RequestParam("orderNum")@Parameter(description = "回单号") String orderNum,
//            @RequestParam("transfereeName")@Parameter(description = "收款人姓名") String name
//    ) {
//        Boolean isTrue = transferService.validateHistoryRecord(orderNum, name);
//        if (isTrue){
//            return ResponseResult.success();
//        }
//        return ResponseResult.error();
//    }

    @GetMapping("/bank")
    @Operation(summary = "获取支持的转账银行")
    public ResponseResult getSupportBank() {
        List<SupportBankVO> supportBankVOS = transferService.getSupportBank();
        return ResponseResult.success(supportBankVOS);
    }
}
