import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slabs.exchange.common.exception.ExchangeException;
import com.slabs.exchange.model.dto.*;
import com.slabs.exchange.util.JWTUtil;
import okhttp3.*;

import java.io.IOException;
import java.math.BigDecimal;

public class ExchangeOrderTest {
    private static final Gson gson = new GsonBuilder().create();

    public static void main(String[] args) {
        getWalletAddr();

    }




    private static void getWalletAddr() {
        NewWalletAddrDto walletAddrDto = new NewWalletAddrDto();
        walletAddrDto.setUserId(20000);
        String requestData = gson.toJson(walletAddrDto);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder()
                .url("http://192.168.50.197:10000/newAddress")
                .post(RequestBody.create(mediaType, requestData))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        String resData = "";
        try {
             resData = okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ExchangeException("获取钱包地址失败！");
        }
        WalletResponseDto walletResponseDto =  gson.fromJson(resData, WalletResponseDto.class);

    }

    /**
     * 撤单测试
     */
    private static void withdrawOrder() {
        WithdrawRequestDto wd = new WithdrawRequestDto();
        wd.setOrder_id("86251921723101184");
        String data = gson.toJson(wd);
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        Request request = new Request.Builder()
                .url("http://39.104.136.10:8000" + "/cancel/" + "btc_usdt")
                .post(RequestBody.create(mediaType, data))
                .header("Authorization", "Bearer" + " " + JWTUtil.encode("10001"))//使用平台方用户的token进行撤单
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        String resData = "";
        try {
             resData = okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            ExchangeApiResDto ea = gson.fromJson(resData, ExchangeApiResDto.class);
            System.out.println(ea.getId());
        }
        ExchangeApiResDto eard = gson.fromJson(resData, ExchangeApiResDto.class);
        System.out.println(eard.getId());
    }

    /**
     * 测试买单
     */
    private static void buyOrder(String side) {
        OrderDto orderDto = new OrderDto();
        orderDto.setSide(side);
        orderDto.setAmount(new BigDecimal(10));
        orderDto.setPrice(new BigDecimal(8954));
        String requestData = gson.toJson(orderDto);
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        Request request = new Request.Builder()
                .url("http://39.104.136.10:8000" + "/limit/" + "btc_usdt")
                .post(RequestBody.create(mediaType, requestData))
                .header("Authorization", "Bearer" + " " + JWTUtil.encode("10001"))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        String resData = "";
        try {
            resData = okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            //log.error("user id:" + ShiroUtils.getUserId() + "ordered failed." + sdf.format(new Date()));
            System.out.println("购买失败，请重新购买！");
        }
        ExchangeApiResDto exchangeApiResDto = gson.fromJson(resData, ExchangeApiResDto.class);
        if (exchangeApiResDto.getId() == null) {
            System.out.println("购买失败，请重新购买！！");
        }
        System.out.println("我是请求后返回的交易txid:" + exchangeApiResDto.getId());
    }



}
