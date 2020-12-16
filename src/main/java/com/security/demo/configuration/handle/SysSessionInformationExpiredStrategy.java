package com.security.demo.configuration.handle;

import com.alibaba.fastjson.JSON;
import com.security.demo.vo.BaseResponseVO;
import com.security.demo.vo.ResponseStatus;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 会话失效（账号被挤下线）处理逻辑
 **/
@Component
public class SysSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException, ServletException {
        BaseResponseVO responseVO = BaseResponseVO.build()
                .status(ResponseStatus.USER_ACCOUNT_USE_BY_OTHERS)
                ;
        HttpServletResponse httpServletResponse = sessionInformationExpiredEvent.getResponse();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(responseVO));
    }
}
