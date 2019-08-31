package com.slabs.exchange.schedule;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.slabs.exchange.common.config.ExchangeApiProperties;
import com.slabs.exchange.common.config.ScheduleProperties;
import com.slabs.exchange.common.enums.BuyAndSellEnum;
import com.slabs.exchange.common.enums.ProjectStatusEnum;
import com.slabs.exchange.common.enums.WithdrawStatusEnum;
import com.slabs.exchange.mapper.back.BoughtAmountMapper;
import com.slabs.exchange.mapper.back.ProjectMapper;
import com.slabs.exchange.mapper.back.SymbolMapper;
import com.slabs.exchange.mapper.ext.back.BoughtAmountExtMapper;
import com.slabs.exchange.model.dto.BoughtAmountDto;
import com.slabs.exchange.model.dto.ExchangeApiResDto;
import com.slabs.exchange.model.dto.OrderDto;
import com.slabs.exchange.model.entity.Project;
import com.slabs.exchange.model.entity.Symbol;
import com.slabs.exchange.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

/**
 * 稳定项目状态变更（定时任务）
 */
@Component
@Slf4j
public class ProjectStatusSchedule {
    private static final Gson gson = new GsonBuilder().create();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Resource
    private BoughtAmountMapper boughtAmountMapper;
    @Resource
    private ExchangeApiProperties exchangeApiProperties;
    @Resource
    private ScheduleProperties scheduleProperties;
    @Resource
    private SymbolMapper symbolMapper;
    @Resource
    private ProjectMapper projectMapper;
    @Resource
    private BoughtAmountExtMapper boughtAmountExtMapper;


    /**
     * 发布前状态不用考虑
     * 只考虑发布后状态变更
     * 稳定项目（一天执行一次）
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void stable() {
        // 细节就是，改变状态更新数据；没有改变状态的，保持原来的逻辑。
        while (true) {
            // 得到所有发布后的项目（预售中，认购中，认购结束）（查询的时候不要关心时间，只关心状态）
            List<Project> projectList =  projectMapper.getAfterPublish();
            if (projectList.size() == 0) {
                return;
            }

            for (Project project: projectList) {
                Long projectId = project.getId();
                // 求得已经认购的总额度
                BoughtAmountDto boughtAmountDto = boughtAmountExtMapper.getAmountByProjectId(projectId);
                // 获取时间
                LocalDateTime now = LocalDateTime.now();
                //当前时间减去投资天数
                now = now.minus(project.getInvestPeriod(), ChronoUnit.DAYS);
                ZoneId zoneId = ZoneId.systemDefault();
                ZonedDateTime zdt = now.toLocalDate().atStartOfDay(zoneId);
                Date nowDate = Date.from(zdt.toInstant());

                // 预售中
                if (ProjectStatusEnum.PRE_SALE.getKey().equals(project.getStatus())) {
                    //项目开始时间大于当前时间
                    if(project.getStartTime().after(new Date())) {
                        // 项目开始时间  >  当前时间 - 认购天数
                        if (project.getStartTime().after(nowDate)) {
                            if (project.getCollectAmount().compareTo(boughtAmountDto.getAmount()) > 0) {
                                //认购中
                                project.setStatus(ProjectStatusEnum.ON_SALE.getKey());
                                projectMapper.updateByPrimaryKeySelective(project);
                            } else {
                                //认购结束
                                project.setStatus(ProjectStatusEnum.END_SALE.getKey());
                                projectMapper.updateByPrimaryKeySelective(project);
                                symbolMapper.updateValid(project.getSymbol().intValue(), true);
                            }

                        } else {
                            if (project.getCollectAmount().compareTo(boughtAmountDto.getAmount()) == 0) {
                                //项目结束
                                // 项目方挂买单（项目方回购所有的项目币）
                                projectThirdOrderBuy(project);
                            } else {
                                //项目失败
                                project.setStatus(ProjectStatusEnum.INVALID.getKey());
                                projectMapper.updateByPrimaryKeySelective(project);
                                boughtAmountMapper.updateWithdrawByProjectId(projectId.intValue(), Integer.valueOf(WithdrawStatusEnum.WITHDRAW_NEED.getKey()));
                            }
                        }
                    }
                }

                //认购中（项目方挂卖单）
                if (ProjectStatusEnum.ON_SALE.getKey().equals(project.getStatus())) {
                    //项目开始时间大于当前时间
                    if(project.getStartTime().after(new Date())) {
                        // 项目开始时间  >  当前时间 - 认购天数
                        if (project.getStartTime().after(nowDate)) {
                            if (project.getCollectAmount().compareTo(boughtAmountDto.getAmount()) > 0) {
                                //保持认购状态
                            } else {
                                //认购结束
                                projectThirdOrderSell(project);
                            }

                        } else {
                            if (project.getCollectAmount().compareTo(boughtAmountDto.getAmount()) == 0) {
                                //项目结束
                                // 项目方挂买单（项目方回购所有的项目币）
                                projectThirdOrderBuy(project);
                            } else {
                                //项目失败
                                project.setStatus(ProjectStatusEnum.INVALID.getKey());
                                projectMapper.updateByPrimaryKeySelective(project);
                                boughtAmountMapper.updateWithdrawByProjectId(projectId.intValue(), Integer.valueOf(WithdrawStatusEnum.WITHDRAW_NEED.getKey()));
                            }
                        }
                    }
                }
                //认购结束（项目方挂卖单）
                if (ProjectStatusEnum.END_SALE.getKey().equals(project.getStatus())) {
                    //项目开始时间大于当前时间
                    if(project.getStartTime().after(new Date())) {
                        // 项目开始时间  >  当前时间 - 认购天数
                        if (project.getStartTime().after(nowDate)) {
                            //保持认购结束状态
                        } else {
                            // 项目方挂买单（项目方回购所有的项目币）
                            projectThirdOrderBuy(project);
                        }
                    }
                }

            }
        }
    }


    /**
     * 认购结束，项目方挂卖单
     */
    private void projectThirdOrderSell(Project project) {
        OrderDto orderDto = new OrderDto();
        orderDto.setSide(BuyAndSellEnum.SELL.getKey());
        // 这个是创建项目的时候，填写此项目需要多少项目币
        orderDto.setAmount(project.getCollectCoinAmount());
        orderDto.setPrice(project.getInitPrice());
        String requestData = gson.toJson(orderDto);
        Symbol symbol = symbolMapper.selectByPrimaryKey(project.getSymbol().intValue());
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        Request request = new Request.Builder()
                .url(exchangeApiProperties.getHost() + exchangeApiProperties.getOrder() + symbol.getName())
                .post(RequestBody.create(mediaType, requestData))
                .header("Authorization", "Bearer" + " " + JWTUtil.encode(project.getUserId().toString()))//项目方用户
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        String resData = "";
        try {
            resData = okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            // 挂该项目的所有卖单失败！(网络原因)
            log.error("network question: project third, side: sell, projectId:"+ project.getId() +  "ordered failed." + sdf.format(new Date()));
        }
        ExchangeApiResDto exchangeApiResDto = gson.fromJson(resData, ExchangeApiResDto.class);
        if (exchangeApiResDto.getId() == null) {
            // 挂该项目的所有卖单失败！(业务原因)
            log.error("business question: project third, side: sell, projectId:"+ project.getId() +  "ordered failed." + sdf.format(new Date()));
        } else {
            //认购结束
            project.setStatus(ProjectStatusEnum.END_SALE.getKey());
            projectMapper.updateByPrimaryKeySelective(project);
            symbolMapper.updateValid(project.getSymbol().intValue(), true);
        }
    }

    /**
     * 项目方挂单（买单）
     */
    private void projectThirdOrderBuy(Project project) {
        OrderDto orderDto = new OrderDto();
        orderDto.setSide(BuyAndSellEnum.BUY.getKey());
        // 这个是创建项目的时候，填写此项目需要多少项目币
        orderDto.setAmount(project.getCollectCoinAmount());
        orderDto.setPrice(project.getInitPrice());
        String requestData = gson.toJson(orderDto);
        Symbol symbol = symbolMapper.selectByPrimaryKey(project.getSymbol().intValue());
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        Request request = new Request.Builder()
                .url(exchangeApiProperties.getHost() + exchangeApiProperties.getOrder() + symbol.getName())
                .post(RequestBody.create(mediaType, requestData))
                .header("Authorization", "Bearer" + " " + JWTUtil.encode(project.getUserId().toString()))//项目方用户
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        String resData = "";
        try {
            resData = okHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            // 挂该项目的所有买单失败！(网络原因)
            log.error("network question: project third, side: buy, projectId:"+ project.getId() +  "ordered failed." + sdf.format(new Date()));
        }
        ExchangeApiResDto exchangeApiResDto = gson.fromJson(resData, ExchangeApiResDto.class);
        if (exchangeApiResDto.getId() == null) {
            // 挂该项目的所有买单失败！(业务原因)
            log.error("business question: project third, side: buy, projectId:"+ project.getId() +  "ordered failed." + sdf.format(new Date()));
        } else {
            // 项目结束
            project.setStatus(ProjectStatusEnum.PROJECT_END.getKey());
            projectMapper.updateByPrimaryKey(project);
            //币对失效（对于交易系统的挂单无任何影响）
            symbolMapper.updateValid(project.getSymbol().intValue(), false);
        }
    }



    /**
     * 创新项目（一天执行一次）
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void creation() {
        // 发布前不用考虑
        // 发布后
        //a,考虑预售中，然后可能去修改状态。
        //b,考虑认购中，可能修改成认购结束，或者项目结束。
        //c,考虑认购结束，可能修改成项目结束。
        while (true) {

        }
    }

}
