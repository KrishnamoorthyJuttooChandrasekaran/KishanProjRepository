package com.cloudGateway.apicloudgateway;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackMethodController {

    private static final String SERVICE_A = "api-gateway";

    @RequestMapping(value = "/customerServiceFallBack", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    @CircuitBreaker(name = "CUSTOMER-SERVICE")
    @Retry(name = "CUSTOMER-SERVICE")
    @RateLimiter(name = "CUSTOMER-SERVICE")
    public String customerServiceFallBackMethod() {
        return "Customer Service is taking longer than expected.\n" +
                "Service is not available at the current moment.\n" +
                "Please try again later";
    }

    @RequestMapping(value = "/farmerServiceFallBack", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    @CircuitBreaker(name = "FARMER-SERVICE")
    @Retry(name = "FARMER-SERVICE")
    @RateLimiter(name = "FARMER-SERVICE")
    public String farmerServiceFallBackMethod() {
        return "Farmer Service is taking longer than expected.\n" +
                "Service is not available at the current moment.\n" +
                "Please try again later";
    }

    @RequestMapping(value = "/securityServiceFallBack", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    @CircuitBreaker(name = "SECURITY-SERVICE")
    @Retry(name = "SECURITY-SERVICE")
    @RateLimiter(name = "SECURITY-SERVICE")
    public String securityServiceFallBackMethod() {
        return "Security Service is taking longer than expected.\n" +
                "Service is not available at the current moment.\n" +
                "Please try again later";
    }
}
