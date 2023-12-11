package xyz.douzhan.bank.constants;

/**
 * 一些声明信息
 * Description:
 * date: 2023/12/11 13:26
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public interface TransactionConstant {
    String DEFAULT_REMARK="转账";
    Integer INTER_BANK=0;
    Integer INTRA_BANK=1;
    Integer ONGOING_STATUS=0;
    Integer SUCCESS_FINISH=1;
    Integer FAILED_FINISH=1;

}
