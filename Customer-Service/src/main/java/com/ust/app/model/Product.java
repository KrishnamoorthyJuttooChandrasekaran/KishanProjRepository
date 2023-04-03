package com.ust.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {


    private int productId;

    @JsonIgnore
    private int farmerId;

    private String productName;

    private boolean organic;

    private double productPrice;

    private boolean inStock;

    private String usedBy;

    private int productQty;



}
