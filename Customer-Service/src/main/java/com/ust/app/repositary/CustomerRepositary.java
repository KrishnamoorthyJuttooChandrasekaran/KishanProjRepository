package com.ust.app.repositary;

import com.ust.app.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepositary extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByUserName(String username);
    public Customer findBymobileNo(String mobileNo);

}
