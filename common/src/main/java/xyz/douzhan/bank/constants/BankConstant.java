package xyz.douzhan.bank.constants;

/**
 * 一些声明信息
 * Description:银行业务常量
 * date: 2023/12/9 20:43
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public interface BankConstant {

    //手续费
    Double DEFAULT_PREMIUM=0.00;
    Integer PHONE_BANK_CHANNEL=1;
    String BANK_NAME="CUMT银行";
    String DEFAULT_ACCOUNT="默认账户";
    String DEBIT_CARD="借记卡";
    String ELECTRONIC_ACCOUNTS="电子账户";
    String REFUSE_TO_REGISTER_A_RESPONSE="如您尚未在本行开立账户，请线下申请账户";
    String CONTRACTED_ACCOUNTS="签约账户";
    Integer FIRST_ACCOUNT=0;
    Integer SECOND_ACCOUNT=1;
    Integer THIRD_ACCOUNT=2;

    String ACCOUNT_IDENTIFIER="银行账户标识符";
    Double DEFAULT_BALANCE=0.00;
    String IS_DEFAULT_ACCOUNT="0";
    String IS_NOT_DEFAULT_ACCOUNT="1";

    String TRANSACTION_ORDER_NUM_REDIS_PREFIX="user:transaction:order:";
    String TRANSACTION_SMS_NUM_REDIS_PREFIX="user:transaction:sms:";
    Long SMS_EXPIRE_TIME=60L;

}
