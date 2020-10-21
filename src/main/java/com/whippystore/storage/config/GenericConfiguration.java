package com.whippystore.storage.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.whippystore.storage.customfilter.OAuth2AuthenticationFilter;
import com.whippystore.storage.service.LoadUserInfo;

@Configuration
public class GenericConfiguration {
  @Bean
  public OAuth2AuthenticationFilter oAuth2AuthenticationFilter() {
	  return new OAuth2AuthenticationFilter();
  }
  
  @Bean
  public LoadUserInfo loadUserInfo() {
	  return new LoadUserInfo();
  }
  
	
}
