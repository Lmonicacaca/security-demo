package com.security.demo.configuration.token.handle;

import com.alibaba.fastjson.JSONObject;
import com.security.demo.vo.BaseResponseVO;
import com.security.demo.vo.ResponseStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JwtAccessDeinedHandler
 *
 * @author lizhihui
 * @version 1.0.0
 * @date 2019/7/29 11:35
 */
public class JwtAccessDeinedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/javascript;charset=utf-8");
        response.setStatus(401);
        response.getWriter().print(JSONObject.toJSONString(BaseResponseVO.build().status(ResponseStatus.ACL_REFUSE)));
    }

}
