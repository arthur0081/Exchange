package com.slabs.exchange.service;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author joey
 * @date 2018/1/25
 */
public abstract class BaseService {
    @Autowired
    private Mapper mapper;

    /**
     * @param source
     * 源数据
     * @param targeClass
     * 将要转换类的class
     * @param <S>
     *     source
     * @param <T>
     *     target
     * @return
     */
    protected <S,T> T map (S source, Class<T> targeClass){
        Assert.notNull(source,"类型转换失败,源类型对象不能为空");
        return mapper.map(source,targeClass);
    }

    /**
     * @param sourceList
     * 源数据列表
     * @param targeClass
     * 将要转换类的class
     * @param <S>
     *     source
     * @param <T>
     *     target
     * @return
     */
    protected <S,T> List<T> map (List<S> sourceList, Class<T> targeClass){
        if(CollectionUtils.isEmpty(sourceList)){
            return new ArrayList<T>();
        }

        List<T> resultList =new ArrayList<T>();
        for (S source: sourceList) {
            resultList.add(mapper.map(source,targeClass));
        }

        return resultList;
    }
}
