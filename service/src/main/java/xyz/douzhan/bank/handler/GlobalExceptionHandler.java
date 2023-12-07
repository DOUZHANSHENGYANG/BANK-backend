package xyz.douzhan.bank.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.douzhan.bank.exception.ThirdPartyAPIException;
import xyz.douzhan.bank.result.Result;

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
    @ExceptionHandler(ThirdPartyAPIException.class)
    public Result thirdPartyAPIExceptionHandler(ThirdPartyAPIException e){
        log.error("发生了第三方接口调用异常：信息为{}",e.getMessage());
        return Result.error().message(e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result otherExceptionHandler(Exception e){
        log.error("发生了其他不可预知异常：信息为{}",e.getMessage());
        return Result.error().message("服务器异常");
    }
}
