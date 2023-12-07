package xyz.douzhan.bank.controller.mobile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.douzhan.bank.result.Result;
import xyz.douzhan.bank.service.BankCardService;
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
@Tag(name = "银行账户")
@RestController
@RequestMapping("/bank/bankcard")
@RequiredArgsConstructor
//@PreAuthorize("hasRole('USER')")
public class BankCardController {
    private final BankCardService bankCardService;
    @GetMapping
    @Operation(summary = "根据手机号查询银行卡")
    public Result getInfo(
            @RequestParam("phoneNumber")@Parameter(description = "手机号")
            String phoneNumber,
            @RequestParam(value = "type",required = false,defaultValue = "0")
            @Parameter(description = "类型 0为全部查 1为只查本行手机号绑定卡 默认为0")
            Integer type
    ){
        List<BankCardVO> bankCardVOList= bankCardService.getByPhoneNumber(phoneNumber,type);
        return Result.success(bankCardVOList);
    }
}
