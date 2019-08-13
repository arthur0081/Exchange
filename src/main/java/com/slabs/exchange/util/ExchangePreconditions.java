package com.slabs.exchange.util;

import com.slabs.exchange.common.exception.ExchangeException;
import com.slabs.exchange.common.exception.RespCommMessageEnum;
import com.slabs.exchange.common.exception.RespMessage;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author joey
 * @date 2018/2/4
 */
public class ExchangePreconditions {
    /**
     * @param expression   boolean 表达式
     * @param errorMessage
     */
    public static void isTrue(boolean expression, String errorMessage) {
        if (!expression) {
            throw new ExchangeException(errorMessage);
        }

    }

    /**
     * 断言对象不为空
     *
     * @param object       要判断的对象
     * @param errorMessage 错误信息
     * @param <T>
     * @return
     */
    public static <T> void notNull(T object, String errorMessage) {
        if (object == null) {
            throw new ExchangeException(errorMessage);
        }
    }

    /**
     * @param expression  boolean 表达式
     * @param respMessage
     */
    public static void isTrue(boolean expression, RespMessage respMessage) {
        if (!expression) {
            throw new ExchangeException(respMessage);
        }

    }

    /**
     * 断言对象不为空
     *
     * @param object      要判断的对象
     * @param respMessage 错误信息
     * @param <T>
     * @return
     */
    public static <T> void notNull(T object, RespMessage respMessage) {
        if (object == null) {
            throw new ExchangeException(respMessage);
        }
    }

    /**
     * 断言对象字段不为空
     *
     * @param object      要判断的对象
     * @param respMessage 错误信息
     * @param <T>
     */
    public static <T> void objFieldNotNull(T object, String respMessage) {
        if (object == null) {
            throw new ExchangeException(respMessage);
        }
        for (Field f : object.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
                if (null == f.get(object)) {
                    throw new ExchangeException(respMessage);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new ExchangeException(RespCommMessageEnum.CONNECTION_ERROR);
            }
        }

    }

    /**
     * 断言是否是手机号
     *
     * @param mobile
     * @return
     */
    public static <T> void isMobile(String mobile) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (mobile.length() != 11) {
            throw new ExchangeException(RespCommMessageEnum.PHONE_ERROR);

        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(mobile);
            boolean isMatch = m.matches();
            if (!isMatch) {
                throw new ExchangeException(RespCommMessageEnum.PHONE_ERROR);
            }
        }
    }

    /**
     * 断言是否是手机号
     *
     * @param idCard
     * @return
     */
    public static <T> void isID(String idCard) {

        boolean isID = IDCardUtil.isIDCard(idCard);
        if (!isID) {
            throw new ExchangeException(RespCommMessageEnum.IDCARD_ERROR);
        }
    }
}
