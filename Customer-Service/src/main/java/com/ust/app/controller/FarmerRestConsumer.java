package com.ust.app.controller;

import com.ust.app.model.Farmer;
import com.ust.app.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @GetMapping("farmer/getallfarmers")
    public List<Farmer> getAllFarmersData();

    @PutMapping("product/updateProduct/{productId}")
    public Product updateProduct(@PathVariable int productId, @RequestBody Product product);

    @PutMapping("product/setProductCountAdd/{productId}/{quantity}")
    public String setproductCountAdd(@PathVariable int productId,@PathVariable int quantity);

    @PutMapping("product/setProductCountRem/{productId}/{quantity}")
    public String setproductCountRem(@PathVariable int productId,@PathVariable int quantity);


    }
