package com.ust.kishon.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.ust.kishon.Entity.Product;
import com.ust.kishon.Exception.FarmerNotFoundException;
import com.ust.kishon.Exception.ProductNotFoundException;
import com.ust.kishon.Repo.FarmerRepo;
import com.ust.kishon.Repo.ProductRepo;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
//@EnableCircuitBreaker
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    @Autowired
    private FarmerRepo farmerRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private JavaMailSender javaMailSender;


    @SneakyThrows
    public Product submitProduct(Product product, int farmerId) {
        logger.info("Inside the ProductService and submitProduct Method");
        Product prd = farmerRepo.findById(farmerId).map(farmer -> {
            product.setFarmer(farmer);
            return productRepo.save(product);
        }).orElseThrow(() -> new FarmerNotFoundException("Farmer", "Id", farmerId));


        String body = "Hi " + prd.getFarmer().getUsername() + "\n" +
                "Your product " + prd.getProductName() + " is added in Kishan Application\n" +
                "\nYour Product Detail is " +
                "\nProduct Name: " + prd.getProductName() +
                "\nOrganic/Not: " + product.isOrganic() +
                "\nProduct Price: " + prd.getProductPrice() +
                "\nIn Stock: " + prd.isInStock() +
                "\nProduct Quantity: " + prd.getProductQty() +
                "\nFarmer ID: " + product.getFarmer().getId() +
                "\nFarmer Name: " + product.getFarmer().getUsername();

        int width = 200;
        int height = 200;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(body, BarcodeFormat.QR_CODE, width, height);
        java.awt.Image qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        Image image = com.itextpdf.text.Image.getInstance(qrImage, null);

        String logoUrl = "images\\Farmer.jpg";
        Image img = Image.getInstance(logoUrl);
        img.scaleAbsolute(150, 180);
        Phrase phrase = new Phrase();
        phrase.add(new Chunk(img, 350, -200));
        System.out.println(img);

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document,baos);

        document.open();

        Font headingFont = FontFactory.getFont("Verdana",32,Font.BOLDITALIC);

        Paragraph heading = new Paragraph("Kishan Application",headingFont);
        heading.setAlignment(Element.ALIGN_CENTER);
        document.add(heading);

        document.add(new Paragraph("\nHey "+product.getFarmer().getUsername()+"!!! Your product details are added in Kishan Application"));
        document.add(new Paragraph(phrase));
        document.add(new Paragraph("------------------------------------------------------------------"));
        document.add(new Paragraph("Product Details:"));
        document.add(new Paragraph("------------------------------------------------------------------"));
        document.add(new Paragraph(body));
        document.add(image);
        document.close();

        byte[] pdfBytes = baos.toByteArray();
        System.out.println("PDF created successfully with " + pdfBytes.length + " bytes.");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message,true);
        messageHelper.setTo(prd.getFarmer().getEmail());
        messageHelper.setSubject("Your product details are registered in Kishan Application");
        messageHelper.setText("Hi "+prd.getFarmer().getUsername() +" Your product " + prd.getProductName() + " is added in Kishan Application\n");
        messageHelper.addAttachment(prd.getFarmer().getUsername()+".pdf",new ByteArrayResource(pdfBytes));

        System.out.println("Sending mail to Farmer: "+prd.getFarmer().getEmail());
        javaMailSender.send(message);
        System.out.println("Mail Sent to Farmer: "+prd.getFarmer().getEmail());

        return prd;

    }

    public List<Product> getProductDetails() {
        logger.info("Inside the ProductService and getProductProduct Method");
        return productRepo.findAll();
    }

    public Product getproductById(int productId) {
        logger.info("Inside the ProductService and getProductById Method");
        return productRepo.findById(productId).orElseThrow(() ->
                new ProductNotFoundException("Product", "Id", productId));
    }

    @SneakyThrows
    public Product updateProduct(int productId, Product productDao) {
        logger.info("Inside the ProductService and updateProduct Method");
        Product product = productRepo.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("Product", "Id", productId));
        product.setProductName(productDao.getProductName());
        product.setOrganic(productDao.isOrganic());
        product.setInStock(productDao.isInStock());
        product.setProductPrice(productDao.getProductPrice());
        product.setUsedBy(productDao.getUsedBy());
        product.setProductQty(productDao.getProductQty());

        String body="Hi " + product.getFarmer().getUsername() + "\n" +
                "Your product " + product.getProductName() + " is updated in Kishan Application\n" +
                "\nYour Product Detail is " +
                "\nProduct Name: " + product.getProductName() +
                "\nOrganic/Not: " + product.isOrganic() +
                "\nProduct Price: " + product.getProductPrice() +
                "\nIn Stock: " + product.isInStock() +
                "\nProduct Quantity: " + product.getProductQty() +
                "\nFarmer ID: " + product.getFarmer().getId() +
                "\nFarmer Name: " + product.getFarmer().getUsername();

        int width = 200;
        int height = 200;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(body, BarcodeFormat.QR_CODE, width, height);
        java.awt.Image qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        Image image = com.itextpdf.text.Image.getInstance(qrImage, null);

        String logoUrl = "images\\Farmer.jpg";
        Image img = Image.getInstance(logoUrl);
        img.scaleAbsolute(150, 180);
        Phrase phrase = new Phrase();
        phrase.add(new Chunk(img, 350, -200));
        System.out.println(img);

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document,baos);

        document.open();

        Font headingFont = FontFactory.getFont("Verdana",32,Font.BOLDITALIC);
        //new Font(Font.FontFamily.HELVETICA,32,Font.BOLDITALIC);
        Paragraph heading = new Paragraph("Kishan Application",headingFont);
        heading.setAlignment(Element.ALIGN_CENTER);
        document.add(heading);

        document.add(new Paragraph("\nHey "+product.getFarmer().getUsername()+"!!! Your product details are updated in Kishan Application"));
        document.add(new Paragraph(phrase));
        document.add(new Paragraph("------------------------------------------------------------------"));
        document.add(new Paragraph("Product Details:"));
        document.add(new Paragraph("------------------------------------------------------------------"));
        document.add(new Paragraph(body));
        document.add(image);
        document.close();

        byte[] pdfBytes = baos.toByteArray();
        System.out.println("PDF created successfully with " + pdfBytes.length + " bytes.");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message,true);
        messageHelper.setTo(product.getFarmer().getEmail());
        messageHelper.setSubject("Your product details are registered in Kishan Application");
        messageHelper.setText("Hi "+product.getFarmer().getUsername() +" Your product " + product.getProductName() + " is added in Kishan Application\n");
        messageHelper.addAttachment(product.getFarmer().getUsername()+".pdf",new ByteArrayResource(pdfBytes));
        System.out.println("Sending mail to Farmer: "+product.getFarmer().getEmail());
        javaMailSender.send(message);
        System.out.println("Mail Sent to Farmer: "+product.getFarmer().getEmail());
        return productRepo.save(product);


    }

    public String deleteProduct(int productId) {
        logger.info("Inside the ProductService and deletProduct Method");
        SimpleMailMessage message = new SimpleMailMessage();
        Product product = productRepo.findById(productId).orElseThrow(() ->
                new FarmerNotFoundException("Product", "Id", productId));
        message.setTo(product.getFarmer().getEmail());
        logger.info(product.getFarmer().getEmail());
        message.setSubject("Your product " + product.getProductName() + " is removed from Kishan Application");
        message.setText("Hi " + product.getFarmer().getUsername() + "\n" +
                "Your product " + product.getProductName() + " is removed from Kishan Application");
        productRepo.deleteById(productId);
        javaMailSender.send(message);
        return "Product Deleted";
    }
}
