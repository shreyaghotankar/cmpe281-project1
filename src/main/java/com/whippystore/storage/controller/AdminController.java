package com.whippystore.storage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.whippystore.storage.dao.UserUploadFileRecord;
import com.whippystore.storage.dao.UserUploadFileRecordDao;
import com.whippystore.storage.dto.AllUserUploadFileRecordDTO;
import com.whippystore.storage.dto.UserUploadFileRecordDTO;

@Controller
public class AdminController {
	@Autowired
    private UserUploadFileRecordDao dao;

    @GetMapping("/admin")
    public String adminHomePage(Model model) {
        List<UserUploadFileRecord> records = dao.getAllRecords();

        Map<String, List<UserUploadFileRecordDTO>> map = new HashMap<>();
        for (UserUploadFileRecord record : records) {
            List<UserUploadFileRecordDTO> list;
            if (map.get(record.getUserName()) != null) {
                list = map.get(record.getUserName());
            } else {
                list = new ArrayList<>();
            }
            list.add(createDTO(record));
            map.put(record.getUserName(), list);
        }
        List<AllUserUploadFileRecordDTO> recordDTOS = new ArrayList<>();
        for (Map.Entry<String, List<UserUploadFileRecordDTO>> entry : map.entrySet()) {
            AllUserUploadFileRecordDTO dto = new AllUserUploadFileRecordDTO(entry.getKey(), entry.getValue());
            recordDTOS.add(dto);
           
        }
        model.addAttribute("users", recordDTOS);

        return "admin";
    }

    private UserUploadFileRecordDTO createDTO(UserUploadFileRecord record) {
        UserUploadFileRecordDTO dto = new UserUploadFileRecordDTO();

        dto.setFilename(record.getFilename());
        dto.setId(record.getId());
        dto.setDescription(record.getDescription());
        dto.setPath(record.getPath());
        dto.setCreatedTime(record.getCreatedTime());
        dto.setUpdatedTime(record.getUpdatedTime());
        dto.setFirstname(record.getFirstname());
        dto.setLastname(record.getLastname());
        dto.setUserName(record.getUserName());

        return dto;
    }
}


