package com.ust.kishon.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name ="product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int productId;
    //@NotBlank(message = "Please enter Product Name its mandatory!! ")
    private String productName;
    //@NotBlank(message = "Please enter organic  its mandatory!! ")
    private boolean organic;

    //@NotBlank(message = "Please enter product price its mandatory!! ")
    private double productPrice;
    @NotNull
    private boolean inStock;

    @NotNull
    private String usedBy;
    @NotNull
    private int productQty;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "farmer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    //@Transient
    private Farmer farmer;



}
