package com.slabs.exchange.service.fore.impl;

import com.slabs.exchange.common.exception.ExchangeException;
import com.slabs.exchange.mapper.UserMapper;
import com.slabs.exchange.mapper.fore.WithdrawMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.WithdrawDto;
import com.slabs.exchange.model.entity.User;
import com.slabs.exchange.model.entity.Withdraw;
import com.slabs.exchange.service.BaseService;
import com.slabs.exchange.service.fore.IWithdrawService;
import com.slabs.exchange.util.AESUtil;
import com.slabs.exchange.util.ExchangePreconditions;
import com.slabs.exchange.util.ShiroUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;


@Service
@Slf4j
public class WithdrawServiceImpl extends BaseService implements IWithdrawService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private WithdrawMapper withdrawMapper;

    @Override
    public ResponseBean withdraw(WithdrawDto withdrawDto) {
        ExchangePreconditions.notNull(withdrawDto, "参数不能为空！");
        //2. 对用户资金密码进行校验
        Integer userId = ShiroUtils.getUserId();
        User user = userMapper.selectByPrimaryKey(userId);
        String plainPassword = AESUtil.desEncrypt(withdrawDto.getFundPassword());
        if (!user.getFundPassword().equals(ShiroUtils.encodeSalt(plainPassword, user.getSalt()))) {
            throw new ExchangeException("输入的资金密码错误！");
        }
        //3. 调用钱包的提现接口 todo 它会返回txid
        // TODO
        Withdraw withdraw = new Withdraw();
        withdraw.setAmount(withdrawDto.getAmount());
        withdraw.setCoin(withdrawDto.getCoin());
        withdraw.setId(userId);
        withdraw.setStatus(false);
        withdraw.setTxid("txid");
        withdraw.setTime(new Date());
        withdrawMapper.insert(withdraw);

        return new ResponseBean(200, "", null);
    }
}
