package xyz.douzhan.bank.controller.mobile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.douzhan.bank.context.UserContext;
import xyz.douzhan.bank.dto.result.ResponseResult;
import xyz.douzhan.bank.po.PhoneFeedback;
import xyz.douzhan.bank.service.PhoneFeedbackService;

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
@RequiredArgsConstructor
public class PhoneFeedbackController {
    private final PhoneFeedbackService phoneFeedbackService;
    @PostMapping("")
    @Operation(summary = "提交反馈信息")
    public ResponseResult submit(@RequestBody@Parameter(description = "反馈实体") PhoneFeedback feedback) {
        feedback.setUserId(UserContext.getContext());
        phoneFeedbackService.save(feedback);
        return ResponseResult.success();
    }
}
