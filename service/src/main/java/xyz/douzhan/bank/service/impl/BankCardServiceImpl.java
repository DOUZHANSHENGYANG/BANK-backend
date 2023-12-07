package xyz.douzhan.bank.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import xyz.douzhan.bank.po.Account;
import xyz.douzhan.bank.po.BankCard;
import xyz.douzhan.bank.mapper.BankCardMapper;
import xyz.douzhan.bank.service.BankCardService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.douzhan.bank.vo.BankCardVO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Service
public class BankCardServiceImpl extends ServiceImpl<BankCardMapper, BankCard> implements BankCardService {
    private final String BANK_DESCRIPTION="中国矿大银行";
    /**
     * 根据手机号查询银行卡信息 类型辅助
     * @return
     */
    @Override
    public List<BankCardVO> getByPhoneNumber(String phoneNumber, Integer type) {
        Account account = Db.lambdaQuery(Account.class).eq(Account::getPhoneNumber, phoneNumber).one();
        List<BankCard> bankCardList = Db.lambdaQuery(BankCard.class).eq(account != null, BankCard::getAccountId, account.getId()).list();

        List<BankCardVO> bankCardVOList= Collections.emptyList();
        BankCardVO bankCardVO = new BankCardVO();
        if (type==0){
            //如果为全查
            //TODO 确定全查字段名
            bankCardVOList = bankCardList.stream().map(bankCard -> bankCardVO).collect(Collectors.toList());
        }else if (type==1){
            //如果只查卡号
            bankCardVOList = bankCardList.stream().map(bankCard -> {
                bankCardVO.setCardNumber(bankCard.getNumber());
                bankCardVO.setBankDescription(BANK_DESCRIPTION);
                return bankCardVO;
            }).collect(Collectors.toList());
        }
        return bankCardVOList;
    }
}
