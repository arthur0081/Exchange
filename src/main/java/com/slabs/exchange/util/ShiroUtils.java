package com.slabs.exchange.util;

import com.slabs.exchange.model.dto.OauthInfoDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.util.ObjectUtils;


/**
 * 类ShiroUtils的功能描述:
 * Shiro工具类
 *
 * @auther joey
 * @date 2017-08-25 16:19:35
 */
public class ShiroUtils {

    /**
     * 加密算法
     */
    public final static String ALGORITHM_NAME = "SHA-256";
    /**
     * 加密散列次数
     */
    public static final int HASH_ITERATIONS = 1024;

    public static String encodeSalt(String password, String salt) {
        return new SimpleHash(ALGORITHM_NAME, password, salt, HASH_ITERATIONS).toString();
    }

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static OauthInfoDto getOauthInfoDto() {

        return (OauthInfoDto) SecurityUtils.getSubject().getPrincipal();
    }

    public static Integer getUserId() {
        OauthInfoDto oauthInfoDto = getOauthInfoDto();

        if (ObjectUtils.isEmpty(oauthInfoDto)) {
            return 1;
        } else {
            return getOauthInfoDto().getUserId();
        }
    }

    public static void setSessionAttribute(Object key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static Object getSessionAttribute(Object key) {
        return getSession().getAttribute(key);
    }

    public static boolean isLogin() {
        return SecurityUtils.getSubject().getPrincipal() != null;
    }

    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    public static String getKaptcha(String key) {
        String kaptcha = getSessionAttribute(key).toString();
        getSession().removeAttribute(key);
        return kaptcha;
    }

}
