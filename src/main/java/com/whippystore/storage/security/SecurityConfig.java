package com.whippystore.storage.security;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;

import com.whippystore.storage.customfilter.OAuth2AuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) 
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler logoutHandler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        logoutHandler.setPostLogoutRedirectUri(URI.create("http://localhost:5000/"));
        return logoutHandler;
    }

    @Autowired
    private OAuth2AuthenticationFilter oAuth2AuthenticationFilter;
    
    
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
            .and()
            .authorizeRequests(authz -> authz.mvcMatchers("/")
         //   .authorizeRequests().antMatchers("/**")
            	.permitAll()
                .anyRequest()
                .authenticated())
            .addFilterAfter(oAuth2AuthenticationFilter, OAuth2LoginAuthenticationFilter.class)
            .oauth2Login()
            .and()
            .logout()
            .logoutSuccessUrl("/")
            .and().logout().logoutSuccessHandler(oidcLogoutSuccessHandler());
    }
}