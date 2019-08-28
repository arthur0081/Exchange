package com.slabs.exchange.util;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slabs.exchange.common.config.ExchangeApiProperties;
import com.slabs.exchange.common.exception.ExchangeException;
import com.slabs.exchange.model.dto.ExchangeApiResDto;
import com.slabs.exchange.model.dto.IssueTokenDto;
import com.slabs.exchange.model.dto.NewWalletAddrDto;
import com.slabs.exchange.model.dto.WalletResponseDto;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class ExchangeApiUtil {
    private static final Gson gson = new GsonBuilder().create();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Resource
    private ExchangeApiProperties exchangeApiProperties;

    public WalletResponseDto getWalletAddr(Integer userId) {
        NewWalletAddrDto walletAddrDto = new NewWalletAddrDto();
        walletAddrDto.setUserId(userId);
        String requestData = gson.toJson(walletAddrDto);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder()
                .url(exchangeApiProperties.getWalletAddr())
                .post(RequestBody.create(mediaType, requestData))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        String resData = "";
        try {
            resData = okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("user id:" + ShiroUtils.getUserId() + "wallet addr failed." + sdf.format(new Date()));
            throw new ExchangeException("获取钱包地址失败！");
        }
        WalletResponseDto walletResponseDto =  gson.fromJson(resData, WalletResponseDto.class);
        return walletResponseDto;
    }

    public WalletResponseDto issueToken(IssueTokenDto issueTokenDto) {
        String requestData = gson.toJson(issueTokenDto);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder()
                .url(exchangeApiProperties.getIssueToken())
                .post(RequestBody.create(mediaType, requestData))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        String resData = "";
        try {
            resData = okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("issue token, walletAddr:" + issueTokenDto.getAddress() + "failed." + sdf.format(new Date()));
            throw new ExchangeException("创建币种失败！");
        }
        WalletResponseDto walletResponseDto =  gson.fromJson(resData, WalletResponseDto.class);
        return walletResponseDto;
    }

}
