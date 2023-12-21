package xyz.douzhan.bank.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import xyz.douzhan.bank.constants.BizExceptionConstant;
import xyz.douzhan.bank.enums.BankcardStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static xyz.douzhan.bank.enums.BankcardStatus.*;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/10 21:29
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class DomainBizUtil {


    /**
     * 判断账户状态是否为正常
     *
     * @param bankcardStatus
     */
    public static Boolean isAccountNormal(BankcardStatus bankcardStatus) {

        String result = switch (bankcardStatus) {
            case NORMAL, LOGOUT -> "";
            case NOT_ACTIVATED -> String.format(BizExceptionConstant.INVALID_ACCOUNT_TEMPLATE, "激活");
            case LOSS -> String.format(BizExceptionConstant.INVALID_ACCOUNT_TEMPLATE, "解除挂失");
            case FREEZE -> String.format(BizExceptionConstant.INVALID_ACCOUNT_TEMPLATE, "解冻");
            case SLEEP -> String.format(BizExceptionConstant.INVALID_ACCOUNT_TEMPLATE, "交易");
        };
        if (!StrUtil.equals(result, "")) {
            return false;
        }
        return true;
    }

    /**
     * 判断状态合法性
     *
     * @param status
     * @return
     */
    public static BankcardStatus hasStatus(Integer status) {
        List<BankcardStatus> bankcardStatuses = Arrays.stream(values())
                .filter(bankcardStatus -> bankcardStatus.getValue() == status.intValue())
                .toList();

        if (CollUtil.isEmpty(bankcardStatuses)) {
            return null;
        }
        return bankcardStatuses.get(0);
    }


    /**
     * 生成订单号 0-231226-00000-00001 0表示交易类型  231266表示年 17位
     * 月日 00000表示秒 最后四位表示订单号
     *
     * @return
     */
    public static String genOrderNum(Integer type, Integer num) {
        String transactionType = String.valueOf(type);

        LocalDateTime now = LocalDateTime.now();
        String nowDate = DateUtil.format(now, "yyMMdd");
        String nowTime = String.valueOf(now.getSecond());
        String numStr = String.format("%05d", num);

        return new StringBuffer(4).append(transactionType)
                .append(nowDate)
                .append(nowTime)
                .append(numStr)
                .toString();
    }
}
