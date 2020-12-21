package com.security.demo.configuration.token;

import com.security.demo.configuration.token.handle.JwtAccessDeinedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Autowired
    private BasicAuthenticationFilter basicAuthenticationFilter;

    @Autowired
    private AuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        // 由于使用的是JWT，我们这里不需要csrf
        httpSecurity.csrf().disable()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                // 对于获取token的rest api要允许匿名访问
                .antMatchers("/login/**").permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated();

        // 添加JWT filter
        httpSecurity.addFilter(basicAuthenticationFilter).exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint).accessDeniedHandler(new JwtAccessDeinedHandler());
        // 禁用缓存
        httpSecurity.headers().cacheControl();
    }

    @Bean
    public BasicAuthenticationFilter basicAuthenticationFilter(AuthenticationManager jWTAuthenticationManager,
                                                               AuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        return new BasicAuthenticationFilter(jWTAuthenticationManager, jwtAuthenticationEntryPoint);
    }
}
