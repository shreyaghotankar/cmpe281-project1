package com.whippystore.storage.controller;


import com.whippystore.storage.service.LoadUserInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;




@Controller
public class HomeController {

    
    @Autowired
    private LoadUserInfo service;
    
    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);



    @GetMapping
    public String getHomePage(Model model, Authentication authentication) {
        
        if (authentication != null && authentication.isAuthenticated()) {

            model.addAttribute("records", service.getUserInfo(((DefaultOidcUser) authentication.getPrincipal()).getEmail())); 
            LOG.info("User info extracted");
           
        }

        return "home";
    }

}
