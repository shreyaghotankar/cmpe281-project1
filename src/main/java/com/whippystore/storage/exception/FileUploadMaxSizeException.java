package com.whippystore.storage.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import com.whippystore.storage.controller.HomeController;
import com.whippystore.storage.service.LoadUserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class FileUploadMaxSizeException {
	
	@Autowired
	private LoadUserInfo service;
	
	private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxSizeException(Authentication authentication) {

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.getModel().put("errorMessage", "File too large!");
        LOG.error("Error Message: File size should be less than 10MB");
        modelAndView.getModel().put("records",service.getUserInfo(((DefaultOidcUser) authentication.getPrincipal()).getEmail()));
        return modelAndView;
    }

}
