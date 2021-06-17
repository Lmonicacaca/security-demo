package com.security.demo.controller;

import com.nmpa.risk.entity.AuthUserDetail;
import com.nmpa.risk.entity.AuthUserDetailUtil;
import com.security.demo.vo.BaseResponseVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/getUserInfo")
    public BaseResponseVO getUserInfo(){
        AuthUserDetail authUserDetail = AuthUserDetailUtil.fetchUser();
        return BaseResponseVO.success(authUserDetail);
    }
}
