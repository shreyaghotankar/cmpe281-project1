package com.whippystore.storage.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import com.whippystore.storage.dao.UserUploadFileRecord;
import com.whippystore.storage.dao.UserUploadFileRecordDao;
import com.whippystore.storage.dto.UserUploadFileRecordDTO;

public class LoadUserInfo {

	@Autowired
	private UserUploadFileRecordDao dao;
	
	public List<UserUploadFileRecordDTO> getUserInfo(String userName){
		List<UserUploadFileRecord> records = dao.getUserUploadRecordByEmail(userName);
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

        return dtos; 
	}
	
}
