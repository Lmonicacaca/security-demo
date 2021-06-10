package com.security.demo.configuration.web;


import com.security.demo.configuration.security.MD5SaltPwdEncoder;
import com.security.demo.configuration.security.UserDetailsServiceImpl;
import com.security.demo.configuration.web.handle.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
@Primary
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private SysLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private SysAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private SysAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private SysSessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private SysAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private SysAuthenticationEntryPoint authenticationEntryPoint;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * 指定用户认证时，默认从哪里获取认证用户信息
         */
        auth.userDetailsService(userDetailsServiceImpl);
    }

    /**
     * Http安全配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         * 表单登录：使用默认的表单登录页面和登录端点/login进行登录
         * 退出登录：使用默认的退出登录端点/logout退出登录
         * 记住我：使用默认的“记住我”功能，把记住用户已登录的Token保存在内存里，记住30分钟
         * 权限：除了/toHome和/toUser之外的其它请求都要求用户已登录
         * 注意：Controller中也对URL配置了权限，如果WebSecurityConfig中和Controller中都对某文化URL配置了权限，则取较小的权限
         */
        http.cors().and().csrf().disable();

        http./*authorizeRequests().
                        withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setAccessDecisionManager(accessDecisionManager);//决策管理器
                        o.setSecurityMetadataSource(securityMetadataSource);//安全元数据源
                        return o;
                    }
                }).and()*/

                formLogin()
                .loginPage("/login")
                // .defaultSuccessUrl("/index", false)
                .loginProcessingUrl("/api/login/account")
                .permitAll()
                //登录成功处理逻辑
                .successHandler(authenticationSuccessHandler)
                //登录失败处理逻辑
                .failureHandler(authenticationFailureHandler)
                .and()
                .logout()
                .logoutUrl("/api/logout/out")
                //允许所有用户
                .permitAll()
                //登出成功处理逻辑
                .logoutSuccessHandler(logoutSuccessHandler)
                .invalidateHttpSession(true)
                //登出之后删除cookie
                .deleteCookies("SESSION")
                .and()
                .rememberMe()
                .tokenValiditySeconds(1800)
                .and()
                .authorizeRequests()
                .antMatchers("/index")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                //异常处理(权限拒绝、登录失效等)
                .exceptionHandling()
                //权限拒绝处理逻辑
                .accessDeniedHandler(accessDeniedHandler).
                //匿名用户访问无权限资源时的异常处理
                        authenticationEntryPoint(authenticationEntryPoint).
                //会话管理
                        and().sessionManagement().
                //同一账号同时登录最大用户数
                        maximumSessions(15).
                //会话失效(账号被挤下线)处理逻辑
                        expiredSessionStrategy(sessionInformationExpiredStrategy).and();

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        log.info("设置用户服务为:{}", this.userDetailsServiceImpl);
        provider.setUserDetailsService(this.userDetailsServiceImpl);
        MD5SaltPwdEncoder pwdEncoder=new MD5SaltPwdEncoder();
        provider.setPasswordEncoder(pwdEncoder);
        log.info("设置加密服务为:{}", pwdEncoder);
        http.authenticationProvider(provider);
        log.info("设置security完毕");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {

        //解决静态资源被SpringSecurity拦截的问题
        web.ignoring().antMatchers(
                "/webjars/**",
                "/resources/**",
                "/swagger-ui.html",
                "/demo/**",
                "/acidntHandle/**",
                "/swagger-resources/**",
                "/v2/api-docs",
                "/test/form",
                "/api/out/socialReportSafety/**",
                "/api/out/importProjectByExcel/**",
                "/api/out/importCorpByExcel/**",
                "/api/out/saveSpongeProBaseFromSafety/**",
                "/api/out/**",
                "/api/housing/**",
                "/api/industry/**")
                .antMatchers("/static/**", "/favicon.ico")
                //画像页免登
                .antMatchers("/api/risk/detail/getRadarList")
                .antMatchers("/api/risk/detail/getRiskInfoDetail")
        ;
        super.configure(web);
    }

    @Resource(name = "userDetailsServiceImpl")
    protected UserDetailsService userDetailsService;

}