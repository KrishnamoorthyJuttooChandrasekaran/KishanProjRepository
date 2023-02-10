package com.ust.app.service;

import com.ust.app.entity.SecurityEntity;
import com.ust.app.model.Customer;
import com.ust.app.model.Farmer;
import com.ust.app.repositary.SecurityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    @Autowired
    SecurityRepo securityRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public SecurityEntity storeCustPasswordDetails(Customer customer)
    {
        SecurityEntity securityEntity=new SecurityEntity();
        securityEntity.setPassword(passwordEncoder.encode(customer.getPassword()));
        securityEntity.setUserName(customer.getUserName());
        securityEntity.setId(customer.getCustomerId());
        securityEntity.setRole(customer.getRole());
        return  securityRepo.save(securityEntity);


    }
    public SecurityEntity storeFarmerPasswordDetails(Farmer farmer)
    {
        SecurityEntity securityEntity=new SecurityEntity();
        securityEntity.setPassword(passwordEncoder.encode(farmer.getPassword()));
        securityEntity.setUserName(farmer.getUsername());
        securityEntity.setId(farmer.getId());
        securityEntity.setRole(farmer.getRoles());
        return  securityRepo.save(securityEntity);


    }
}
