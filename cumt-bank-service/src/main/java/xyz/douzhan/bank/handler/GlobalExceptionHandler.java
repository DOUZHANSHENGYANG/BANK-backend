package xyz.douzhan.bank.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.douzhan.bank.exception.AuthenticationException;
import xyz.douzhan.bank.exception.BizException;
import xyz.douzhan.bank.exception.ThirdPartyAPIException;
import xyz.douzhan.bank.dto.result.ResponseResult;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/7 19:10
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ResponseResult bizExceptionHandler(BizException e) {
        log.error("发生了业务：信息为{}", e.getMessage());
        return ResponseResult.error().message(e.getMessage()).code(400);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseResult authenticationExceptionHandler(AuthenticationException e) {
        log.error("发生了认证异常：信息为{}", e.getMessage());
        return ResponseResult.error().message(e.getMessage()).code(403);
    }

    @ExceptionHandler(ThirdPartyAPIException.class)
    public ResponseResult thirdPartyAPIExceptionHandler(ThirdPartyAPIException e) {
        log.error("发生了第三方接口调用异常：信息为{}", e.getMessage());
        return ResponseResult.error().message(e.getMessage()).code(400);
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult otherExceptionHandler(Exception e) {
        log.error("发生了其他不可预知异常：信息为{}", e.getMessage());
        return ResponseResult.error().message("服务器异常").code(500);
    }
}
