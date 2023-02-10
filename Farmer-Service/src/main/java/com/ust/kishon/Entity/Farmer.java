package com.ust.kishon.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="farmer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Farmer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="farmerId")
    private int id;
    @NotBlank(message = "Please enter username it's a Manditary field!!")
    private String username;
    @NotBlank(message = "Please enter password it's a Manditary field!!")
    private String password;
    @Email(message = "Please enter proper email address!")
    @NotBlank(message = "Farmer email is mandatory to fill !!")
    private String email;
    @Size(min=1,max=10)
    @NotBlank(message = "Farmer Phone is mandatory to fill !!")
    private  String phone;

    @Size(min=1,max=12)
    @NotBlank(message = "Please enter Aadhaar No its mandatory!! ")
    private String adharno;

    @NotBlank(message = "Please enter address No its mandatory!! ")
    private String address;

    @NotBlank(message = "Please enter Account No its mandatory!! ")
    private String accountno;
    @NotBlank(message = "Please enter IFSC code its mandatory!! ")
    private String ifcno;

    @NotBlank(message = "Please enter UPI Id its mandatory!! ")
    private String upi;

    private boolean enabled=true;

    @NotBlank(message = "Please provide Role its mandatory!! ")
    private String Roles;

}
