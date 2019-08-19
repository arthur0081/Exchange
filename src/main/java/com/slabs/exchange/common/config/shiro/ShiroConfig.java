package com.slabs.exchange.common.config.shiro;


import com.oracle.xmlns.internal.webservices.jaxws_databinding.JavaWsdlMappingType;
import com.slabs.exchange.common.ExchangeConsts;
import com.slabs.exchange.common.config.redis.CachingShiroSessionDao;
import com.slabs.exchange.common.exception.ExchangeException;
import com.slabs.exchange.util.JWTUtil;
import com.slabs.exchange.util.ShiroUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import scala.reflect.internal.Trees;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 类ShiroConfig的功能描述:
 * Shiro配置
 * @auther hxy
 * @date 2017-11-15 21:50:12
 */
@Configuration
public class ShiroConfig {
    /**
     * cookie对象;
     * rememberMeCookie()方法是设置Cookie的生成模版，比如cookie的name，cookie的有效时间等等。
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie(){
        //System.out.println("ShiroConfiguration.rememberMeCookie()");
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }

    /**
     * cookie管理对象;
     * rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(){
        //System.out.println("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return cookieRememberMeManager;
    }
    @Bean("sessionManager")
    public SessionManager sessionManager(CachingShiroSessionDao sessionDAO){
        sessionDAO.setPrefix(ExchangeConsts.SESSION_OUATH_INFO + ":");
        //注意中央缓存有效时间要比本地缓存有效时间长
        sessionDAO.setSeconds(ExchangeConsts.LOGIN_EFFECTING_SECONDS);
       // DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        MySessionManager sessionManager = new MySessionManager();
        //设置session过期时间为24小时(单位：毫秒)，
        sessionManager.setGlobalSessionTimeout(ExchangeConsts.LOGIN_SESSION_TIMEOUT);
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionDAO(sessionDAO);
        return sessionManager;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(MyRealm myRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm);
        securityManager.setSessionManager(sessionManager);
       // securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {

        System.out.println("--------------------shiro filter-------------------");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put("authc", new ShiroLoginFilter());
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        //注意过滤器配置顺序 不能颠倒
        // 配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/static/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/v2/api-docs", "anon");
        filterChainDefinitionMap.put("/uncheck/**", "anon");
        filterChainDefinitionMap.put("/swagger*/**", "anon");
        filterChainDefinitionMap.put("/error/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/sql/druid/**", "anon");

        //公开接口
        filterChainDefinitionMap.put("/login", "anon");

        //拦截其他所有接口
       // filterChainDefinitionMap.put("/**", "authc");

        //配置shiro默认登录界面地址，前后端分离中登录界面跳转应由前端路由控制，后台仅返回json数据
       // shiroFilterFactoryBean.setLoginUrl("/exchange-api/login");
      //  shiroFilterFactoryBean.setSuccessUrl("/uncheck/success");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/uncheck/unauth");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;

    }
    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
     * @return
     */
//    @Bean
//    @DependsOn({"lifecycleBeanPostProcessor"})
//    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
//        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
//        advisorAutoProxyCreator.setProxyTargetClass(true);
//        return advisorAutoProxyCreator;
//    }

    /**
     * 保证实现了Shiro内部lifecycle函数的bean执行
     * @return
     */
//    @Bean("lifecycleBeanPostProcessor")
//    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
//        return new LifecycleBeanPostProcessor();
//    }

    /**
     * AOP式方法级权限检查
     * @return
     */
//    @Bean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
//        proxyCreator.setProxyTargetClass(true);
//        return proxyCreator;
//    }
//
//    @Bean
//    public SessionManager sessionManager() {
//        MySessionManager mySessionManager = new MySessionManager();
//        return mySessionManager;
//    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
    public class MySessionManager extends DefaultWebSessionManager {
        @Override
        protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
            HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
            String token = httpServletRequest.getParameter("token");
            String jwt = httpServletRequest.getHeader("jwt");
//            if (StringUtils.isEmpty(jwt)) {
//                //判断为登陆,不作任何处理
//            } else {
//                if (JWTUtil.decode(jwt) == null) {
//                    //JWT失效
//                    throw  new ExchangeException("token失效!");
//                }
//            }

            if(!StringUtils.isEmpty(token)){
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "token");
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, token);
                request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
                return token;
            }else{
                return super.getSessionId(request, response);
            }
//            if(CookieUtil.get(httpServletRequest,"JSESSIONID") != null){
//                System.out.println(CookieUtil.get(httpServletRequest,"JSESSIONID").getValue());
//            }
        }
    }



}
