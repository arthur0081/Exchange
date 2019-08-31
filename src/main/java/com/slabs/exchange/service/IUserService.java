package com.slabs.exchange.service;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.*;

public interface IUserService {

    ResponseBean save(UserDto userDto);

    ResponseBean selectOneById(Integer userId);

    ResponseBean update(UserDto userDto);

    ResponseBean list(PageParamDto pageParamDto);

    ResponseBean register(UserDto userDto);

    ResponseBean resetLoginPassword();

    ResponseBean resetFundPassword();

    ResponseBean getMyInfo();

    OauthInfoDto login(UserDto userDto);

    ResponseBean identityUpdate(UserDto userDto);

    ResponseBean checkAccount(AccountCheckDto accountCheckDto);

    ResponseBean getProjectUsers();

    ResponseBean preUpdate();

    ResponseBean updateLoginPassword(UpdatePasswordDto updatePasswordDto);

    ResponseBean updateFundPassword(UpdatePasswordDto updatePasswordDto);

    ResponseBean identifyDetail(Integer userId);
}
