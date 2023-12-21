package xyz.douzhan.bank.controller.bank;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.douzhan.bank.dto.TransactionDetailsDTO;
import xyz.douzhan.bank.dto.result.PageResponseResult;
import xyz.douzhan.bank.po.TransactionHistory;
import xyz.douzhan.bank.dto.result.ResponseResult;
import xyz.douzhan.bank.service.TransactionHistoryService;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-11
 */
@Tag(name="交易记录")
@RestController
@RequestMapping("/bank/bank/history")
@RequiredArgsConstructor
public class TransactionHistoryController {
    private final TransactionHistoryService transactionHistoryService;
    @PostMapping("/details")
    @Operation(summary = "获取交易明细")
    public PageResponseResult getTransactionDetails(@RequestBody@Parameter(description = "交易明细DTO") TransactionDetailsDTO detailsDTO) {
        return transactionHistoryService.getTransactionDetails(detailsDTO);

    }

}
