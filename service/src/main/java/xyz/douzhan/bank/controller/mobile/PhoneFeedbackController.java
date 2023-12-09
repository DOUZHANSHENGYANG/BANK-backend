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
@Tag(name="反馈信息")
@RestController
@RequestMapping("/bank/mobile/phone-feedback")
//@PreAuthorize("hasRole('USER')")
public class PhoneFeedbackController {

}
