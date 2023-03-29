package com.ust.app.controller;



import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.netflix.discovery.converters.Auto;
import com.ust.app.CustomerServiceApplication;
import com.ust.app.model.*;
import com.ust.app.entity.Customer;
import com.ust.app.service.CustomerService;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/customer")

//@SessionAttributes("cart")
public class CustomerController {

    private Cart cart;



    @Autowired
    public CustomerController(Cart cart) {
        this.cart = cart;

    }

    @Autowired
    private JavaMailSender javaMailSender;



    @Autowired
    private CustomerService customerService;

    @Autowired
    private FarmerRestConsumer consumer;


    CartDetails cartDetails = new CartDetails();

    @PostMapping("/register")
    public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer1) {
        Customer _customer = customerService.registerCustomer(customer1);
        return new ResponseEntity<>(_customer, HttpStatus.CREATED);
    }

    @GetMapping("/getallcustomers")
    public ResponseEntity<List<Customer>> getcustomers() {
        List<Customer> _customer = customerService.getAllCustomers();
        return new ResponseEntity<>(_customer, HttpStatus.OK);
    }

    @GetMapping("/getcustomer/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable int customerId) {
        Customer _customer = customerService.getCustomer(customerId);
        return new ResponseEntity<>(_customer, HttpStatus.OK);
    }

    @PutMapping("/update/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer, @PathVariable int customerId) {
        Customer _customer = customerService.updateCustomerDet(customer, customerId);
        return new ResponseEntity<>(_customer, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{customerId}")
    //@PreAuthorize("hasAuthority('CUS_ROLE')")
    public ResponseEntity<String> deleteCustomer(@PathVariable int customerId) {
        String value = customerService.deleteCustomer(customerId);
        return new ResponseEntity<>(value, HttpStatus.OK);
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<Farmer> getFarmerInfo(@PathVariable Integer farmerId) {
        Farmer _farmer = consumer.getFarmerData(farmerId);
        return new ResponseEntity<>(_farmer, HttpStatus.OK);
    }

    @GetMapping("/farmer/products/{farmerId}")
    public ResponseEntity<List<Product>> getProductsByFarmerId(@PathVariable Integer farmerId) {
        List<Product> _product = consumer.getProductDataById(farmerId);
        return new ResponseEntity<>(_product, HttpStatus.OK);
    }

    @GetMapping("/product/allproducts")
    public ResponseEntity<List<Product>> getallProducts() {
        List<Product> _products = consumer.getproductDetails();
        return new ResponseEntity<>(_products, HttpStatus.OK);
    }

    @GetMapping("/product/getProductById/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer productId) {
        Product _product = consumer.getProductById(productId);
        return new ResponseEntity<>(_product, HttpStatus.OK);
    }

    @GetMapping("farmer/getallfarmers")
    public ResponseEntity<List<Farmer>> getAllfarmerdetails() {
        List<Farmer> _farmer = consumer.getAllFarmersData();
        return new ResponseEntity<List<Farmer>>(_farmer, HttpStatus.OK);
    }


    @GetMapping("/addToCart/{productId}/{farmerId}/{customerId}/{quantity}")
    public ResponseEntity<CartDetails> addToCart(@PathVariable("productId") int productId, @PathVariable("farmerId") int farmerId, @PathVariable("customerId") int customerId, @PathVariable("quantity") int quantity) {
        cartDetails.setFarmer(consumer.getFarmerData(farmerId));
        cartDetails.setCustomer(customerService.getCustomer(customerId));
        cartDetails.setProduct(consumer.getProductById(productId));

        Product product = consumer.getProductById(productId);

        cart.addProduct(product, quantity);
        return new ResponseEntity<>(cartDetails, HttpStatus.OK);
    }

    @GetMapping("/removeFromCart/{productId}/{quantity}")
    public ResponseEntity<String> removeFromCart(@PathVariable("productId") int productId,
                                                 @PathVariable("quantity") int quantity) {

        Product product = consumer.getProductById(productId);
        cart.removeProduct(product, quantity);
        return new ResponseEntity<>("Product Removed you can check in cart", HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping("/buy")
    public ResponseEntity<Cart> viewCart() {


        String body = "Order Details\n-----------------------------" +
                "\nProduct ID: " + cartDetails.getProduct().getProductId() +
                "\nProduct Name: " + cartDetails.getProduct().getProductName() +
                "\nPrice per kg: " + cartDetails.getProduct().getProductPrice() +
                "\nProduct Quantity: " + cartDetails.getProduct().getProductQty() +
                "\nPrice: " + cart.getTotalPrice() +
                "\n\n\nFarmer Details\n-----------------------------" +
                "\nFarmer ID: " + cartDetails.getFarmer().getId() +
                "\nFarmer Name: " + cartDetails.getFarmer().getUsername();


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

        document.add(new Paragraph("\nHey " + cartDetails.getCustomer().getCustomerName() + "!!! Thanks for purchasing from Kishan Application"));
        document.add(new Paragraph(phrase));
        document.add(new Paragraph("------------------------------------------------------------------"));
        document.add(new Paragraph("Purchase Details:"));
        document.add(new Paragraph("------------------------------------------------------------------"));
        document.add(new Paragraph(body));

        document.add(image);
        document.close();

        byte[] pdfBytes = baos.toByteArray();
        System.out.println("PDF created successfully with " + pdfBytes.length + " bytes.");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setTo(cartDetails.getCustomer().getEmailId());
        messageHelper.setSubject("Thanks for purchasing from Kishan Application");
        messageHelper.setText("Hey " + cartDetails.getCustomer().getCustomerName() + "!!! Thanks for purchasing from Kishan Application");
        messageHelper.addAttachment(cartDetails.getCustomer().getCustomerName() + ".pdf", new ByteArrayResource(pdfBytes));


        System.out.println("Sending mail to Customer : " + cartDetails.getCustomer().getEmailId());
        javaMailSender.send(message);
        System.out.println("Mail Sent to Farmer: " + cartDetails.getCustomer().getEmailId());

        System.out.println(consumer.getProductById(cartDetails.getProduct().getProductId()));

        Product product = consumer.getProductById(cartDetails.getProduct().getProductId());

        System.out.println(product.getProductQty());
        System.out.println();


        return new ResponseEntity<>(cart, HttpStatus.OK);
    }


}
