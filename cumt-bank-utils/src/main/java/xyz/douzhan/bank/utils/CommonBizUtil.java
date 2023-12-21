package xyz.douzhan.bank.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.crypto.digest.SM3;
import xyz.douzhan.bank.constants.BizExceptionConstant;
import xyz.douzhan.bank.exception.BizException;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * 一些声明信息
 * Description:业务工具类
 * date: 2023/12/9 15:36
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class CommonBizUtil {


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

    /**
     * 根据luhn算法生成校验位
     *
     * @param part
     * @return
     */
    public static int genVerifyBitByLuhn(String part){
        boolean flag=false;
        int sum=0;
        for (int i = part.length()-1; i >=0 ; i--) {
            int num = Integer.parseInt(String.valueOf(part.charAt(i)));
            if (flag){
                num=num*2;
                if (num>9){
                    sum=sum+num/10+num%10;
                }else {
                    sum=sum+num;
                }
            }else {
                num=num+sum;
            }
            flag=!flag;
        }
        return (sum*9)%10;
    }
    public static Boolean verifyByLuhn(String number){
        boolean flag=true;
        int sum=0;
        for (int i = number.length()-1; i >=0; i++) {
            int num = Integer.parseInt(String.valueOf(number.charAt(i)));
            if (flag){
                sum=sum+num;
            }else {
                num=num*2;
                if (num>9){
                    sum=sum+num/10+num%10;
                }else {
                    sum=sum+num;
                }
            }
            flag=!flag;
        }
        return sum%10==0;
    }

//    /**
//     *获取截断版本和hash结果
//     * @param cardNum
//     */
//    public static HashMap<String,String> getTruncateAndHashPart(String cardNum) {
//        HashMap<String, String> map = new HashMap<>();
//        map.put(BizConstant.TRUNCATE_REMAIN_NUM, cardNum.substring(0,6)+cardNum.substring(15));
//        map.put(BizConstant.HASH_NUM,hashedPart(cardNum));
//        return map;
//    }
    public static String hashedPart(String cardNum){
        //用卡号后四位做盐值
        SM3 sm3 = new SM3(cardNum.substring(15).getBytes(StandardCharsets.UTF_8), 4);
        return sm3.digestHex(cardNum.substring(6,15),StandardCharsets.UTF_8);
    }
}
