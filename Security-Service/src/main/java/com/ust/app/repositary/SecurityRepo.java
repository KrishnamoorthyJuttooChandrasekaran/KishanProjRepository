package com.ust.app.repositary;

import com.ust.app.entity.SecurityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecurityRepo extends JpaRepository<SecurityEntity , Integer> {
    Optional<SecurityEntity> findByUserName(String name);
}
