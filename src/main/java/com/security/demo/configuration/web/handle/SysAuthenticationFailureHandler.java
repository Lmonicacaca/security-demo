package com.security.demo.configuration.web.handle;

import com.alibaba.fastjson.JSON;
import com.security.demo.vo.BaseResponseVO;
import com.security.demo.vo.ResponseStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登陆失败处理逻辑
 **/
@Component
public class SysAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //返回json数据
        BaseResponseVO result = null;
        if (e instanceof AccountExpiredException) {
            //账号过期
            result = BaseResponseVO.build().status(ResponseStatus.ACCOUNT_EXPIRED);
        } else if (e instanceof BadCredentialsException) {
            //密码错误
            result = BaseResponseVO.build().status(ResponseStatus.USER_CREDENTIALS_ERROR);
        } else if (e instanceof CredentialsExpiredException) {
            //密码过期
            result = BaseResponseVO.build().status(ResponseStatus.USER_CREDENTIALS_EXPIRED);
        } else if (e instanceof DisabledException) {
            //账号不可用
            result = BaseResponseVO.build().status(ResponseStatus.USER_ACCOUNT_DISABLE);
        } else if (e instanceof LockedException) {
            //账号锁定
            result = BaseResponseVO.build().status(ResponseStatus.USER_ACCOUNT_LOCKED);
        } else if (e instanceof InternalAuthenticationServiceException) {
            //用户不存在
            result = BaseResponseVO.build().status(ResponseStatus.USER_ACCOUNT_NOT_EXIST);
        }else{
            //其他错误
            result = BaseResponseVO.build().status(ResponseStatus.ERROR);
        }
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }
}
