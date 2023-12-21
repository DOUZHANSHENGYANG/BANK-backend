package xyz.douzhan.bank.constants;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/11 13:26
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class TransferConstant {
    /**
     * 支出
     */
    public static final Integer PAYMENT=1;
    /**
     * 收入
     */
    public static final Integer INCOME=0;
    /**
     * 最小金额
     */
    public static final Integer ZERO_BALANCE=0;
    /**
     * 默认手续费用
     */
    public static final Integer DEFAULT_PREMIUM=0;
    /**
     * 默认转账备注
     */
    public static final String DEFAULT_REMARK="转账";
    /**
     * 转账类型
     */
    public static final Integer OR_CODE_REMIT=3;
    public static final Integer OR_CODE_CREDIT=4;
    public static final Integer INTER_BANK=1;
    public static final Integer INTRA_BANK=0;
    /**
     * 转账状态 0进行中 1成功结束 2异常结束
     */
    public static final Integer ONGOING_STATUS=0;
    public static final Integer SUCCESS_FINISH=1;
    public static final Integer FAILED_FINISH=2;

    /**
     * 手机银行转账限额 单笔50w
     */
    public static final Integer PHONE_ACCOUNT_SINGLE_LIMIT=50000000;
    /**
     * 手机银行转账限额 日累计500w
     */
    public static final Integer FIRST_ACCOUNT_DAY_TOTAL_LIMIT=500000000;
    /**
     * 二类账户限额 触发条件
     * 存款和接收非绑定账户转入资金日累计金额不得高于1万元，年累计金额不得高于20万元
     * 取款、消费、缴费和向非绑定账户转出资金日累计金额不得高于1万元，年累计金额不得高于20万元
     */

    public static final Integer SECOND_ACCOUNT_DAY_LIMIT=1000000;
    public static final Integer SECOND_ACCOUNT_YEAR_LIMIT=20000000;
    /**
     * 三类账户限额 触发条件
     * 消费、缴费、向非绑定账户转出资金日累计金额不得高于2000元，年累计金额不得高于5万元
     * 账户余额不得超过2000元
     */
    public static final Integer THIRD_ACCOUNT_DAY_LIMIT=200000;
    public static final Integer THIRD_ACCOUNT_YEAR_LIMIT=5000000;
    public static final Integer THIRD_ACCOUNT_BALANCE_LIMIT=200000;

}
