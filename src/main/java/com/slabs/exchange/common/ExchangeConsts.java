package com.slabs.exchange.common;

public class ExchangeConsts {
    /**
     * 登录超时时间 minute
     */
    public static final long LOGIN_EFFECTING_MINUTE = 24 * 60 * 30;
    /**
     * 登录超时时间 Seconds
     */
    public static final int LOGIN_EFFECTING_SECONDS = 30 * 24 * 60 * 60 ;
    /**
     * 登录SESSION超时时间 毫秒
     */
    public static final int LOGIN_SESSION_TIMEOUT = 24 * 60 * 60 * 1000;

    /**
     * token 属性名称
     */
    public static final String OAUTH_TOKEN_ID = "oauthTokenId";

    /**
     * session 属性名称
     */
    public static final String SESSION_OUATH_INFO ="SESSION_OUATH_INFO";

    /**
     * 文件路径
     */
    public static final String TMP_FILE_RELATIVE_PATH="/root/tmp";

}
