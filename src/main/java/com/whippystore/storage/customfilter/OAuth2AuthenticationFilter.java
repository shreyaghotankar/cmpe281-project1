package com.whippystore.storage.customfilter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.servlet.FilterChain;
import javax.servlet.GenericFilter;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OAuth2AuthenticationFilter extends GenericFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication;

        try {
            authentication = SecurityContextHolder.getContext().getAuthentication();
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            if(authentication != null && (authentication instanceof OAuth2AuthenticationToken)) {
                grantedAuthorities.addAll(authentication.getAuthorities());
                
                if ("okta".equalsIgnoreCase(detectIdentityProvider(authentication))) {
                    if (hasOktaAdminRole(authentication) && !isAdminRoleAdded(authentication)) {
                        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    }
                } else {
                    if (hasCognitoAdminRole(authentication) && !isAdminRoleAdded(authentication)) {
                        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    }
                }
                
                OAuth2AuthenticationWrapperToken token = new OAuth2AuthenticationWrapperToken((OAuth2AuthenticationToken) authentication, grantedAuthorities);
                SecurityContextHolder.getContext().setAuthentication(token);
            }

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
    

    private boolean hasOktaAdminRole(Authentication authentication) {
        if (((OAuth2User)authentication.getPrincipal()).getAttribute("groups") != null) {
            return ((OAuth2User)authentication.getPrincipal()).getAttribute("groups").toString().contains("Admin");
        } else {
            return false;
        }
    }

    private boolean hasCognitoAdminRole(Authentication authentication) {
        if (((OAuth2User)authentication.getPrincipal()).getAttribute("cognito:roles") != null) {
            return ((OAuth2User)authentication.getPrincipal()).getAttribute("cognito:roles").toString().contains("admin");
        } else {
            return false;
        }
    }

    private boolean isAdminRoleAdded(Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equalsIgnoreCase(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    private String detectIdentityProvider(Authentication authentication) {
        if ("okta".equalsIgnoreCase(((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId())) {
            return "okta";
        } else {
            return "cognito";
        }
    }
    
}
