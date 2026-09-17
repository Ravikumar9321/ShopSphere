package com.shopsphere.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopsphere.Entity.UserInfo;

public interface User_Repository extends JpaRepository<UserInfo, Integer> {
   Optional<UserInfo> findByEmail(String email);
}
