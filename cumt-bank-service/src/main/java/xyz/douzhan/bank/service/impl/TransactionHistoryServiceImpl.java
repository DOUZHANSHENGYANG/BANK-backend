package xyz.douzhan.bank.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import xyz.douzhan.bank.constants.TransferConstant;
import xyz.douzhan.bank.context.UserContext;
import xyz.douzhan.bank.dto.TransactionDetailsDTO;
import xyz.douzhan.bank.dto.TransactionDetailsVO;
import xyz.douzhan.bank.dto.result.PageResponseResult;
import xyz.douzhan.bank.mapper.TransactionHistoryMapper;
import xyz.douzhan.bank.po.BankPhoneBankRef;
import xyz.douzhan.bank.po.Bankcard;
import xyz.douzhan.bank.po.TransactionHistory;
import xyz.douzhan.bank.service.TransactionHistoryService;
import xyz.douzhan.bank.utils.CypherUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-11
 */
@Service
public class TransactionHistoryServiceImpl extends ServiceImpl<TransactionHistoryMapper, TransactionHistory> implements TransactionHistoryService {


    /**
     * 获取交易明细
     *
     * @param detailsDTO
     * @return
     */
    @Override
    public PageResponseResult getTransactionDetails(TransactionDetailsDTO detailsDTO) {
        detailsDTO.setCardNum(CypherUtil.encrypt(detailsDTO.getCardNum()));

        Page<TransactionHistory> page = new Page<>(detailsDTO.getStartPage(), detailsDTO.getPageNum());

        LocalDate startDate = detailsDTO.getStartTime();
        LocalDate endDate = detailsDTO.getEndTime();

        LambdaQueryWrapper<TransactionHistory> lqw = new LambdaQueryWrapper<>();
        lqw.eq(TransactionHistory::getTransferorNum, detailsDTO.getCardNum()).or().eq(TransactionHistory::getTransfereeNum, detailsDTO.getCardNum());
        lqw.between(TransactionHistory::getCreateTime, startDate, endDate);
        lqw.eq(TransactionHistory::getResult, TransferConstant.SUCCESS_FINISH);

        baseMapper.selectPage(page, lqw);
        List<TransactionDetailsVO> transactionDetailsVOS = Collections.emptyList();
        List<TransactionHistory> records = page.getRecords();

        if (!CollUtil.isEmpty(records)) {
            transactionDetailsVOS = records.stream().map(record -> {
                TransactionDetailsVO transactionDetailsVO = new TransactionDetailsVO();
                BeanUtils.copyProperties(record, transactionDetailsVO);
                transactionDetailsVO.setTransferorName(CypherUtil.decrypt(record.getTransferorName()));
                transactionDetailsVO.setTransfereeName(CypherUtil.decrypt(record.getTransfereeName()));
                transactionDetailsVO.setTransferorNum(CypherUtil.decrypt(record.getTransferorNum()));
                transactionDetailsVO.setTransfereeNum(CypherUtil.decrypt(record.getTransfereeNum()));
                if (StrUtil.equals(detailsDTO.getCardNum(), record.getTransferorNum())) {
                    transactionDetailsVO.setIe(TransferConstant.PAYMENT);
                } else {
                    transactionDetailsVO.setIe(TransferConstant.INCOME);
                }
                return transactionDetailsVO;
            }).toList();
        }

        return PageResponseResult.success(transactionDetailsVOS, page.getTotal(), page.getCurrent(), page.getSize());
    }

    /**
     * 获取本月收支
     *
     * @param type
     * @return
     */
    @Override
    public Integer getIE(Integer type) {
        Long phoneAccountId = UserContext.getContext();
        List<String> bankcardNumbers = Db.lambdaQuery(BankPhoneBankRef.class).eq(BankPhoneBankRef::getPhoneAccountId, phoneAccountId).select(BankPhoneBankRef::getAccountId).list().stream().map(bankPhoneBankRef -> Db.lambdaQuery(Bankcard.class).eq(Bankcard::getId, bankPhoneBankRef.getAccountId()).select(Bankcard::getNumber).one().getNumber()).toList();



        LocalDateTime now = LocalDateTime.now();
        LocalDateTime first = now.withDayOfMonth(1);





        Integer result = 0;
        if (TransferConstant.PAYMENT.compareTo(type) == 0) {
            result = bankcardNumbers.stream().map(bankcardNumber -> baseMapper.selectList(Wrappers.lambdaQuery(TransactionHistory.class)
                            .eq(TransactionHistory::getTransferorNum, bankcardNumber)
                    .eq(TransactionHistory::getResult, TransferConstant.SUCCESS_FINISH)
                    .between(TransactionHistory::getCreateTime, first, now))
                    .stream()
                    .map(TransactionHistory::getMoney)
                    .reduce(0, Integer::sum)).reduce(0, Integer::sum);
        } else {
            result = bankcardNumbers.stream().map(bankcardNumber -> baseMapper.selectList(Wrappers.lambdaQuery(TransactionHistory.class)
                            .eq(TransactionHistory::getTransfereeName, bankcardNumber)
                            .eq(TransactionHistory::getResult, TransferConstant.SUCCESS_FINISH)
                            .between(TransactionHistory::getCreateTime, first, now))
                    .stream()
                    .map(TransactionHistory::getMoney)
                    .reduce(0, Integer::sum)).reduce(0, Integer::sum);
        }
        return result;
    }
}
