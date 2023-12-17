package xyz.douzhan.bank.service;

import com.alibaba.fastjson2.JSONObject;
import xyz.douzhan.bank.dto.BankCardVO;
import xyz.douzhan.bank.dto.result.ResponseResult;
import xyz.douzhan.bank.po.Bankcard;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
public interface BankCardService extends IService<Bankcard> {

    /**
     * 更新银行卡状态
     * @param bankcardId
     * @param status
     */
    void updateStatus(Long bankcardId, Integer status);

    /**
     * 开通银行卡账户
     * @param bankcardInfo
     * @param phoneAccountId
     */
    void createBankcardAccount(Bankcard bankcardInfo, Long phoneAccountId);

    /**
     * 根据手机账户id查询卡号和手机号
     * @param phoneAccountId
     * @param type
     * @return
     */
    JSONObject getCardNumAndPhoneNumber(Long phoneAccountId, Integer type);

    /**
     * 根据手机号查询I类账户id
     * @param phoneNumber
     * @return
     */
    ResponseResult getFirstAccount(String phoneNumber);

    /**
     * 根据银行卡id集合查询账户信息
     *
     * @param bankcardIds
     * @return
     */
    List<BankCardVO> getByIds(List<Long> bankcardIds);

    /**
     * 根据银行卡id查询完整卡号
     * @param bankcardId
     * @return
     */
    ResponseResult getBankcardNumber(Long bankcardId);
}
