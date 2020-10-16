package com.whippystore.storage.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.util.CollectionUtils;

//@Repository
@Transactional
public class UserUploadFileRecordDaoImpl implements UserUploadFileRecordDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void createOrUpdate(UserUploadFileRecord record) {
        sessionFactory.getCurrentSession().saveOrUpdate(record);
    }

    @Override
    public List<UserUploadFileRecord> getUserUploadRecordByEmail(String email) {
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from UserUploadFileRecord where userName = :userName ");
        query.setParameter("userName", email);


        List<UserUploadFileRecord> files = query.list();
        if (CollectionUtils.isNullOrEmpty(files)) {
            return null;
        }
        return files;
    }

	@Override
	public void deleteRecord(int id) {
		

        UserUploadFileRecord record = findById(id);
        
        sessionFactory.getCurrentSession().delete(record);
        
		
	}

	@Override
	public UserUploadFileRecord findById(int id) {
		Query query = sessionFactory.getCurrentSession()
                .createQuery("from UserUploadFileRecord where id = :id ");
        query.setParameter("id", id);
        
        UserUploadFileRecord record = (UserUploadFileRecord) query.getSingleResult();
        
		return record;
	}
	
	@Override
	public List<UserUploadFileRecord> getAllRecords() {
	    Query query = sessionFactory.getCurrentSession()
	            .createQuery("from UserUploadFileRecord ");

	    List<UserUploadFileRecord> files = query.list();
	    if (CollectionUtils.isNullOrEmpty(files)) {
	        return null;
	    }
	    return files;
	}
}
