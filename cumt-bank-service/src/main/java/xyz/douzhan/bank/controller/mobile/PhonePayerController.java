package xyz.douzhan.bank.controller.mobile;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.douzhan.bank.context.UserContext;
import xyz.douzhan.bank.dto.result.ResponseResult;
import xyz.douzhan.bank.po.PhonePayer;
import xyz.douzhan.bank.service.PhonePayerService;
import xyz.douzhan.bank.utils.CypherUtil;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Tag(name="转账人信息")
@RestController
@RequestMapping("/bank/mobile/phone-payer")
@RequiredArgsConstructor
public class PhonePayerController {
    private final PhonePayerService phonePayerService;
    @PostMapping("")
    @Operation(summary = "新增")
    public ResponseResult save(@RequestBody @Parameter(description = "装账人实体") PhonePayer phonePayer) {
        phonePayer.setPhoneAccountId(UserContext.getContext());
        phonePayer.setBankcardNum(CypherUtil.encrypt(phonePayer.getBankcardNum()));
        phonePayer.setName(CypherUtil.encrypt(phonePayer.getName()));
        phonePayerService.save(phonePayer);
        return ResponseResult.success();
    }
    @DeleteMapping("")
    @Operation(summary = "删除")
    public ResponseResult remove(@RequestParam("id")  @Parameter(description = "转账人实体id") Long id ) {
        phonePayerService.removeById(id);
        return ResponseResult.success();
    }
    @GetMapping("")
    @Operation(summary = "查询")
    public ResponseResult get() {
        List<PhonePayer> phonePayers = phonePayerService.list(Wrappers.lambdaQuery(PhonePayer.class).eq(PhonePayer::getPhoneAccountId, UserContext.getContext()));
        if (!CollUtil.isEmpty(phonePayers)){
            List<PhonePayer> list = phonePayers.stream().map(phonePayer -> {
                phonePayer.setName(CypherUtil.decrypt(phonePayer.getName()));
                phonePayer.setName(CypherUtil.decrypt(phonePayer.getBankcardNum()));
                return phonePayer;}).toList();
            return ResponseResult.success(list);
        }
        return ResponseResult.success(phonePayers);
    }
    @PutMapping("")
    @Operation(summary = "修改")
    public ResponseResult update(@RequestBody @Parameter(description = "转账人实体") PhonePayer phonePayer) {
        phonePayerService.update(phonePayer);
        return ResponseResult.success();
    }

}
