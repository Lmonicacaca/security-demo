package com.security.demo.configuration.token.filter;

import com.alibaba.druid.util.StringUtils;
import com.security.demo.configuration.token.handle.JwtAuthenticationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 鉴权使用过滤器
 */
//@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    @Value("${jwt.header}")
    private String tokenHeader;

    /**
     * 使用我们自己开发的JWTAuthenticationManager
     *
     * @param authenticationManager JWTAuthenticationManager
     */
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws
            IOException, ServletException {
        String token = request.getHeader(tokenHeader);
        if (StringUtils.isEmpty(token)) {
            chain.doFilter(request, response);
            return;
        }
        Map<String, String[]> map = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            String mapKey = entry.getKey();
            String[] value = entry.getValue();
            for (String item : value) {
                if (item.equals("null")) {
                    String clientChannel = request.getHeader("Client-Channel");
                    logger.info("-------抛空异常地址" + request.getRequestURI() + "------参数名称" + mapKey + "------Client-Channel" + clientChannel);
                }
                if (item.equals("undefined")) {
                    String clientChannel = request.getHeader("Client-Channel");
                    logger.info("-------抛undefined异常地址" + request.getRequestURI() + "------参数名称" + mapKey + "------Client-Channel" + clientChannel);
                }
            }
        }
        try {
            JwtAuthenticationToken JwtToken = new JwtAuthenticationToken(token);
            // 鉴定权限，如果鉴定失败，AuthenticationManager会抛出异常被我们捕获
            Authentication authResult = getAuthenticationManager().authenticate(JwtToken);
            // 将鉴定成功后的Authentication写入SecurityContextHolder中供后序使用
            SecurityContextHolder.getContext().setAuthentication(authResult);
        } catch (AuthenticationException failed) {
            SecurityContextHolder.clearContext();
            // 返回鉴权失败
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, failed.getMessage());
            return;
        }
        chain.doFilter(request, response);
    }
}
