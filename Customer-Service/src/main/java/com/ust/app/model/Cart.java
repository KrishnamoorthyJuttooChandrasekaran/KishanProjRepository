package com.ust.app.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@Data
public class Cart {
    private List<Product> products;
    private double totalPrice;

    private  int quantity;

    public Cart() {
        this.products = new ArrayList<>();
        this.totalPrice = 0.0;
        this.quantity = 0;
    }


    public Product addProduct(Product product, int quantity) {
        this.products.add(product);
        this.quantity= quantity;
        this.totalPrice += product.getProductPrice() * quantity ;
        return product;
    }

    public Product removeProduct(Product product,int quantity) {
        this.products.remove(product);
        this.quantity= quantity;
        this.totalPrice -= product.getProductPrice() * product.getProductQty();
        return product;
    }

}
