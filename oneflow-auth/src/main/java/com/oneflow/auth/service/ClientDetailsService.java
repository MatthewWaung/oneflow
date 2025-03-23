package com.oneflow.auth.service;

import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

public interface ClientDetailsService {

    ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException;
}
