package xyz.douzhan.bank.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.douzhan.bank.dto.RegisterDTO;
import xyz.douzhan.bank.result.Result;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@RestController
@RequestMapping("/mobile/account")
@PreAuthorize("hasRole('USER')")
public class PhoneAccountController {

    @PostMapping("/register")
    public Result register(@RequestBody RegisterDTO registerDTO){
        return Result.success();
    }
}
