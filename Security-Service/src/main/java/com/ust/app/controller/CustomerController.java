package com.ust.app.controller;

import com.ust.app.model.Customer;
import com.ust.app.model.Farmer;
import com.ust.app.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="CUSTOMER-SERVICE")
public interface CustomerController {

    @GetMapping("/customer/getallcustomers")
    public List<Customer> getAllCustomers();

    @PostMapping("/customer/register")
    public Customer registerCustomer(@RequestBody Customer customer);

    @GetMapping("/customer/getcustomer/{customerId}")
    public Customer getCustomer(@PathVariable int customerId);

    @PutMapping("/customer/update/{customerId}")
    public Customer updateCustomerDet(@PathVariable int customerId, @RequestBody Customer customer);

    @DeleteMapping("/customer/delete/{customerId}")
    public String  deleteCustomer(@PathVariable int customerId);

    @GetMapping("/customer/farmer/{farmerId}")
    public Farmer getFarmerData(@PathVariable Integer farmerId);

    @GetMapping("/customer/farmer/products/{farmerId}")
    public List<Product> getProductsByFarmerId(@PathVariable Integer farmerId);






}
