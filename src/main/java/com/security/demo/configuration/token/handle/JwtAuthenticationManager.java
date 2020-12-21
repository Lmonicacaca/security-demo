package com.security.demo.configuration.token.handle;

import com.alibaba.druid.util.StringUtils;
import com.security.demo.entity.User;
import com.security.demo.service.UserService;
import com.security.demo.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Component
public class JwtAuthenticationManager implements AuthenticationManager {

    @Resource
    private UserService userService;
    /**
     * 进行token鉴定
     * @param authentication 待鉴定的JWTAuthenticationToken
     * @return 鉴定完成的JWTAuthenticationToken，供Controller使用
     * @throws AuthenticationException 如果鉴定失败，抛出
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws
        AuthenticationException {
        String token = authentication.getCredentials().toString();
        String userId = JwtUtil.getAppUID(token);

        if (StringUtils.isEmpty(userId)) {
            throw new UsernameNotFoundException("该用户不存在");
        }
        User user = userService.getById(new Long(userId));
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("该用户不存在");
        }

        HttpServletRequest request =
            ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        JwtAuthenticationToken authenticatedAuth = new JwtAuthenticationToken(
            token, user, AuthorityUtils.commaSeparatedStringToAuthorityList(null)
        );
        return authenticatedAuth;
    }
}
