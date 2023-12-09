package xyz.douzhan.bank.controller.mobile;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Tag(name="转账交易")
@RestController
@RequestMapping("/bank/transaction")
//@PreAuthorize("hasRole('USER')")
public class TransactionController {

}
