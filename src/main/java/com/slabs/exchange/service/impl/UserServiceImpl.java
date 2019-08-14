package com.slabs.exchange.service.impl;

import com.slabs.exchange.mapper.RoleMapper;
import com.slabs.exchange.mapper.UserMapper;
import com.slabs.exchange.mapper.UserRoleMapper;
import com.slabs.exchange.mapper.ext.UserExtMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.OauthInfoDto;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.dto.UserDto;
import com.slabs.exchange.model.dto.UserListDto;
import com.slabs.exchange.model.entity.Role;
import com.slabs.exchange.model.entity.User;
import com.slabs.exchange.model.entity.UserRole;
import com.slabs.exchange.service.BaseService;
import com.slabs.exchange.service.IUserService;
import com.slabs.exchange.util.JWTUtil;
import com.slabs.exchange.util.Sha256;
import com.slabs.exchange.util.ShiroUtils;
import okhttp3.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Service
public class UserServiceImpl extends BaseService implements IUserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserExtMapper userExtMapper;


    /**
     * 新增用户
     */
    @Override
    public ResponseBean save(UserDto userDto) {
        // 插入用户基础信息
        User user = map(userDto, User.class);
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setPassword(ShiroUtils.encodeSalt("12345678", salt));
        user.setFundPassword(ShiroUtils.encodeSalt("12345678", salt));
        // 新增用户时间
        user.setRegTime(new Date());
        user.setSalt(salt);
        // 钱包地址
        //TODO 调用钱包api

        user.setWalletAddr("QOdsfsQWdfREHIsdfsafWEHFIDHFdfjdkjgasdjkl=ad");
        // 当前登陆用户
        userMapper.insert(user);

        // 系统给定默认角色为普通用户（4）
        List<UserRole> userRoleList = new ArrayList<>();
        UserRole ur = new UserRole();
        ur.setUserId(user.getId());
        ur.setRoleId(4);
        userRoleList.add(ur);
        // 批量插入用户角色对应关系信息
        userRoleMapper.batchInsert(userRoleList);
        return new ResponseBean(200, "", null);
    }

    /**
     * 构建用户和角色对应关系
     */
    private List<UserRole> buildUserRoles(UserDto userDto, User user) {
        List<Integer> roleList = userDto.getRoleList();
        List<UserRole> userRoleList = new ArrayList<>();
        for (Integer in: roleList) {
            UserRole userRole = new UserRole();
            // 得到刚插入的用户的自增id
            userRole.setUserId(user.getId());
            userRole.setRoleId(in);
            userRoleList.add(userRole);
        }
        return userRoleList;
    }

    /**
     * 预更新
     */
    @Override
    public ResponseBean selectOneById(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        List<UserRole> userRoles = userRoleMapper.selectByUserId(userId);
        //得到所有的角色
        List<Role> roleList = roleMapper.getRoles();

        Map<String, Object> data = new HashMap<>();
        data.put("userId", userId);
        data.put("account", user.getAccount());
        data.put("userRoles", userRoles);
        data.put("roleList", roleList);

        return new ResponseBean(200, "", data);
    }

    /**
     * 更新用户信息（前台用户）
     */
    @Override
    public ResponseBean update(UserDto userDto) {
        User user = map(userDto, User.class);
        // 构建用户角色对应关系
        List<UserRole> userRoleList = buildUserRoles(userDto, user);

        // 删除用户角色关系
        userRoleMapper.deleteByUserId(userDto.getId());

        // 批量插入用户角色对应关系信息
        userRoleMapper.batchInsert(userRoleList);

        return new ResponseBean(200, "", null);
    }



    /**
     * 分页查询
     */
    @Override
    public ResponseBean list(PageParamDto pageParamDto) {
        // 获取总数
        int total = userExtMapper.count();

        // 获取列表
        int start = (pageParamDto.getCurrentPage() - 1) * pageParamDto.getPageSize();
        pageParamDto.setStart(start);
        List<UserListDto> list = userExtMapper.list(pageParamDto);

        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("pageSize", pageParamDto.getPageSize());
        data.put("currentPage", pageParamDto.getCurrentPage());
        data.put("list", list);
        return new ResponseBean(200, "", data);
    }

    /**
     * 注册前台用户
     */
    @Override
    public ResponseBean register(UserDto userDto) {
        // 插入用户基础信息
        User user = map(userDto, User.class);

        user.setPassword(userDto.getPassword());
        user.setFundPassword(userDto.getFundPassword());

        // 注册时间
        user.setRegTime(new Date());
        // 钱包地址
        //TODO 调用钱包api

        user.setWalletAddr("QOdsfsQWdfREHIsdfsafWEHFIDHFdfjdkjgasdjkl=ad");
        user.setInvitationCode(null);
        userMapper.insert(user);

        // 给注册用户插入邀请码(唯一性)
        String invitationCode = Sha256.getSHA256(user.getId().toString()).substring(0, 8);
        user.setInvitationCode(invitationCode);
        userMapper.updateByPrimaryKeySelective(user);

        if (userDto.getInvitationCode() == null || userDto.getInvitationCode().equals("")) {
            // do nothing
        } else {
            // 给主动邀请的人发放 平台币，直接调用充值接口。
            // todo
            User user1 = userMapper.selectByInvitationCode(userDto.getInvitationCode());

            //对方ip地址
            MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
            String requestBody = "{}";
            Request request = new Request.Builder()
                    .url("充值接口ip地址")
                    .post(RequestBody.create(mediaType, requestBody))
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient();
            try {
                Response response = okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 构建用户角色对应关系
        List<UserRole> userRoleList = buildUserRoles(userDto, user);

        // 批量插入用户角色对应关系信息
        userRoleMapper.batchInsert(userRoleList);

        return new ResponseBean(200, "", null);
    }

    /**
     * 重置登陆密码（默认密码12345678）
     */
    @Override
    public ResponseBean resetLoginPassword() {
        Integer userId = ShiroUtils.getUserId().intValue();
        User user = new User();
        user.setId(userId);
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setPassword(ShiroUtils.encodeSalt("12345678", salt));
        user.setSalt(salt);
        userMapper.updateByPrimaryKeySelective(user);

        return new ResponseBean(200, "", null);
    }

    /**
     * 重置资金密码(默认密码12345678)
     */
    @Override
    public ResponseBean resetFundPassword() {
        Integer userId = ShiroUtils.getUserId().intValue();
        User user = new User();
        user.setId(userId);
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setFundPassword(ShiroUtils.encodeSalt("12345678", salt));
        user.setSalt(salt);
        userMapper.updateByPrimaryKeySelective(user);
        return new ResponseBean(200, "", null);
    }

    /**
     * 得到我的基本信息
     */
    @Override
    public ResponseBean getMyInfo() {
        Integer userId = ShiroUtils.getUserId().intValue();
        User user = userMapper.selectByPrimaryKey(userId);
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setAccount(user.getAccount());
        userDto.setEmail(user.getEmail());
        return new ResponseBean(200, "", userDto);
    }

    /**
     * 登陆
     */
    @Override
    public OauthInfoDto login(UserDto userDto) {
        Subject subject = ShiroUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userDto.getAccount(), userDto.getPassword());
        subject.login(token);
        OauthInfoDto oauthInfoDto = ShiroUtils.getOauthInfoDto();
        oauthInfoDto.setToken(JWTUtil.encode(ShiroUtils.getUserId().toString()));
        return oauthInfoDto;
    }

}
