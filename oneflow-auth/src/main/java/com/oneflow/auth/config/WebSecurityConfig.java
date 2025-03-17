package com.oneflow.auth.config;

import com.oneflow.auth.security.config.security.AuthenticationEntryPointImpl;
import com.oneflow.auth.security.config.security.JwtAuthenticationTokenFilter;
import com.oneflow.auth.security.config.security.LogoutSuccessHandlerImpl;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * prePostEnabled = true：开启 @PreAuthorize 基于方法的权限注解（常用）
 * securedEnabled = true：开启 @Secured 基于角色权限的注解，参数以ROLE_为前缀，写在方法或类上
 * 参考：https://blog.csdn.net/shaixinxin/article/details/105960407
 */
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 每个系统都应该有自己的用户体系，需要自定义用户的登录认证和接口的认证控制，这里就需要对WebSecurityConfiguration进行配置，
     * 主要关注三个方法：configure(HttpSecurity http)，configure(AuthenticationManagerBuilder auth)和configure(WebSecurity)。
     * 这三个方法Security框架已经实现，这里我们重写这三个方法是为了灵活使用自己的配置
     * ————————————————
     * 原文链接：https://blog.csdn.net/lmchhh/article/details/121052116
     */

    /**
     * 自定义用户认证逻辑
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 认证失败处理类
     */
    @Autowired
    private AuthenticationEntryPointImpl unauthorizedHandler;

    /**
     * 权限失败处理器
     */
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    /**
     * 退出处理类
     */
    @Autowired
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    /**
     * token认证过滤器
     */
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    /**
     * 跨域过滤器
     */
    @Autowired
    private CorsFilter corsFilter;

    /**
     * 解决 无法直接注入 AuthenticationManager
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * anyRequest          |   匹配所有请求路径
     * access              |   SpringEl表达式结果为true时可以访问
     * anonymous           |   匿名可以访问
     * denyAll             |   用户不能访问
     * fullyAuthenticated  |   用户完全认证可以访问（非remember-me下自动登录）
     * hasAnyAuthority     |   如果有参数，参数表示权限，则其中任何一个权限可以访问
     * hasAnyRole          |   如果有参数，参数表示角色，则其中任何一个角色可以访问
     * hasAuthority        |   如果有参数，参数表示权限，则其权限可以访问
     * hasIpAddress        |   如果有参数，参数表示IP地址，如果用户IP和参数匹配，则可以访问
     * hasRole             |   如果有参数，参数表示角色，则其角色可以访问
     * rememberMe          |   允许通过remember-me登录的用户访问
     * permitAll           |   用户可以任意访问
     * authenticated       |   用户登录后可访问
     * antMatchers(String) |   匹配接口
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // **如果自己实现登录页面和登录接口，那么只需要关闭表单登录（.formLogin()）即可。**
                // 开启表单登录，访问未放行的任何接口都会跳转到登录接口(/login)进行认证，如果直接访问/login接口，则需要指定登录成功后跳转的页面或接口
//                .formLogin().successForwardUrl("/loginSuccess")
                // 自定义表单登录页和自定义登录接口
//                .loginPage("/login.html").loginProcessingUrl("/doLogin").and()
                // CSRF禁用，因为不使用session
                .csrf().disable()
                // 认证失败处理类
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 过滤请求
                .authorizeRequests()
                // 对于登录login 验证码captchaImage 允许匿名访问
                .antMatchers(
                        "/login", "/captchaImage", "/task/getDoneNodeApprover",
                        "/system/dingtalk/dingTalkScanLogin", "/fei-shu/getAppJSApiTicket", "/sso/auth", "/task/sapComplete", "/task/makeUpForDingTalkStatus/**", "/Beisen/**", "/actuator/**", "/task/bpm/**"
                ).anonymous()
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()
                .antMatchers("/system/config/**").permitAll()
                .antMatchers("/task/start").permitAll()
                .antMatchers("/processDefinition/**").permitAll()
                .antMatchers("/activitiHistory/**").permitAll()
                .antMatchers("/profile/**").permitAll()
                .antMatchers("/common/download**").anonymous()
                .antMatchers("/common/download/resource**").anonymous()
                .antMatchers("/swagger-ui.html").anonymous()
                .antMatchers("/swagger-resources/**").anonymous()
                .antMatchers("/webjars/**").anonymous()
                .antMatchers("/*/api-docs").anonymous()
                .antMatchers("/druid/**").anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable();
        httpSecurity.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
        // 添加JWT filter
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        // 添加CORS filter
        httpSecurity.addFilterBefore(corsFilter, JwtAuthenticationTokenFilter.class);
        httpSecurity.addFilterBefore(corsFilter, LogoutFilter.class);
    }


    /**
     * 强散列哈希加密实现
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 身份认证接口
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
        //测试可使用内存存储用户的方式，实际项目一般都是在数据库存储用户信息
//        auth.inMemoryAuthentication()   // 在内存存储用户信息
//                .withUser("lmc").password("{noop}123") // 用户名为lmc,密码为123，{noop}表示密码不加密
//                .roles("USER");  // 给用户lmc赋予USER角色
    }

    // 测试加密算法
    public static void main(String[] args) {
        // BCrypt每次加密的密文是不一样的，但校验时同样的密码不同的密文是可以校验成功的
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String psw1 = encoder.encode("123456");
        System.out.println("====>" + psw1); //$2a$10$yJvpy40eVzf5G3ELMDRUKecI9oehDS3UDLdrJgW8sWf7bY/AXIH7G
        System.out.println(encoder.matches("123456", psw1));
        String psw2 = encoder.encode("123456");
        System.out.println("====>" + psw2); //$2a$10$kHsAq8bTlffoSTQNIJfqNOg05IK8oz6mpBk470wUEKav7XV.jUpWS
        System.out.println(encoder.matches("123456", psw2));
    }
}
