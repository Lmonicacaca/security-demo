package com.security.demo.configuration.web;


import com.security.demo.configuration.security.MD5SaltPwdEncoder;
import com.security.demo.configuration.security.UserDetailsServiceImpl;
import com.security.demo.configuration.web.handle.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.annotation.Resource;

//@Configuration
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Resource
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @Resource
    private SysLogoutSuccessHandler sysLogoutSuccessHandler;
    @Resource
    private SysAuthenticationSuccessHandler sysAuthenticationSuccessHandler;
    @Resource
    private SysAuthenticationFailureHandler sysAuthenticationFailureHandler;
    @Resource
    private SysAccessDeniedHandler sysAccessDeniedHandler;
    @Resource
    private SysAuthenticationEntryPoint sysAuthenticationEntryPoint;
    @Resource
    private SysSessionInformationExpiredStrategy sysSessionInformationExpiredStrategy;


    /**
     * http安全配置，这里配置允许访问的url,登录页等
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.authorizeRequests().
                antMatchers("/index")
                .permitAll()
                .anyRequest()
                .authenticated().
                //登入
                and().formLogin().
                //默认的登录url是/login,这里可以根据需要修改，注意是POST请求，使用get会报404
                loginProcessingUrl("/api/login/account").
                //允许所有用户登录
                permitAll().
                //登录成功处理逻辑
                successHandler(sysAuthenticationSuccessHandler).
                //登录失败处理逻辑
                failureHandler(sysAuthenticationFailureHandler).
                //登出
                and().logout().
                //允许所有用户登出
                permitAll().
                //登出成功处理逻辑
                logoutSuccessHandler(sysLogoutSuccessHandler).
                //登出之后删除cookie
                deleteCookies("JSESSIONID").

                //异常处理(权限拒绝、登录失效等)
                and().exceptionHandling().
                //权限拒绝处理逻辑
                accessDeniedHandler(sysAccessDeniedHandler).
                //匿名用户访问无权限资源时的异常处理
                authenticationEntryPoint(sysAuthenticationEntryPoint).
                //会话管理
                and().sessionManagement().
                //同一账号同时登录最大用户数
                maximumSessions(1).
                //会话失效(账号被挤下线)处理逻辑
                expiredSessionStrategy(sysSessionInformationExpiredStrategy);


//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(this.userDetailsServiceImpl);
//        MD5SaltPwdEncoder pwdEncoder=new MD5SaltPwdEncoder();
//        provider.setPasswordEncoder(pwdEncoder);
//        http.authenticationProvider(provider);
    }

    @Bean
    public MD5SaltPwdEncoder passwordEncoder() {
        return new MD5SaltPwdEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl);
    }
}
