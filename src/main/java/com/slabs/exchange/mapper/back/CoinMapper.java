package com.slabs.exchange.mapper.back;

import com.slabs.exchange.model.dto.WalletAndContractAddrDto;
import com.slabs.exchange.model.entity.Coin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CoinMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Coin record);

    int insertSelective(Coin record);

    Coin selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Coin record);

    int updateByPrimaryKey(Coin record);

    List<Coin> getCoins();

    List<Coin> getNonsymbolCoin();

    List<Coin> getAllCoins();

    @Select("select s.wallet_addr walletAddr, pc.contract_addr contractAddr, s.id userId from t_coin tc, project_coin pc, sysuser s where tc.id = pc.coin_id and pc.user_id = s.id and tc.name = #{coinName}")
    WalletAndContractAddrDto getWalletAndContractAddrByCoinName(String coinName);

}