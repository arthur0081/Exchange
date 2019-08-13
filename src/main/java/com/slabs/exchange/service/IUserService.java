package com.slabs.exchange.service;

import com.slabs.exchange.model.common.ResponseBean;
import com.slabs.exchange.model.dto.OauthInfoDto;
import com.slabs.exchange.model.dto.PageParamDto;
import com.slabs.exchange.model.dto.UserDto;

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
}
