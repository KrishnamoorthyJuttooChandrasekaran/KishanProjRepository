package com.ust.kishon.Repo;

import com.ust.kishon.Entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FarmerRepo extends JpaRepository<Farmer,Integer> {
    Optional<Farmer> findByUsername(String username);
    Farmer findByPhone(String Phone);

}
