package com.whippystore.storage.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class UserUploadFileRecordDTO implements Serializable {

    private static final long serialVersionUID = -1;
    private int id;
    private String filename;
    private String path;
    private String description;
    private String fileSize;
    private Timestamp createdTime;
    private Timestamp updatedTime;
    private String firstname;
    private String lastname;

    private String userName;

    public UserUploadFileRecordDTO() {
        super();
    }

    public UserUploadFileRecordDTO(int id, String filename, String path, String description, String fileSize, Timestamp createdTime, Timestamp updatedTime, String userName, String firstname, String lastname) {
        super();
        this.id =  id;
        this.filename = filename;
        this.path = path;
        this.description= description;
        this.fileSize = fileSize;
        this.createdTime=createdTime;
        this.updatedTime = updatedTime;
        this.userName = userName;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
   
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

}
