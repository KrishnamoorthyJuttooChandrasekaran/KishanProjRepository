package com.ust.app.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.ust.app.Exception.CustomerNotFoundException;
import com.ust.app.entity.Customer;
import com.ust.app.repositary.CustomerRepositary;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.ByteArrayOutputStream;
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

    public CustomerService() {
    }

    @SneakyThrows
    public Customer registerCustomer(Customer customerRegistration) {
        logger.info("Inside the CustomerService and RegisterCustomer Method");
        Optional<Customer> existingMobile = Optional.ofNullable(customerRepositary.findBymobileNo(customerRegistration.getMobileNo()));
        Optional<Customer> existingUserName = customerRepositary.findByUserName(customerRegistration.getUserName());
        if (existingUserName.isPresent()) {
            throw new CustomerNotFoundException("Customer", "Mobile", customerRegistration.getMobileNo());
        } else if (existingUserName.isPresent()) {
            throw new CustomerNotFoundException("Customer", "Name", customerRegistration.getCustomerName());
        }

      String body = "Hey Customer : "+ customerRegistration.getCustomerName() + "!! Your details are registered in Kishan Application\n" +
              "Your Details are " +
              "\nCustomer Name: " + customerRegistration.getCustomerName() +
              "\nUser Name: " + customerRegistration.getUserName() +
              "\nMail: " + customerRegistration.getEmailId() +
              "\nAddress: " + customerRegistration.getAddress()+
              "\nPinCode: " + customerRegistration.getPincode()+
              "\nAadhar No: " + customerRegistration.getAadharNo()+
              "\nMobile: " + customerRegistration.getMobileNo()+
              "\nUserName: " + customerRegistration.getUserName();

        int width = 200;
        int height = 200;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(body, BarcodeFormat.QR_CODE, width, height);
        java.awt.Image qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        Image image = com.itextpdf.text.Image.getInstance(qrImage, null);

        String logoUrl = "images\\Customer.png";
        Image img = Image.getInstance(logoUrl);
        img.scaleAbsolute(150, 180);
        Phrase phrase = new Phrase();
        phrase.add(new Chunk(img, 350, -200));
        System.out.println(img);

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();

        Font headingFont = FontFactory.getFont("Verdana", 32, Font.BOLDITALIC);
        //new Font(Font.FontFamily.HELVETICA,32,Font.BOLDITALIC);
        Paragraph heading = new Paragraph("Kishan Application", headingFont);
        heading.setAlignment(Element.ALIGN_CENTER);
        document.add(heading);

        document.add(new Paragraph("\nHey " + customerRegistration.getCustomerName() + "!!! Your details are registered in Kishan Application"));
        document.add(new Paragraph(phrase));
        document.add(new Paragraph("------------------------------------------------------------------"));
        document.add(new Paragraph("Customer Details:"));
        document.add(new Paragraph("------------------------------------------------------------------"));

        document.add(new Paragraph(body));

        document.add(image);
        document.close();

        byte[] pdfBytes = baos.toByteArray();
        System.out.println("PDF created successfully with " + pdfBytes.length + " bytes.");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setTo(customerRegistration.getEmailId());
        messageHelper.setSubject("Your details are registered in Kishan Application");
        messageHelper.setText("Hey " + customerRegistration.getCustomerName() + "!!! Your details are registered in Kishan Application");
        messageHelper.addAttachment(customerRegistration.getCustomerName() + ".pdf", new ByteArrayResource(pdfBytes));

        System.out.println("Sending mail to Customer : " + customerRegistration.getEmailId());
        javaMailSender.send(message);
        System.out.println("Mail Sent to Farmer: " + customerRegistration.getEmailId());

        customerRegistration.setPassword(passwordEncoder.encode(customerRegistration.getPassword()));
        return customerRepositary.save(customerRegistration);
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

    @SneakyThrows
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

        String body=cus.getCustomerName() + "are updated in Kishan Application\n" +
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
                "\nUserName: " + cus.getUserName();

        int width = 200;
        int height = 200;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(body, BarcodeFormat.QR_CODE, width, height);
        java.awt.Image qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        Image image = com.itextpdf.text.Image.getInstance(qrImage, null);

        String logoUrl = "images\\Customer.png";
        Image img = Image.getInstance(logoUrl);
        img.scaleAbsolute(150, 180);
        Phrase phrase = new Phrase();
        phrase.add(new Chunk(img, 350, -200));
        System.out.println(img);

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();
        Font headingFont = FontFactory.getFont("Verdana", 32, Font.BOLDITALIC);
        //new Font(Font.FontFamily.HELVETICA,32,Font.BOLDITALIC);
        Paragraph heading = new Paragraph("Kishan Application", headingFont);
        heading.setAlignment(Element.ALIGN_CENTER);
        document.add(heading);

        document.add(new Paragraph("\nHey " + cus.getCustomerName() + "!!! Your details are updated in Kishan Application"));
        document.add(new Paragraph(phrase));
        document.add(new Paragraph("------------------------------------------------------------------"));
        document.add(new Paragraph("Customer Details:"));
        document.add(new Paragraph("------------------------------------------------------------------"));

        document.add(new Paragraph(body));

        document.add(image);
        document.close();

        byte[] pdfBytes = baos.toByteArray();
        System.out.println("PDF created successfully with " + pdfBytes.length + " bytes.");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setTo(cus.getEmailId());
        messageHelper.setSubject("Your details are updated in Kishan Application");
        messageHelper.setText("Hey " + cus.getCustomerName() + "!!! Your details are updated in Kishan Application");
        messageHelper.addAttachment(cus.getCustomerName() + ".pdf", new ByteArrayResource(pdfBytes));

        System.out.println("Sending mail to Customer : " + cus.getEmailId());
        javaMailSender.send(message);
        System.out.println("Mail Sent to Farmer: " + cus.getEmailId());

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
