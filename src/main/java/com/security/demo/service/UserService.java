package com.security.demo.service;

import com.security.demo.entity.User;

import java.util.List;

public interface UserService {
    List<User> selectByUserName(String userName);

    User getById(Long id);
}
