package com.ust.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int customerId;
    @NotBlank(message = "Please provide customer Name. Its mandatory")
    private String customerName;
    @NotBlank(message = "Please enter User Name its mandatory!! ")
    private String userName;
    @Size(min=1,max=10)
    @NotBlank(message = "User Phone is mandatory to fill !!")
    private String mobileNo;
    @Email(message = "please enter proper Email address")
    private String emailId;
    @Size(min=1,max=12)
    @NotBlank(message = "Please enter Aadhaar No its mandatory!! ")
    private String aadharNo;

    @NotBlank(message = "Please enter address , Its mandatry")
    private String address;
    @Size(max = 6)
    @NotBlank(message = "Please enter Pincode no Its Mandatory!!")
    private String pincode;
    @NotBlank(message = "Please enter Password its mandatory!! ")
    private String password;

    @NotBlank(message = "Please enter Role its mandatory!! ")
    private String role;
}
