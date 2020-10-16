package com.whippystore.storage.dto;

import java.io.Serializable;
import java.util.List;

public class AllUserUploadFileRecordDTO implements Serializable {

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<UserUploadFileRecordDTO> getRecords() {
        return records;
    }

    public void setRecords(List<UserUploadFileRecordDTO> records) {
        this.records = records;
    }

    String userName;
    List<UserUploadFileRecordDTO> records;

    public AllUserUploadFileRecordDTO(String userName, List<UserUploadFileRecordDTO> records) {
        this.userName = userName;
        this.records = records;
    }
}