package com.oneflow.auth.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.util.StringUtils;

import java.util.*;

public class OauthClient implements ClientDetails {

    /**
     * 字段见 oauth_client_details 表，以下字段复制自 BaseClientDetails.java
     */

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("client_secret")
    private String clientSecret;

    @JsonProperty("scope")
    private Set<String> scope;

    @JsonProperty("resource_ids")
    private Set<String> resourceIds;

    @JsonProperty("authorized_grant_types")
    private Set<String> authorizedGrantTypes;

    @JsonProperty("redirect_uri")
    private Set<String> registeredRedirectUris;

    @JsonProperty("autoapprove")
    private Set<String> autoApproveScopes;
    private List<GrantedAuthority> authorities;

    @JsonProperty("access_token_validity")
    private Integer accessTokenValidity;

    @JsonProperty("refresh_token_validity")
    private Integer refreshTokenValidity;

    // 参考 BaseClientDetails 六个参数的构造方法
    public OauthClient(String clientId, String resourceIds, String scopes, String grantTypes, String authorities, String redirectUris) {
        this.scope = Collections.emptySet();
        this.resourceIds = Collections.emptySet();
        this.authorizedGrantTypes = Collections.emptySet();
        this.authorities = Collections.emptyList();
//        this.additionalInformation = new LinkedHashMap();
        this.clientId = clientId;
        Set scopeList;
        if (StringUtils.hasText(resourceIds)) {
            scopeList = StringUtils.commaDelimitedListToSet(resourceIds);
            if (!scopeList.isEmpty()) {
                this.resourceIds = scopeList;
            }
        }

        if (StringUtils.hasText(scopes)) {
            scopeList = StringUtils.commaDelimitedListToSet(scopes);
            if (!scopeList.isEmpty()) {
                this.scope = scopeList;
            }
        }

        if (StringUtils.hasText(grantTypes)) {
            this.authorizedGrantTypes = StringUtils.commaDelimitedListToSet(grantTypes);
        } else {
            this.authorizedGrantTypes = new HashSet(Arrays.asList("authorization_code", "refresh_token"));
        }

        if (StringUtils.hasText(authorities)) {
            this.authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
        }

        if (StringUtils.hasText(redirectUris)) {
            this.registeredRedirectUris = StringUtils.commaDelimitedListToSet(redirectUris);
        }

    }

    public OauthClient(String clientId, String clientSecret, Set<String> scope, Set<String> resourceIds, Set<String> authorizedGrantTypes, Set<String> registeredRedirectUris, Set<String> autoApproveScopes, List<GrantedAuthority> authorities, Integer accessTokenValidity, Integer refreshTokenValidity) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.scope = scope;
        this.resourceIds = resourceIds;
        this.authorizedGrantTypes = authorizedGrantTypes;
        this.registeredRedirectUris = registeredRedirectUris;
        this.autoApproveScopes = autoApproveScopes;
        this.authorities = authorities;
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }

    @Override
    public Set<String> getResourceIds() {
        return this.resourceIds;
    }

    @Override
    public boolean isSecretRequired() {
        return true;
    }

    @Override
    public String getClientSecret() {
        return this.clientSecret;
    }

    @Override
    public boolean isScoped() {
        return true;
    }

    @Override
    public Set<String> getScope() {
        return this.scope;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return this.authorizedGrantTypes;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return this.registeredRedirectUris;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return this.accessTokenValidity;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return this.refreshTokenValidity;
    }

    @Override
    public boolean isAutoApprove(String s) {
        return true;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return null;
    }
}
