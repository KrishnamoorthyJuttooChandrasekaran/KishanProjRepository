package com.ust.kishon.Exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FarmerNotFoundException extends RuntimeException {
    String resourceName;
    String fieldName;
    long fieldValue;
    String Phone;

    public FarmerNotFoundException( String resourceName, String fieldName, long fieldValue) {
        super(String.format("%s not found with %s : %s",resourceName,fieldName,fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
    public FarmerNotFoundException( String resourceName, String fieldName, String Phone) {
        super(String.format("%s  found with %s : %s",resourceName,fieldName,Phone));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.Phone = Phone;
    }

}
