package com.slabs.exchange.service.back.impl;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.service.BaseService;
import com.slabs.exchange.service.back.IBackIndexService;
import com.slabs.exchange.util.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BackIndexServiceImpl extends BaseService implements IBackIndexService {

    @Resource
    private RedisUtil redisUtil;
    /**
     * 后台首页展示信息逻辑
     */
    @Override
    public ResponseBean getBackIndexInfo() {
        //1.  注册用户数
        //2.  活跃用户数
        int activeCount = 0;
        try {
            activeCount = redisUtil.getActiveUserCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //3.  昨日新增用户数

        //4.  平台币涨幅（如果没有的话就是0）
        //5.  平台币市值（如果没有人认购项目的话，0）
        //6.  平台币最新价格（如果在交易没有取到的话，就取初始价格）

        //7.  24小时换手率（降序排列）

        //8.  持币用户数（降序排列）

        //9.  持币列表  （每个用户持币数量占总数的一个百分比）

        return new ResponseBean(200, "", activeCount);
    }


}
