package com.shopsphere.Service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shopsphere.Entity.UserInfo;
import com.shopsphere.Repository.User_Repository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetail implements UserDetailsService {

    private final User_Repository repository;  

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserInfo userInfo = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not registered")); // ✅ standard exception

        return User.withUsername(userInfo.getEmail())
                .password(userInfo.getPassword())
                .authorities("ROLE_USER")
                .build();
    }
}
