package xyz.douzhan.bank.result;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 一些声明信息
 * Description:
 * date: 2023/11/25 22:42
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
@Data
@Accessors(fluent = true,chain = true)
@Schema(description = "统一返回结果")
public class Result<T>{
    @Schema(description = "状态码")
    private Integer code;
    @Schema(description = "消息")
    private String message;
    @Schema(description = "数据")
    private T data;

    public static <T> Result success(){
        Result<T> result = new Result<>();
        result.code=200;
        result.message="";
        result.data=null;
        return result;
    }

    public static <T> Result success(T data){
        Result<T> result = new Result<>();
        result.code=200;
        result.message="";
        result.data=data;
        return result;
    }
    public static <T> Result error(){
        Result<T> result = new Result<>();
        result.code=400;
        result.message="";
        result.data=null;
        return result;
    }
    public static <T> Result error(T data){
        Result<T> result = new Result<>();
        result.code=400;
        result.message="";
        result.data=data;
        return result;
    }
}
