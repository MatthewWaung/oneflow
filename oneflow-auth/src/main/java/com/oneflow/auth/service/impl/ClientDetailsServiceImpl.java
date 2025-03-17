package com.oneflow.auth.service.impl;

import com.oneflow.auth.service.ClientDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 类似于 UserDetailsService
 */
@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        // 使用BaseClientDetails见：https://blog.csdn.net/u012153127/article/details/118443204
        BaseClientDetails clientDetails = new BaseClientDetails(clientId, "resource_id",
                "all,select", "authorization_code,client_credentials,refresh_token,password",
                "aut", "http://www.baidu.com");
        clientDetails.setClientSecret(passwordEncoder.encode("secret_" + clientId));
        return clientDetails;

        // 也可使用自定义的OauthClient实现ClientDetails，参考：https://blog.csdn.net/weixin_33800463/article/details/92533739
//        OauthClient oauthClient = new OauthClient(clientId, "resource_id",
//                "all,select", "authorization_code,client_credentials,refresh_token,password",
//                "aut", "http://www.baidu.com");
//        oauthClient.setClientSecret(passwordEncoder.encode("secret_" + clientId));
//        return oauthClient;
    }
}
