package com.whippystore.storage.customfilter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;


import java.util.List;

public class OAuth2AuthenticationWrapperToken extends OAuth2AuthenticationToken {

    public OAuth2AuthenticationWrapperToken(OAuth2AuthenticationToken token, List<GrantedAuthority> authorityList) {
        super(token.getPrincipal(), authorityList, token.getAuthorizedClientRegistrationId());
    }
}
