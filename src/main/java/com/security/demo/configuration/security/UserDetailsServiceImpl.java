package com.security.demo.configuration.security;

import com.security.demo.configuration.security.AuthUserDetail;
import com.security.demo.entity.User;
import com.security.demo.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录成功后设置用户信息
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        List<User> users = userService.selectByUserName(userName);
        if(CollectionUtils.isEmpty(users)){
            throw new RuntimeException("用户不存在");
        }
        User user = users.get(0);
        AuthUserDetail authUserDetail = new AuthUserDetail(user.getUserName(),user.getPassword(),new ArrayList<>());
        return authUserDetail;
    }
}
