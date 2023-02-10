package com.dailycodebuffer.cloud.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallBackMethodController {

    @GetMapping("/customerServiceFallBack")
    public String customerServiceFallBackMethod() {
        return "customer Service is taking longer than Expected." +
                " Please try again later";
    }

    @GetMapping("/farmerServiceFallBack")
    public String farmerServiceFallBackMethod() {
        return "farmer Service is taking longer than Expected." +
                " Please try again later";
    }

    @GetMapping("/securityServiceFallBack")
    public String securityServiceFallBackMethod() {
        return "security Service is taking longer than Expected." +
                " Please try again later";
    }
}
