package com.oneflow.auth.config;

import com.oneflow.auth.core.constant.AuthConstants;
import com.oneflow.auth.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 授权服务器配置：
 * 1、配置客户端
 * 2、配置Access_Token生成
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;


    // AuthorizationServerConﬁgurerAdapter要求配置以下几个类，这几个类是由Spring创建的独立的配置对象，它们
    // 会被Spring传入AuthorizationServerConﬁgurer中进行配置。
    // 以下三个方法Oauth框架已经实现，这里我们重写这三个方法是为了灵活使用自己的配置
    // AuthorizationServerSecurityConfigurer用来配置令牌端点的安全约束
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        super.configure(security);

        /**
         * 参考：https://blog.csdn.net/wangooo/article/details/113978198
         * AuthorizationServerSecurityConfigurer继承SecurityConfigurerAdapter.也就是一个 Spring Security安全配置提供给
         * AuthorizationServer去配置AuthorizationServer的端点（/oauth/****）的安全访问规则、过滤器Filter。
         *
         *  配置：安全检查流程,用来配置令牌端点（Token Endpoint）的安全与权限访问
         *  默认过滤器：BasicAuthenticationFilter
         *  1、oauth_client_details表中clientSecret字段加密【ClientDetails属性secret】
         *  2、CheckEndpoint类的接口 oauth/check_token 无需经过过滤器过滤，默认值：denyAll()
         * 对以下的几个端点进行权限配置：
         * /oauth/authorize：授权端点
         * /oauth/token：令牌端点
         * /oauth/confirm_access：用户确认授权提交端点
         * /oauth/error：授权服务错误信息端点
         * /oauth/check_token：用于资源服务访问的令牌解析端点
         * /oauth/token_key：提供公有密匙的端点，如果使用JWT令牌的话
         **/
        security.allowFormAuthenticationForClients()  //允许客户表单认证
                .passwordEncoder(new BCryptPasswordEncoder())  //设置oauth_client_details中的密码编码器
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
        //注意：/oauth/token
        //如果配置支持allowFormAuthenticationForClients的，且url中有client_id和client_secret的会走ClientCredentialsTokenEndpointFilter来保护
        //如果没有支持allowFormAuthenticationForClients或者有支持但是url中没有client_id和client_secret的，走basic auth认证（最简单的认证方式，不安全）
    }

    // 用来配置客户端详情服务（ClientDetailsService），客户端详情信息在这里进行初始化，
    // 你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息。
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        super.configure(clients);
        // 使用自定义的方法
        clients.withClientDetails(clientDetailsService);
    }

    // 用来配置令牌（token）的访问端点和令牌服务(token services)
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        super.configure(endpoints);
        /**
         * 参考：https://blog.csdn.net/wangooo/article/details/113978198
         * AuthorizationServerEndpointsConfigurer其实是一个装载类，装载Endpoints所有相关的类配置
         * （AuthorizationServer、TokenServices、TokenStore、ClientDetailsService、UserDetailsService）。
         *
         * 另参考：https://www.cnblogs.com/cjsblog/archive/2022/03/23/16040652.html
         */
        List tokenEnhancerList = new ArrayList<>();
        tokenEnhancerList.add(jwtTokenEnhancer());
        tokenEnhancerList.add(jwtAccessTokenConverter());
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(tokenEnhancerList);
        endpoints.authenticationManager(authenticationManager)
                .accessTokenConverter(jwtAccessTokenConverter())  // 配置JwtAccessToken转换器
                .tokenEnhancer(jwtTokenEnhancer());
    }


    /**
     * 参考：https://www.cnblogs.com/cjsblog/archive/2022/03/23/16040652.html
     * Token增强——用于生成自定义的jwtToken，而不是源码提供的uuid
     *
     * 采用自定义方式生成jwtToken的位置：DefaultTokenServices类中createAccessToken()方法
     * 中的this.accessTokenEnhancer.enhance(token, authentication)增强方法。
     */
    public TokenEnhancer jwtTokenEnhancer() {
        return new TokenEnhancer() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                LoginUser securityUser = (LoginUser) authentication.getPrincipal();
                Map<String, Object> additionalInformation = new HashMap<>();
                additionalInformation.put(AuthConstants.JWT_USER_ID_KEY, securityUser.getUserId());
                additionalInformation.put(AuthConstants.JWT_USER_NAME_KEY, securityUser.getUsername());
                additionalInformation.put(AuthConstants.JWT_DEPT_ID_KEY, securityUser.getDeptId());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
                return accessToken;
            }
        };
    }

    /**
     * 采用RSA加密算法对JWT进行签名
     */
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setKeyPair(keyPair());
        return jwtAccessTokenConverter;
    }

    /**
     * 密钥对
     */
    @Bean
    public KeyPair keyPair() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "123456".toCharArray());
        return keyStoreKeyFactory.getKeyPair("jwt", "123456".toCharArray());
    }

//    @Bean
//    public TokenKeyEndpoint tokenKeyEndpoint() {
//        return new TokenKeyEndpoint(jwtAccessTokenConverter());
//    }

}
