package com.ust.app.controller;

import com.ust.app.entity.SecurityEntity;
import com.ust.app.model.Customer;
import com.ust.app.model.Farmer;
import com.ust.app.model.Product;
import com.ust.app.repositary.SecurityRepo;
import com.ust.app.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/security")
public class SecurityController  {

    @Autowired
    private SecurityService securityService;



    @Autowired
    private CustomerController customerController;


    @Autowired
    private FarmerController farmerController;



    @GetMapping("/customer")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public List<Customer> getCustomerInfo() {

        return  customerController.getAllCustomers();
    }
    @PostMapping("/savecustomerdata")
    public Customer postCustomerInfo(@RequestBody Customer customer)
    {
        securityService.storeCustPasswordDetails(customer);
       return  customerController.registerCustomer(customer);
    }

    @GetMapping("/getcustomer/{customerId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Customer getCustomerInfo(@PathVariable int customerId)
    {
        return customerController.getCustomer(customerId);
    }

    @PutMapping("/updateCustomer/{customerId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Customer updateCustomerInfo(@PathVariable int customerId,@RequestBody Customer customer)
    {
        return customerController.updateCustomerDet(customerId,customer);
    }

    @DeleteMapping("/deleteCustomer/{customerId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public String deleteCustomerInfo(@PathVariable int customerId)
    {
        return customerController.deleteCustomer(customerId);
    }

    @GetMapping("/getFarmerInfo/{farmerId}")
    @PreAuthorize("hasAuthority('FARMER')")
    public Farmer getFarmerInfo(@PathVariable int farmerId)
    {
        return customerController.getFarmerData(farmerId);
    }

    @GetMapping("/getProductInfo/{farmerId}")
    @PreAuthorize("hasAuthority('FARMER')")
    public List<Product> getproductInfoByFarmerId(@PathVariable int farmerId)
    {
        return customerController.getProductsByFarmerId(farmerId);
    }

    @GetMapping("/allProducts")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public List<Product> getAllProducts()
    {
        return farmerController.getproductDetails();
    }

    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAuthority('FARMER')")
    public Product productById(@PathVariable int productId)
    {
        return farmerController.getProductById(productId);
    }



    @DeleteMapping("/deleteproduct/{productId}")
    @PreAuthorize("hasAuthority('FARMER')")
    public String deleteproductDet(@PathVariable int productId){
        return farmerController.deleteProduct(productId);
    }


    @PostMapping("/registerFarmer")
    public Farmer addFarmerDet(@RequestBody Farmer farmer){

        securityService.storeFarmerPasswordDetails(farmer);
        return farmerController.addFarmer(farmer);
    }



    @DeleteMapping("/deletefarmer/{id}")
    @PreAuthorize("hasAuthority('FARMER')")
    public String deleteFarmerDet(@PathVariable int id){
        return farmerController.deleteFarmer(id);
    }

    @GetMapping("/getfarmerById/{farmerId}")
    @PreAuthorize("hasAuthority('FARMER')")
    public Farmer getFarmerByIdDet(@PathVariable int farmerId){
      return farmerController.getFarmerById(farmerId);
    }

    @GetMapping("/getProductdetails/{farmerId}")
    @PreAuthorize("hasAuthority('FARMER')")
    public List<Product> getProductDetailsUsingFarmerIdDet(@PathVariable int farmerId){
      return farmerController.getProductDetailsUsingFarmerId(farmerId);
    }



     @PostMapping("/addproduct/{farmerId}")
     public Product addProduct(@RequestBody Product product,@PathVariable int farmerId)
     {
          return farmerController.submitProduct( product,farmerId);
      }

    @PutMapping("/updateProduct/{productId}")
    @PreAuthorize("hasAuthority('FARMER')")
    public Product updateProductdet(@RequestBody Product product,@PathVariable int productId){
      return farmerController.updateProduct(product,productId);
    }

     @PutMapping("/updatefarmer/{farmerId}")
     @PreAuthorize("hasAuthority('FARMER')")
     public Farmer updateFarmerDetails(@RequestBody Farmer farmer, @PathVariable int farmerId){
          return farmerController.updateFarmer(farmer,farmerId);
      }





}
