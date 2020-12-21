package com.security.demo.controller;

import com.security.demo.configuration.security.MD5SaltPwdEncoder;
import com.security.demo.entity.User;
import com.security.demo.service.UserService;
import com.security.demo.util.JwtUtil;
import com.security.demo.util.Md5Util;
import com.security.demo.vo.BaseResponseVO;
import com.security.demo.vo.ResponseStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Resource
    private UserService userService;

    /**
     * 实际开发使用post
     * @return
     */
    @GetMapping("/user")
    public BaseResponseVO login(String userName,String passWord){
        List<User> users = userService.selectByUserName(userName);
        if (CollectionUtils.isEmpty(users)) {
            return BaseResponseVO.build().status(ResponseStatus.USER_ACCOUNT_NOT_EXIST);
        }
        User user = users.get(0);
        MD5SaltPwdEncoder.encodeS(passWord);
        boolean pass = user.getPassword().equals(MD5SaltPwdEncoder.encodeS(passWord));
        if(!pass){
            return BaseResponseVO.build().status(ResponseStatus.USER_CREDENTIALS_ERROR);
        }
        String token = JwtUtil.createToken(user.getId(), 7);
        return BaseResponseVO.success(token);
    }
}
