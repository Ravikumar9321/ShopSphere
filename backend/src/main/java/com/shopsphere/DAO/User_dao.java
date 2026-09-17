package com.shopsphere.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.shopsphere.Entity.UserInfo;
import com.shopsphere.Repository.User_Repository;

@Repository
public class User_dao {

	@Autowired
	private User_Repository repository;

	public UserInfo createuser(UserInfo user) {
		return repository.save(user);
	}

	public List<UserInfo> getAllUser() {
		return repository.findAll();
	}
}
