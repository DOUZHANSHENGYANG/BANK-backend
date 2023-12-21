package xyz.douzhan.bank.constants;

import jdk.dynalink.beans.StaticClass;

import java.util.Stack;

/**
 * 一些声明信息
 * Description:银行业务常量
 * date: 2023/12/9 20:43
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class BizConstant {
    public static final Integer ID_CARD=1;
    public static final Integer BANKCARD=0;
    public static final Integer PIC_FRONT=0;
    public static final Integer PIC_BACK=1;
    public static final String DOCUMENTS_FRONT="front";
    public static final String DOCUMENTS_BACK="back";
    public static final String DOCUMENTS="documents";
    public static final String TRUNCATE_REMAIN_NUM="truncate";
    public static final String HASH_NUM="hash";
    public static final Integer HASH_COUNT=3;
    public static final Integer DEFAULT_MONEY=0;
    //银行卡没有实体
    public static Integer HAS_NOT_ENTITY=0;
    //银行卡有实体
    public static Integer HAS_ENTITY=1;

    //手续费
    public static Integer DEFAULT_PREMIUM = 0;
    public static Integer PHONE_BANK_CHANNEL = 1;
    public static String BANK_NAME = "CUMT银行";
    public static String DEFAULT_ACCOUNT = "默认账户";
    public static String DEBIT_CARD = "借记卡";
    public static String ELECTRONIC_ACCOUNTS = "电子账户";
    public static String REFUSE_TO_REGISTER_A_RESPONSE = "如您尚未在本行开立账户，请线下申请账户";
    public static String CONTRACTED_ACCOUNTS = "签约账户";
    public static String ACCOUNT_IDENTIFIER = "银行账户标识符";
    public static Long DEFAULT_BALANCE = 0L;
    public static Integer IS_DEFAULT_ACCOUNT = 0;
    public static Integer IS_NOT_DEFAULT_ACCOUNT = 1;

    public static String FIRST_ACCOUNT = "firstAccount";
    public static String SECOND_ACCOUNT = "secondAccount";
    public static String THIRD_ACCOUNT = "thirdAccount";
    public static Long SMS_EXPIRE_TIME = 60L;


}
