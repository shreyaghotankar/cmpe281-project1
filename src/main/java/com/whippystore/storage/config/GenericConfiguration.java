package com.whippystore.storage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.whippystore.storage.customfilter.OAuth2AuthenticationFilter;

@Configuration
public class GenericConfiguration {
  @Bean
  public OAuth2AuthenticationFilter oAuth2AuthenticationFilter() {
	  return new OAuth2AuthenticationFilter();
  }
	
}
