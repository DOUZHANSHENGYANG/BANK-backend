package xyz.douzhan.bank.utils;

import cn.hutool.core.collection.CollUtil;
import xyz.douzhan.bank.constants.BizExceptionConstant;
import xyz.douzhan.bank.exception.BizException;

import java.util.Collection;

/**
 * 一些声明信息
 * Description:业务工具类
 * date: 2023/12/9 15:36
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class CommonBizUtils {

    /**
     * 判断参数是否为空
     * @param args
     */
    public static void assertArgsNotNull(Object... args){
        if (args==null||args.length==0){
            throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_NOT_NULL);
        }
        for (Object arg : args) {
            if (arg==null){
                throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_NOT_NULL);
            }
        }
    }
    /**
     * 判断集合是否为空
     * @param coll
     */
    public static void assertCollNotNull(Collection coll){
        if (CollUtil.isEmpty(coll)){
            throw new BizException(BizExceptionConstant.BUSINESS_PARAMETERS_ARE_INVALID);
        }
    }



}
