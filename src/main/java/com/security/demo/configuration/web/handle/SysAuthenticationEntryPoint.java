package com.security.demo.configuration.web.handle;

import com.alibaba.fastjson.JSON;
import com.security.demo.vo.BaseResponseVO;
import com.security.demo.vo.ResponseStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 匿名用户访问无权限资源时得异常
 **/
@Component
public class SysAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        BaseResponseVO notLogin = BaseResponseVO.build().status(ResponseStatus.UNLOGIN);
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(notLogin));
    }
}
