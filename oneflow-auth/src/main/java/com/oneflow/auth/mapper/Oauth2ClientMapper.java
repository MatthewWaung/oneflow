package com.oneflow.auth.mapper;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface Oauth2ClientMapper {

    /**
     * 查询oauth_client_details表中的数据
     * @param clientId
     * @return
     */
    ClientDetails selectClientById(String clientId);
}
