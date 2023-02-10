package com.ust.kishon.config;

import com.ust.kishon.Entity.Farmer;
import com.ust.kishon.Repo.FarmerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FarmerInfoFarmerDetailsService implements UserDetailsService
{

    @Autowired
    FarmerRepo farmerRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Farmer> userInfo = farmerRepo.findByUsername(username);
        return userInfo.map(FarmerInfoFarmerDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));

    }
    }

