package com.ust.app.controller;

import com.ust.app.model.Farmer;
import com.ust.app.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="FARMER-SERVICE")
public interface FarmerRestConsumer
{
    @GetMapping("/farmer/getfarmerById/{farmerId}")
    public Farmer getFarmerData(@PathVariable Integer farmerId);

    @GetMapping("/farmer/getProductdetails/{farmerId}")
    public List<Product> getProductDataById(@PathVariable Integer farmerId);

    @GetMapping("/product/getallProductdetails")
    public List<Product> getproductDetails();

    @GetMapping("/product/getProductById/{productId}")
    public Product getProductById(@PathVariable Integer productId);


}
