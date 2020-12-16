package com.security.demo.service.impl;

import com.security.demo.dao.UserMapper;
import com.security.demo.entity.User;
import com.security.demo.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> selectByUserName(String userName) {
        return userMapper.selectByUserName(userName);
    }
}
