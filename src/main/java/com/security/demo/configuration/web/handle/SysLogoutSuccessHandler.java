package com.security.demo.configuration.web.handle;

import com.alibaba.fastjson.JSON;
import com.security.demo.common.SecurityConstants;
import com.security.demo.vo.BaseResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 登出成功处理逻辑
 **/
@Component
@Slf4j
public class SysLogoutSuccessHandler implements LogoutSuccessHandler {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        BaseResponseVO success = BaseResponseVO.success("登出成功");
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(success));
        deleteLoginFlag(httpServletRequest);
    }
    /**
     * 设置登录成功标识
     *
     * @param httpServletRequest
     */
    private void deleteLoginFlag(HttpServletRequest httpServletRequest) {
        String seesionId = httpServletRequest.getSession().getId();
        /**
         * 先设置拦截，后续根据cookie id完善一下。不然seesion失效的时候会有问题
         */
        stringRedisTemplate.delete(SecurityConstants.SAFE_SUPERVISION_LOGIN + seesionId);
    }
}
