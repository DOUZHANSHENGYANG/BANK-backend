package xyz.douzhan.bank.dto.result;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 一些声明信息
 * Description:
 * date: 2023/11/25 22:42
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Accessors(fluent = true, chain = true)
@Schema(description = "统一返回结果")
public class ResponseResult<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "状态码")
    private Integer code;
    @Schema(description = "消息")
    private String message;
    @Schema(description = "数据")
    private T data;

    private ResponseResult(){}
    public static <T> ResponseResult success() {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.code = 200;
        responseResult.message = "";
        responseResult.data = null;
        return responseResult;
    }

    public static <T> ResponseResult success(T data) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.code = 200;
        responseResult.message = "";
        responseResult.data = data;
        return responseResult;
    }

    public static <T> ResponseResult error() {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.code = 400;
        responseResult.message = "";
        responseResult.data = null;
        return responseResult;
    }

    public static <T> ResponseResult error(T data) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.code = 400;
        responseResult.message = "";
        responseResult.data = data;
        return responseResult;
    }
}
