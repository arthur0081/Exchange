package com.slabs.exchange.common.config.shiro;


import com.slabs.exchange.common.enums.LoginTypeEnum;
import com.slabs.exchange.common.exception.ExchangeException;
import com.slabs.exchange.mapper.UserMapper;
import com.slabs.exchange.mapper.UserRoleMapper;
import com.slabs.exchange.model.dto.OauthInfoDto;
import com.slabs.exchange.model.entity.User;
import com.slabs.exchange.model.entity.UserRole;
import com.slabs.exchange.service.IRoleService;
import com.slabs.exchange.util.RedisUtil;
import com.slabs.exchange.util.ShiroUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 类的功能描述.
 * shiro 认证
 * @Auther hxy
 * @Date 2017/4/27
 */
@Component
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 授权
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        OauthInfoDto oauthInfoDto = (OauthInfoDto) principals.getPrimaryPrincipal();
        if(oauthInfoDto != null){
            //根据用户id查询该用户所有的角色,并加入到shiro的SimpleAuthorizationInfo
            List<String> permissions = roleService.queryNamesByRoleId(oauthInfoDto.getUserId());
            info.addStringPermissions(permissions);
        }
        return info;
    }

    /**
     * 认证方法
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException, ExchangeException {
        String accountLoginType = (String) token.getPrincipal();
        String account = accountLoginType.split("_")[0];
        String loginType = accountLoginType.split("_")[1];
        User userTemp = userMapper.queryByAccount(account);
        if(userTemp == null){
            throw new UnknownAccountException();
        }

        OauthInfoDto oauthInfoDto = new OauthInfoDto();
        List<UserRole> ur = userRoleMapper.selectByUserId(userTemp.getId());
        Integer roleId = ur.get(0).getRoleId();
        if (LoginTypeEnum.BACK.getKey().equals(loginType)) {
            // todo 后续对魔法数字进行优化
            if (roleId == 1) {//超级管理员放行
                //do nothing
            }
            if (roleId == 3 || roleId == 4) {//项目方或者普通用户
                throw new UnknownAccountException();
            }
        }

        if (LoginTypeEnum.FORE.getKey().equals(loginType)) {
            if (roleId == 1) {//超级管理员放行
                //do nothing
            }
            if (roleId == 2) {//项目方或者普通用户
                throw new UnknownAccountException();
            }
        }

        oauthInfoDto.setRoleId(roleId);
        oauthInfoDto.setAccount(userTemp.getAccount());
        oauthInfoDto.setUserId(userTemp.getId());
        oauthInfoDto.setAuditState(userTemp.getAuditState());

        List<String> permissions = this.roleService.queryNamesByRoleId(roleId);
        //加入权限
        oauthInfoDto.setPermissions(permissions);
        SimpleAuthenticationInfo sainfo = new SimpleAuthenticationInfo(
                oauthInfoDto,
                userTemp.getPassword(),
                ByteSource.Util.bytes(userTemp.getSalt()),
                getName());
        return sainfo;
    }

    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
        shaCredentialsMatcher.setHashAlgorithmName(ShiroUtils.ALGORITHM_NAME);
        shaCredentialsMatcher.setHashIterations(ShiroUtils.HASH_ITERATIONS);
        super.setCredentialsMatcher(shaCredentialsMatcher);
    }

}
