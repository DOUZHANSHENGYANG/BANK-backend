package xyz.douzhan.bank.result;

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
public class Result<T>{
    private Integer code;
    private String message;
    private T data;

    public static <T> Result success(){
        Result<T> result = new Result<>();
        result.code=200;
        result.message=null;
        result.data=null;
        return result;
    }

    public static <T> Result success(T data){
        Result<T> result = new Result<>();
        result.code=200;
        result.message=null;
        result.data=data;
        return result;
    }
    public static <T> Result error(){
        Result<T> result = new Result<>();
        result.code=400;
        result.message=null;
        result.data=null;
        return result;
    }
    public static <T> Result error(T data){
        Result<T> result = new Result<>();
        result.code=400;
        result.message=null;
        result.data=data;
        return result;
    }
}
