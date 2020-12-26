package com.security.demo.dao;

import com.security.demo.common.TkMapper;
import com.security.demo.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends TkMapper<User> {

    List<User> selectByUserName(@Param("userName") String userName);

    User selectById(@Param("id") Long id);
}
