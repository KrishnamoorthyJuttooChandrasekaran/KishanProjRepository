package com.ust.kishon.Exception;

public class ProductNotFoundException extends RuntimeException{

    String resourceName;
    String fieldName;
    long fieldValue;

    public ProductNotFoundException( String resourceName, String fieldName, long fieldValue) {
        super(String.format("%s not found with %s : %s",resourceName,fieldName,fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }




}
