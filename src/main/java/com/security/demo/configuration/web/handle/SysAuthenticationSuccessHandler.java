package com.security.demo.configuration.web.handle;

import com.alibaba.fastjson.JSON;
import com.security.demo.configuration.security.AuthUserDetail;
import com.security.demo.vo.BaseResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登陆成功处理逻辑
 **/
@Component
public class SysAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(SysAuthenticationSuccessHandler.class);
    private static final String CONTENT_TYPE_JSON = "text/json;charset=utf-8";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        AuthUserDetail user = (AuthUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        httpServletResponse.setContentType(CONTENT_TYPE_JSON);
        BaseResponseVO responseVO = BaseResponseVO.success("登录成功", user);
        httpServletResponse.getWriter().write(JSON.toJSONString(responseVO));
        logger.info(user.getUsername() + "登录成功！");
    }

}
