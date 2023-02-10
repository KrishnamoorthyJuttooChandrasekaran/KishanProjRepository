package com.ust.kishon.Repo;

import com.ust.kishon.Entity.Product;
import lombok.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product,Integer> {

    @Query(value ="select * from product where farmer_id=:id",nativeQuery = true)
    List<Product> findByFarmerId(@Param("id") int id);

    @Query(value ="select * from product where farmer_id=:farmerId and product_id=:productId",nativeQuery = true)
    Product findByFarmerProductId(@Param("farmerId") int id,@Param("productId") int productId);

}
