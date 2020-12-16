package com.security.demo.configuration.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class AuthUserDetailUtil {
    public static AuthUserDetail fetchUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.nonNull(authentication)) {
            Object principal = authentication.getPrincipal();
            return principal instanceof AuthUserDetail? (AuthUserDetail)principal : null;
        }
        return null;
    }
}
