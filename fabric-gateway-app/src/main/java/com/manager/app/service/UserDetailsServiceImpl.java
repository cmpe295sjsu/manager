package com.manager.app.service;

import java.util.*;
import com.manager.app.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

@Service
public class UserDetailsServiceImpl  implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        com.manager.app.model.User user = usersRepository.findByEmail(email);
        if(user != null) {
            return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("There is no admin user  by this email.");
        }
    }
}
