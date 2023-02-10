package com.ust.app.controller;

import com.ust.app.model.Farmer;
import com.ust.app.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "FARMER-SERVICE")
public interface FarmerController {

    @PostMapping("/farmer/registerFarmer")
    public Farmer addFarmer(@RequestBody Farmer farmer);



    @DeleteMapping("/farmer/deletefarmer/{id}")
    public String deleteFarmer(int id);

    @GetMapping("/farmer/getfarmerById/{farmerId}")
    public Farmer getFarmerById(int farmerId);

    @GetMapping("/farmer/getProductdetails/{farmerId}")
    public List<Product> getProductDetailsUsingFarmerId(int farmerId);




    @GetMapping("/product/allproducts")
    public List<Product> getproductDetails();

    @GetMapping("/product/getProductById/{productId}")
    public Product getProductById(@PathVariable Integer productId);

    @DeleteMapping("/product/deleteproduct/{productId}")
    public String deleteProduct(@PathVariable("productId") int productId);


     @PutMapping("/farmer/updatefarmer/{farmerId}")
      public Farmer updateFarmer(@RequestBody Farmer farmer,@PathVariable int farmerId);

     @PutMapping("/product/updateProduct/{productId}")
     public Product updateProduct(@RequestBody Product product,@PathVariable int productId);

     @PostMapping("/product/submitproduct/{farmerId}")
     public Product submitProduct(@RequestBody Product product,@PathVariable int farmerId);

}
