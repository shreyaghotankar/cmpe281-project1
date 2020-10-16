package com.whippystore.storage.controller;

import com.whippystore.storage.dao.UserUploadFileRecord;
import com.whippystore.storage.dao.UserUploadFileRecordDaoImpl;
import com.whippystore.storage.dto.UserUploadFileRecordDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserUploadFileRecordDaoImpl dao;

    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @GetMapping
    public String getHomePage(Model model, Authentication authentication) {
        LOG.info("Welcome here !!!");
        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                model.addAttribute("secretMessage", "Admin message is s3crEt");
            } else {
                List<UserUploadFileRecord> records = dao.getUserUploadRecordByEmail( ((DefaultOidcUser) authentication.getPrincipal()).getEmail());
                List<UserUploadFileRecordDTO> dtos = new ArrayList<>();
             if(records != null) {
                for (UserUploadFileRecord record :  records) {
                    UserUploadFileRecordDTO dto = new UserUploadFileRecordDTO();

                    dto.setFilename(record.getFilename());
                    dto.setId(record.getId());
                    dto.setDescription(record.getDescription());
                    dto.setFirstname(record.getFirstname());
                    dto.setLastname(record.getLastname());
                    dto.setCreatedTime(record.getCreatedTime());
                    dto.setUpdatedTime(record.getUpdatedTime());
                    dto.setPath(record.getPath());
                    dtos.add(dto);
                }
             }

                model.addAttribute("records", dtos);
            }
        }

        return "home";
    }

}
