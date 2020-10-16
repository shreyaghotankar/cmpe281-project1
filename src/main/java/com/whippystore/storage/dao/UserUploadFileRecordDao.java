package com.whippystore.storage.dao;

import java.util.List;

public interface UserUploadFileRecordDao {

    void createOrUpdate(UserUploadFileRecord record);

    List<UserUploadFileRecord> getUserUploadRecordByEmail(String email);
    
    void deleteRecord(int id);
    
    UserUploadFileRecord findById(int id);

	List<UserUploadFileRecord> getAllRecords();
}
