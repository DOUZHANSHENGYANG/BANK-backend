package xyz.douzhan.bank.utils;

import xyz.douzhan.bank.constants.BizExceptionConstants;
import xyz.douzhan.bank.exception.BizException;

/**
 * 一些声明信息
 * Description:业务工具类
 * date: 2023/12/9 15:36
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class BizUtils {
    /**
     * 判断参数是否为空
     * @param args
     */
    public static void assertArgsNotNull(Object... args){
        if (args==null||args.length==0){
            throw new BizException(BizExceptionConstants.BUSINESS_PARAMETERS_ARE_NOT_NULL);
        }
        for (Object arg : args) {
            if (arg==null){
                throw new BizException(BizExceptionConstants.BUSINESS_PARAMETERS_ARE_NOT_NULL);
            }
        }
    }
}
