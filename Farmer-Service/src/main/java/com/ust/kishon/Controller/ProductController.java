package com.ust.kishon.Controller;

import com.ust.kishon.Entity.Product;
import com.ust.kishon.Exception.FarmerNotFoundException;
import com.ust.kishon.Exception.ProductNotFoundException;
import com.ust.kishon.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
   private ProductService productService;

    @PostMapping("/submitproduct/{farmerId}")
    public ResponseEntity<Product> productInsert( @RequestBody Product product,@PathVariable int farmerId)
            {
        Product _product=productService.submitProduct(product,farmerId);
        return new ResponseEntity<>(_product, HttpStatus.CREATED);
    }

    @GetMapping("/getallProductdetails")
    public ResponseEntity<List<Product>> getproductDetails()
    {
        List<Product> _product= productService.getProductDetails();
        return new ResponseEntity<>(_product,HttpStatus.OK);
    }


    @GetMapping("/getProductById/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable int productId) throws ProductNotFoundException, FarmerNotFoundException {
        Product _product= productService.getproductById(productId);
        return new ResponseEntity<>(_product,HttpStatus.OK);
    }

    @PutMapping("/updateProduct/{productId}")
    public ResponseEntity<Product> updateProduct( @PathVariable("productId") int productId, @RequestBody Product product) throws ProductNotFoundException {
        Product _product= productService.updateProduct(productId,product);
        return new ResponseEntity<>(_product,HttpStatus.OK);
    }

    @DeleteMapping("/deleteproduct/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") int productId){
        productService.deleteProduct(productId);
        return new ResponseEntity<>("ProductDeleted Successfully",HttpStatus.NO_CONTENT);
    }
}
