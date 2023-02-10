package com.ust.app.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerNotFoundException extends RuntimeException{
    String resourceName;
    String fieldName;
    long fieldValue;
    String phoneNo;

    public CustomerNotFoundException( String resourceName, String fieldName, long fieldValue) {
        super(String.format("%s not found with %s : %s",resourceName,fieldName,fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    public CustomerNotFoundException( String resourceName, String fieldName,String phone) {
        super(String.format("%s found with %s : %s",resourceName,fieldName,phone));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.phoneNo = phoneNo;
    }

}
