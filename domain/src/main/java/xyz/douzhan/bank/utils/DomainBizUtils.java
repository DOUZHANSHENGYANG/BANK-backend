package xyz.douzhan.bank.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import xyz.douzhan.bank.constants.BizExceptionConstant;
import xyz.douzhan.bank.enums.AccountStatus;
import xyz.douzhan.bank.enums.BankcardStatus;
import xyz.douzhan.bank.exception.BizException;

import java.time.LocalDateTime;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/10 21:29
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class DomainBizUtils {
    /**
     * 判断账户状态是否为正常
     * @param accountStatus
     */
    public static void assertAccountNormal(AccountStatus accountStatus){

        String result=switch (accountStatus){
            case NORMAL,LOGOUT -> "";
            case NOT_ACTIVATED -> String.format(BizExceptionConstant.INVALID_ACCOUNT_TEMPLATE,"激活");
            case LOSS -> String.format(BizExceptionConstant.INVALID_ACCOUNT_TEMPLATE,"解除挂失");
            case  FREEZE-> String.format(BizExceptionConstant.INVALID_ACCOUNT_TEMPLATE,"解冻");
            case SLEEP -> String.format(BizExceptionConstant.INVALID_ACCOUNT_TEMPLATE,"交易");
        };
        if (!StrUtil.equals(result,"")){
            throw new BizException(result);
        }


    }

    /**
     * 判断银行卡状态是否为正常
     * @param bankcardStatus
     */
    public static void assertBankcardNormal(BankcardStatus bankcardStatus){
        String result=switch (bankcardStatus){
            case NORMAL -> "";
            case NOT_ACTIVE -> String.format(BizExceptionConstant.INVALID_BANKCARD_TEMPLATE,"激活");
            case LOCK -> String.format(BizExceptionConstant.INVALID_BANKCARD_TEMPLATE,"解锁");
        };
        if (!StrUtil.equals(result,"")){
            throw new BizException(result);
        }
    }

    /**
     * 生成订单号 0-231226-00000-0001 0表示交易类型  付款人sc 收款人sc 231266表示年月日 00000表示秒 最后两位表示订单号
     * @return
     */
    public static String genOrderNum(Integer type,String transferorSwiftCode,String transfereeSwiftCode,Integer num){
        String transactionType= String.valueOf(type);

        LocalDateTime now = LocalDateTime.now();
        String nowDate= DateUtil.format(now, "yyMMdd");
        String nowTime= String.valueOf(now.getSecond());

        return transactionType+transferorSwiftCode+transfereeSwiftCode+nowDate+nowTime+num;
    }
}
