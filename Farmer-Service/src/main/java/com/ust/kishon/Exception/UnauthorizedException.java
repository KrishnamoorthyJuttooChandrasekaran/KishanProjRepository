package com.ust.kishon.Exception;

public class UnauthorizedException extends RuntimeException{

    String resourceName;
    long fieldValue;

    public UnauthorizedException( String resourceName,long fieldValue) {
        super(String.format("%s access for farmerId: %s",resourceName,fieldValue));
        this.resourceName = resourceName;
        this.fieldValue = fieldValue;
    }
}
