package com.ust.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Farmer {

    private int id;
    private String username;
    private String password;
    private String email;
    private  String phone;

    private String adharno;

    private String address;

    private String accountno;
    private String ifcno;

    private String upi;

    private boolean enabled=true;

    private String Roles;





}
