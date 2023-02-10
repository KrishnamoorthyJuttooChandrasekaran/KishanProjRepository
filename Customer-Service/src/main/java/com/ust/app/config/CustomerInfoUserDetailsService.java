package com.ust.app.config;


import com.ust.app.entity.Customer;
import com.ust.app.repositary.CustomerRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerInfoUserDetailsService implements UserDetailsService {

    @Autowired
    CustomerRepositary repository;
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<Customer> userInfo = repository.findByUserName(name);
        return userInfo.map(CustomerInfoUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + name));
    }
}
