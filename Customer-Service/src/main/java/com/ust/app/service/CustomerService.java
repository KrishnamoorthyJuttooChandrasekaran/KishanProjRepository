package com.ust.app.service;

import com.ust.app.Exception.CustomerNotFoundException;
import com.ust.app.entity.Customer;
import com.ust.app.repositary.CustomerRepositary;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    @Autowired
    private CustomerRepositary customerRepositary;

    @Autowired
    private JavaMailSender javaMailSender;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public Customer registerCustomer(Customer customerRegistration) {
        logger.info("Inside the CustomerService and RegisterCustomer Method");
        Optional<Customer> existingMobile = Optional.ofNullable(customerRepositary.findBymobileNo(customerRegistration.getMobileNo()));
        Optional<Customer> existingUserName = customerRepositary.findByUserName(customerRegistration.getUserName());
        if (existingUserName.isPresent()) {
            throw new CustomerNotFoundException("Customer", "Mobile", customerRegistration.getMobileNo());
        } else if (existingUserName.isPresent()) {
            throw new CustomerNotFoundException("Customer", "Name", customerRegistration.getCustomerName());
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(customerRegistration.getEmailId());
        message.setSubject("Your details are registered in Kishan Application");
        message.setText(customerRegistration.getCustomerName() + "is registered in Kishan Application\n" +
                "Your Details are " +
                "\nCustomer Name: " + customerRegistration.getCustomerName() +
                "\nUser Name: " + customerRegistration.getUserName() +
                "\nMail: " + customerRegistration.getEmailId() +
                "\nAddress: " + customerRegistration.getAddress()+
                "\nPinCode: " + customerRegistration.getPincode()+
                "\nAadhar No: " + customerRegistration.getAadharNo()+
                "\nMobile: " + customerRegistration.getMobileNo()+
                "\nUserName: " + customerRegistration.getUserName());
        javaMailSender.send(message);
        customerRegistration.setPassword(passwordEncoder.encode(customerRegistration.getPassword()));
        return customerRepositary.save(customerRegistration);
    }

    private void sendMail() throws MailException, MessagingException {
    }

    public List<Customer> getAllCustomers() {
        logger.info("Inside the CustomerService and getAllCustomer Method");
        return customerRepositary.findAll();
    }

    public Customer getCustomer(int customerId) {
        logger.info("Inside the CustomerService and getCustomer Method");
        Customer farmer = this.customerRepositary.findById(customerId).orElseThrow(() -> new
                CustomerNotFoundException("Customer", "Id", customerId));
        return customerRepositary.findById(customerId).get();
    }

    public Customer updateCustomerDet(Customer customer, int customerId) {
        logger.info("Inside the CustomerService and updateCustomerDet Method");
        Customer cus = customerRepositary.findById(customerId).orElseThrow(
                () -> new CustomerNotFoundException("Customer", "Id", customerId));
        cus.setCustomerId(customer.getCustomerId());
        cus.setCustomerName(customer.getCustomerName());
        cus.setAddress(customer.getAddress());
        cus.setPincode(customer.getPincode());
        cus.setAadharNo(customer.getAadharNo());
        cus.setMobileNo(customer.getMobileNo());
        cus.setEmailId(customer.getEmailId());
        cus.setUserName(customer.getUserName());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(cus.getEmailId());
        message.setSubject("Your details are updated in Kishan Application");
        message.setText(cus.getCustomerName() + "are updated in Kishan Application\n" +
                "Your Details are " +
                "\nCustomer Name: " + cus.getCustomerName() +
                "\nUser Name: " + cus.getUserName() +
                "\nMail: " + cus.getEmailId() +
                "\nId: " + customerId +
                "\nAddress: " + cus.getAddress()+
                "\nPinCode: " + cus.getPincode()+
                "\nAadhar No: " + cus.getAadharNo()+
                "\nMobile: " + cus.getMobileNo()+
                "\nEmail: " + cus.getEmailId()+
                "\nUserName: " + cus.getUserName());

        javaMailSender.send(message);


        return customerRepositary.save(cus);
    }

    public String deleteCustomer(int customerId) {
        logger.info("Inside the CustomerService and deleteCustomer Method");
        Customer cust = customerRepositary.findById(customerId).orElseThrow(() ->
                new CustomerNotFoundException("Customer", "Id", customerId));
        customerRepositary.deleteById(customerId);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(cust.getEmailId());
        message.setSubject("Your details are deleted in Kishan Application");
        message.setText("Name: " + cust.getCustomerName() +"\nId: "+ cust.getCustomerId() + " is deleted in Kishan Application");
        javaMailSender.send(message);
        return "Customer Deleted SuccessFully";
    }
}
