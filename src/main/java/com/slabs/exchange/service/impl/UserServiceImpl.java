package com.slabs.exchange.service.impl;

import com.slabs.exchange.common.config.ExchangeApiProperties;
import com.slabs.exchange.common.enums.CertificateEnum;
import com.slabs.exchange.common.enums.YNEnum;
import com.slabs.exchange.common.exception.ExchangeException;
import com.slabs.exchange.mapper.RoleMapper;
import com.slabs.exchange.mapper.UserMapper;
import com.slabs.exchange.mapper.UserRoleMapper;
import com.slabs.exchange.mapper.back.AttachFileMapper;
import com.slabs.exchange.mapper.ext.UserExtMapper;
import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.*;
import com.slabs.exchange.model.entity.AttachFile;
import com.slabs.exchange.model.entity.Role;
import com.slabs.exchange.model.entity.User;
import com.slabs.exchange.model.entity.UserRole;
import com.slabs.exchange.service.BaseService;
import com.slabs.exchange.service.IUserService;
import com.slabs.exchange.util.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Slf4j
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
    @Resource
    private AttachFileMapper attachFileMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ExchangeApiProperties exchangeApiProperties;

    /**
     * 新增用户
     */
    @Override
    public ResponseBean save(UserDto userDto) {
        // 插入用户基础信息
        User user = map(userDto, User.class);
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setSalt(salt);
        user.setPassword(ShiroUtils.encodeSalt("12345678", salt));
        user.setFundPassword(ShiroUtils.encodeSalt("12345678", salt));
        // 新增用户时间
        user.setRegTime(new Date());
        //TODO 调用钱包api

        user.setWalletAddr("QOdsfsQWdfREHIsdfsafWEHFIDHFdfjdkjgasdjkl=ad");
        // todo 当前登陆用户

        userMapper.insert(user);
        // 给用户生成一个邀请码
        String invitationCode = Sha256.getSHA256(user.getId().toString() + System.currentTimeMillis()).substring(0, 8);
        user.setInvitationCode(invitationCode);
        userMapper.updateByPrimaryKeySelective(user);

        List<UserRole> userRoleList = new ArrayList<>();
        UserRole ur = new UserRole();
        ur.setUserId(user.getId());
        ur.setRoleId(userDto.getRoleList().get(0));//目前用户角色只做一个
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
        String plainPassword = AESUtil.desEncrypt(userDto.getPassword());
        String plainFundPassword = AESUtil.desEncrypt(userDto.getFundPassword());
        String salt = RandomStringUtils.randomAlphanumeric(20);
        user.setSalt(salt);
        user.setPassword(ShiroUtils.encodeSalt(plainPassword, salt));
        user.setFundPassword(ShiroUtils.encodeSalt(plainFundPassword, salt));
        user.setRegTime(new Date());
        // 钱包地址
        //TODO 调用钱包api

        user.setWalletAddr("QOdsfsQWdfREHIsdfsafWEHFIDHFdfjdkjgasdjkl=ad");
        userMapper.insert(user);
        // 给注册用户插入邀请码(唯一性)
        String invitationCode = Sha256.getSHA256(user.getId().toString() + System.currentTimeMillis()).substring(0, 8);
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
                    .url(exchangeApiProperties.getHost() + exchangeApiProperties.getCharge())
                    .post(RequestBody.create(mediaType, requestBody))
                    .build();
            OkHttpClient okHttpClient = new OkHttpClient();
            String resData = "";
            try {
                resData = okHttpClient.newCall(request).execute().body().string();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("Register giving hos: user id is " + user1.getId() + " " + user1.getUsername() + " charging failed.");
            }
        }
        // 给一个默认角色
        List<Integer> roleList = new ArrayList<>();
        roleList.add(5);
        userDto.setRoleList(roleList);
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
        Integer userId = ShiroUtils.getUserId();
        User user = new User();
        user.setId(userId);
        User user1 = userMapper.selectByPrimaryKey(userId);
        user.setPassword(ShiroUtils.encodeSalt("12345678", user1.getSalt()));
        userMapper.updateByPrimaryKeySelective(user);

        return new ResponseBean(200, "", null);
    }

    /**
     * 重置资金密码(默认密码12345678)
     */
    @Override
    public ResponseBean resetFundPassword() {
        Integer userId = ShiroUtils.getUserId();
        User user = new User();
        user.setId(userId);
        User user1 = userMapper.selectByPrimaryKey(userId);
        user.setFundPassword(ShiroUtils.encodeSalt("12345678", user1.getSalt()));
        userMapper.updateByPrimaryKeySelective(user);

        return new ResponseBean(200, "", null);
    }

    /**
     * 得到我的基本信息
     */
    @Override
    public ResponseBean getMyInfo() {
        Integer userId = ShiroUtils.getUserId();
        User user = userMapper.selectByPrimaryKey(userId);
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setAccount(user.getAccount());
        userDto.setEmail(user.getEmail());
        userDto.setInvitationCode(user.getInvitationCode());
        return new ResponseBean(200, "", userDto);
    }

    /**
     * 登陆
     */
    @Override
    public OauthInfoDto login(UserDto userDto) {
        Subject subject = ShiroUtils.getSubject();
        String plain = AESUtil.desEncrypt(userDto.getPassword());
        UsernamePasswordToken token = new UsernamePasswordToken(userDto.getAccount(), plain);
        subject.login(token);
        OauthInfoDto oauthInfoDto = ShiroUtils.getOauthInfoDto();
        oauthInfoDto.setToken(JWTUtil.encode(ShiroUtils.getUserId().toString()));
        oauthInfoDto.setSessionId(ShiroUtils.getSession().getId().toString());

        //当用户登陆后，设置为当天的活跃用户
        try {
            redisUtil.setActiveUserCount(ShiroUtils.getUserId(), -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oauthInfoDto;
    }

    /**
     * 只添加身份认证信息
     */
    @Override
    public ResponseBean identityUpdate(UserDto userDto) {
        // 获取到用户的id
        Integer userId = ShiroUtils.getUserId();
        User user = map(userDto, User.class);
        user.setId(userId);
        userMapper.updateByPrimaryKeySelective(user);
        List<AttachFile> list = null;
        if (userDto.getCertificateType() == 2) {
            //构建护照信息
            list = buildPassportAttachFile(userId, userDto);
        } else {
            //构建身份证信息
            list = buildIdcardAttachFile(userId, userDto);
        }
        // 批量插入附件信息
        attachFileMapper.batchInsert(list);

        return new ResponseBean(200, "添加认证信息成功",null);
    }

    /**
     * 因为身份证分为正面和反面
     */
    private List<AttachFile> buildIdcardAttachFile(Integer userId, UserDto userDto) {
        List<AttachFile> attachFiles = new ArrayList<>();
        Map<String, List<AttachFileDto>> map = userDto.getAttachFileDtoMap();
        for(Map.Entry<String, List<AttachFileDto>> entry : map.entrySet()){
            String mapKey = entry.getKey();
            List<AttachFileDto> mapValue = entry.getValue();
            for (AttachFileDto afd: mapValue) {
                AttachFile af = new AttachFile();
                af.setRefId(userId);
                af.setType(mapKey);
                af.setFilePath(afd.getFilePath());
                af.setFileName(afd.getFileName());
                af.setIsDel(YNEnum.N.getKey());
                af.setCreateUser(userId.longValue());
                af.setCreateTime(new Date());
                attachFiles.add(af);
            }
        }

        return attachFiles;
    }

    /**
     *  构建用户护照证件
     */
    private List<AttachFile> buildPassportAttachFile(Integer userId, UserDto userDto) {
        List<AttachFileDto> list = userDto.getAttachFileList();
        List<AttachFile> attachFiles = new ArrayList<>();
        for (AttachFileDto afd: list) {
            AttachFile af = map(afd, AttachFile.class);
            af.setIsDel(YNEnum.N.getKey());
            af.setRefId(userId);
            af.setType(CertificateEnum.PASSPORT.getKey());
            af.setCreateUser(userId.longValue());
            af.setCreateTime(new Date());
            attachFiles.add(af);
        }
        return attachFiles;
    }


    /**
     * 用户账户不能重复
     */
    @Override
    public ResponseBean checkAccount(AccountCheckDto accountCheckDto) {
        User user = userMapper.checkAccount(accountCheckDto);
        if (user != null) {
            throw new ExchangeException("当前账户已被使用！");
        }
        return new ResponseBean(200,"", null);
    }

    /**
     * 得到所有的项目方用户
     */
    @Override
    public ResponseBean getProjectUsers() {
        List<User> list = userMapper.getProjectUsers();
        return new ResponseBean(200, "", list);
    }

    /**
     * 前台：身份认证的预修改（回显）
     */
    @Override
    public ResponseBean preUpdate() {
        Integer userId = ShiroUtils.getUserId();
        User user = userMapper.selectByPrimaryKey(userId);
        UserDto userDto = map(user, UserDto.class);
        Map<String, List<AttachFileDto>> idcardMap = new HashMap<>();
        if (ExchangePreconditions.objCheckIsNull(user)) {
            //do nothing
        } else {
            if (user.getCertificateType() == 1) {//idcard
                List<AttachFile>  idcardFront = attachFileMapper.selectByTypeAndRefId(CertificateEnum.IDCARD_FRONT.getKey(), userId.longValue());
                List<AttachFileDto> idcardFrontDtos = map(idcardFront, AttachFileDto.class);
                idcardMap.put(CertificateEnum.IDCARD_FRONT.getKey(), idcardFrontDtos);

                List<AttachFile>  idcardBack = attachFileMapper.selectByTypeAndRefId(CertificateEnum.IDCARD_BACK.getKey(), userId.longValue());
                List<AttachFileDto> idcardBackDtos = map(idcardBack, AttachFileDto.class);
                idcardMap.put(CertificateEnum.IDCARD_BACK.getKey(), idcardBackDtos);
                userDto.setAttachFileDtoMap(idcardMap);

            } else if(user.getCertificateType() == 2) {//passport
                List<AttachFile> attachFiles = attachFileMapper.selectByTypeAndRefId(CertificateEnum.PASSPORT.getKey(), userId.longValue());
                List<AttachFileDto> attachFileDtos = map(attachFiles, AttachFileDto.class);
                userDto.setAttachFileList(attachFileDtos);
            } else {
                //do nothing
            }
        }
        return new ResponseBean(200, "", userDto);
    }

    /**
     * 修改用户登陆密码
     */
    @Override
    public ResponseBean updateLoginPassword(UpdatePasswordDto updatePasswordDto) {
        String plainPassword = AESUtil.desEncrypt(updatePasswordDto.getLoginPassword());
        Integer userId = ShiroUtils.getUserId();
        User user = userMapper.selectByPrimaryKey(userId);
        if (!user.getPassword().equals(ShiroUtils.encodeSalt(plainPassword, user.getSalt()))) {
            throw new ExchangeException("原始登陆密码错误！");
        }
        String newPlainPassword = AESUtil.desEncrypt(updatePasswordDto.getNewLoginPassword());
        String newPassword = ShiroUtils.encodeSalt(newPlainPassword, user.getSalt());
        user.setPassword(newPassword);
        userMapper.updateByPrimaryKeySelective(user);
        return new ResponseBean(200, "修改登陆密码成功", null);
    }

    /**
     * 修改用户资金密码
     */
    @Override
    public ResponseBean updateFundPassword(UpdatePasswordDto updatePasswordDto) {
        String plainPassword = AESUtil.desEncrypt(updatePasswordDto.getFundPassword());
        Integer userId = ShiroUtils.getUserId();
        User user = userMapper.selectByPrimaryKey(userId);
        if (!user.getFundPassword().equals(ShiroUtils.encodeSalt(plainPassword, user.getSalt()))) {
            throw new ExchangeException("原始资金密码错误！");
        }
        String newPlainPassword = AESUtil.desEncrypt(updatePasswordDto.getNewFundPassword());
        String newPassword = ShiroUtils.encodeSalt(newPlainPassword, user.getSalt());
        user.setFundPassword(newPassword);
        userMapper.updateByPrimaryKeySelective(user);
        return new ResponseBean(200, "修改资金密码成功", null);
    }

}
