package com.ust.app.model;

import com.ust.app.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDetails {
    private Farmer farmer;
    private Product product;
    private Customer customer;
}
