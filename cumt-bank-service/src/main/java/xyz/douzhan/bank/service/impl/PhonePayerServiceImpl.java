package xyz.douzhan.bank.service.impl;

import xyz.douzhan.bank.po.PhonePayer;
import xyz.douzhan.bank.mapper.PhonePayerMapper;
import xyz.douzhan.bank.service.PhonePayerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.douzhan.bank.utils.CypherUtil;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 斗战圣洋
 * @since 2023-12-06
 */
@Service
public class PhonePayerServiceImpl extends ServiceImpl<PhonePayerMapper, PhonePayer> implements PhonePayerService {

    /**
     * 修改
     *
     * @param phonePayer
     */
    @Override
    public void update(PhonePayer phonePayer) {
        if (phonePayer.getName()!=null){
            phonePayer.setName(CypherUtil.encrypt(phonePayer.getName()));
        }
        if (phonePayer.getBankcardNum()!=null){
            phonePayer.setBankcardNum(CypherUtil.encrypt(phonePayer.getBankcardNum()));
        }
        baseMapper.updateById(phonePayer);
    }
}
