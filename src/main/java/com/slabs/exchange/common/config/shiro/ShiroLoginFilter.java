package com.slabs.exchange.common.config.shiro;

import com.slabs.exchange.common.exception.ExchangeException;
import com.slabs.exchange.common.exception.RespCommMessageEnum;
import com.slabs.exchange.model.dto.ResponseDto;
import com.slabs.exchange.util.JWTUtil;
import com.slabs.exchange.util.JsonUtil;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShiroLoginFilter extends FormAuthenticationFilter {

    /**
     * 在访问controller前判断是否登录，返回json，不进行重定向。
     *
     * @param request
     * @param response
     * @return true-继续往下执行，false-该filter过滤器已经处理，不继续执行其他过滤器
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            response.setContentType("application/json;charset=UTF-8");
            ResponseDto responseDto = new ResponseDto(RespCommMessageEnum.AUTH_SESSION_ERROR.getErrorCode(), RespCommMessageEnum.AUTH_SESSION_ERROR.getErrorMsg());
            response.getWriter().print(JsonUtil.getJsonByObj(responseDto));
            return false;
    }

}
