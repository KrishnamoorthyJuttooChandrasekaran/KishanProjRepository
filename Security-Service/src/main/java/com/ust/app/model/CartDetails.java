package com.ust.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDetails {
    private Farmer farmer;
    private Product product;
    private Customer customer;
}
