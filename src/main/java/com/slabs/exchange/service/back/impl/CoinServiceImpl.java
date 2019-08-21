package com.slabs.exchange.service.back.impl;

import com.slabs.exchange.common.enums.AttachEnum;
import com.slabs.exchange.common.enums.CoinEnum;
import com.slabs.exchange.common.enums.YNEnum;
import com.slabs.exchange.mapper.UserMapper;
import com.slabs.exchange.mapper.back.AttachFileMapper;
import com.slabs.exchange.mapper.back.CoinMapper;
import com.slabs.exchange.mapper.back.ProjectCoinMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.AttachFileDto;
import com.slabs.exchange.model.dto.CoinDto;
import com.slabs.exchange.model.dto.ProjectCoinDto;
import com.slabs.exchange.model.entity.*;
import com.slabs.exchange.service.BaseService;
import com.slabs.exchange.service.back.ICoinService;
import com.slabs.exchange.util.ShiroUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CoinServiceImpl extends BaseService implements ICoinService {
    @Resource
    private CoinMapper coinMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ProjectCoinMapper projectCoinMapper;
    @Resource
    private AttachFileMapper attachFileMapper;

    /**
     * 得到所有的 USDT 和 HOS
     */
    @Override
    public ResponseBean getCoins() {
        List<Coin> list = coinMapper.getCoins();
        return new ResponseBean(200, "", list);
    }

    /**
     * 新增加币逻辑
     */
    @Override
    public ResponseBean insert(CoinDto coinDto) {
        // todo 调用发币地址
        User user = userMapper.selectByPrimaryKey(coinDto.getUserId());
        // wallet_add, amount, name
        String contractAddr = "I am a project coin token addr.";

        // 如果捕获到异常，创建币种失败。
        // 如果成功的话，才执行后续逻辑过程。
        Coin coin = map(coinDto, Coin.class);
        coin.setPrecision(6);
        coinMapper.insert(coin);
        ProjectCoin projectCoin = new ProjectCoin();
        //该项目币的控制人
        projectCoin.setUserId(coinDto.getUserId());
        projectCoin.setAmount(coinDto.getAmount());
        projectCoin.setCoinId(coin.getId());
        // other 代表项目币
        projectCoin.setCoinType(CoinEnum.OTHER.getKey());
        projectCoin.setContractAddr(contractAddr);
        projectCoinMapper.insert(projectCoin);

        // 构建附件信息
        List<AttachFile> list = buildAttachFile(coin.getId(), coinDto);
        // 批量插入附件信息
        attachFileMapper.batchInsert(list);

        return new ResponseBean(200, "", null);
    }

    /**
     * 构建附件信息
     */
    private List<AttachFile> buildAttachFile(Integer coinId, CoinDto coinDto) {
        List<AttachFileDto> list = coinDto.getAttachFileList();
        List<AttachFile> attachFiles = new ArrayList<>();
        for (AttachFileDto afd: list) {
            AttachFile af = map(afd, AttachFile.class);
            af.setIsDel(YNEnum.N.getKey());
            af.setRefId(coinId);
            af.setType(AttachEnum.PRO_COIN.getKey());
            af.setCreateUser(ShiroUtils.getUserId().longValue());
            af.setCreateTime(new Date());
            attachFiles.add(af);
        }
        return attachFiles;
    }

    @Override
    public ResponseBean getNonsymbolCoin() {

        List<Coin> list = coinMapper.getNonsymbolCoin();
        return new ResponseBean(200, "", list);
    }
}
