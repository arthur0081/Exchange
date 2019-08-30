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

    @Select("select aa.contract_addr contractAddr, aa.user_id userId, u.wallet_addr walletAddr from ( select pc.contract_addr , pc.user_id from t_coin tc, project_coin pc where tc.id = pc.coin_id and tc.name = #{coinName}) aa ,sysuser u where aa.user_id = u.id")
    WalletAndContractAddrDto getWalletAndContractAddrByCoinName(String coinName);

}