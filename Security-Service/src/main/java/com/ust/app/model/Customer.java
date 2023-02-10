package com.ust.app.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class Customer {
    private int customerId;
    private String customerName;

    private String userName;
    private String mobileNo;
    private String emailId;
    private String aadharNo;
    private String address;
    private String pincode;
    private String password;

    private String role;
}
