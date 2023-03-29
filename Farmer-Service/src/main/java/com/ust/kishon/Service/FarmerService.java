package com.ust.kishon.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.ust.kishon.Entity.Farmer;
import com.ust.kishon.Entity.Product;
import com.ust.kishon.Exception.FarmerNotFoundException;
import com.ust.kishon.Exception.ProductNotFoundException;
import com.ust.kishon.Exception.UnauthorizedException;
import com.ust.kishon.Repo.FarmerRepo;
import com.ust.kishon.Repo.ProductRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FarmerService {
    @Autowired
    FarmerRepo farmerRepo;
    @Autowired
    ProductRepo pRepo;
    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(FarmerService.class);

    @SneakyThrows
    public Farmer addFarmer(Farmer farmer) {
        logger.info("Inside the FarmerService and RegisterFarmer Method");
        Optional<Farmer> existingPhone = Optional.ofNullable(farmerRepo.findByPhone(farmer.getPhone()));
        Optional<Farmer> existingUserName = farmerRepo.findByUsername(farmer.getUsername());

        if (existingPhone.isPresent()) {
            throw new FarmerNotFoundException("farmer", "Phone", farmer.getPhone());
        } else if (existingUserName.isPresent()) {
            throw new FarmerNotFoundException("Farmer", "Phone", farmer.getUsername());
        }

        String body = "Your Details are " +
                "\nFarmer Name: " + farmer.getUsername() +
                "\nMail: " + farmer.getEmail() +
                "\nAddress: " + farmer.getAddress() +
                "\nPhone: " + farmer.getPhone() +
                "\nAadhar No: " + farmer.getAdharno() +
                "\nAddress: " + farmer.getAddress() +
                "\nAccount No: " + farmer.getAccountno() +
                "\nIFSC : " + farmer.getIfcno() +
                "\nUPI : " + farmer.getUpi();

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
        PdfWriter.getInstance(document, baos);

        document.open();

        Font headingFont = FontFactory.getFont("Verdana", 32, Font.BOLDITALIC);
        //new Font(Font.FontFamily.HELVETICA,32,Font.BOLDITALIC);
        Paragraph heading = new Paragraph("Kishan Application", headingFont);
        heading.setAlignment(Element.ALIGN_CENTER);
        document.add(heading);

        document.add(new Paragraph("\nHey " + farmer.getUsername() + "!!! Your details are registered in Kishan Application"));
        document.add(new Paragraph(phrase));
        document.add(new Paragraph("------------------------------------------------------------------"));
        document.add(new Paragraph("Farmer Details:"));
        document.add(new Paragraph("------------------------------------------------------------------"));

        document.add(new Paragraph(body));

        document.add(image);
        document.close();

        byte[] pdfBytes = baos.toByteArray();
        System.out.println("PDF created successfully with " + pdfBytes.length + " bytes.");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setTo(farmer.getEmail());
        messageHelper.setSubject("Your details are registered in Kishan Application");
        messageHelper.setText("Hey " + farmer.getUsername() + "!!! Your details are registered in Kishan Application");
        messageHelper.addAttachment(farmer.getUsername() + ".pdf", new ByteArrayResource(pdfBytes));

        System.out.println("Sending mail to Farmer: " + farmer.getEmail());
        javaMailSender.send(message);
        System.out.println("Mail Sent to Farmer: " + farmer.getEmail());

        farmer.setPassword(passwordEncoder.encode(farmer.getPassword()));
        return farmerRepo.save(farmer);
    }

    public String deleteFarmer(int id) {
        logger.info("Inside the FarmerService and Delete Method");
        SimpleMailMessage message = new SimpleMailMessage();
        Farmer farmer = farmerRepo.findById(id).orElseThrow(() ->
                new FarmerNotFoundException("Farmer", "Id", id));
        message.setTo(farmer.getEmail());
        message.setSubject("Your details and your crops details are deleted in Kishan Application");
        message.setText("Name: " + farmer.getUsername() + "\nId: " + farmer.getId() + " is deleted in Kishan Application");
        javaMailSender.send(message);

        List<Product> prod = pRepo.findByFarmerId(id);
        List<Integer> prodIds = new ArrayList<Integer>();
        for (Product p : prod) {
            prodIds.add(p.getProductId());
        }
        System.out.println(prodIds);

        //Delete Farmer
        farmerRepo.deleteById(id);

        //Delete Products of the deleted farmer
        for (int ids :  prodIds) {
            pRepo.deleteById(ids);
        }

        return "Farmer and their products are deleted";
    }

    public Farmer getfarmerById(int farmerId) {
        logger.info("Inside the FarmerService and getfarmerById Method");

        Farmer farmer = this.farmerRepo.findById(farmerId).orElseThrow(() -> new
                FarmerNotFoundException("Farmer", "Id", farmerId));
        return farmer;
    }

    public List<Product> getProductDetailsUsingFarmerId(int farmerId) {
        logger.info("Inside the FarmerService and getProductDetailUsingFarmer Method");

       /* Farmer farmer = this.farmerRepo.findById(farmerId).
                orElseThrow(() -> new FarmerNotFoundException("Farmer", "Id", farmerId));
        return pRepo.findAll(); */

        Optional<Farmer> farmer = farmerRepo.findById(farmerId);
        List<Product> product = null;
        if (farmer.isPresent()) {
            product = pRepo.findByFarmerId(farmer.get().getId());
            if (product.isEmpty()) {
                throw new ProductNotFoundException("Product", "Farmer Id", farmerId);
            }
        }
        return product;
    }

    @SneakyThrows
    public Farmer updateFarmer(int farmerId, Farmer farmer) throws WriterException, MessagingException {
        logger.info("Inside the FarmerService and updateFarmer Method");
        Farmer f = new Farmer();
        Farmer findById = this.farmerRepo.findById(farmerId)
                .orElseThrow(() -> new FarmerNotFoundException("Farmer", "id", farmerId));
        f.setId(farmerId);
        f.setAccountno(farmer.getAccountno());
        f.setAddress(farmer.getAddress());
        f.setEmail(farmer.getEmail());
        f.setAdharno(farmer.getAdharno());
        f.setEnabled(farmer.isEnabled());
        f.setIfcno(farmer.getIfcno());
        f.setPhone(farmer.getPhone());
        f.setRoles(farmer.getRoles());
        f.setUpi(farmer.getUpi());
        f.setUsername(farmer.getUsername());
        f.setPassword(farmer.getPassword());

        String body = "Your Details are " +
                "\nFarmer Name: " + f.getUsername() +
                "\nMail: " + f.getEmail() +
                "\nAddress: " + f.getAddress() +
                "\nPhone: " + f.getPhone() +
                "\nAadhar No: " + f.getAdharno() +
                "\nAddress: " + f.getAddress() +
                "\nAccount No: " + f.getAccountno() +
                "\nIFSC : " + f.getIfcno() +
                "\nUPI : " + f.getUpi();

        //Code for QR code
        int width = 200;
        int height = 200;
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(body, BarcodeFormat.QR_CODE, width, height);
        java.awt.Image qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        Image image = com.itextpdf.text.Image.getInstance(qrImage, null);

        //Code for Farmer Image
        String logoUrl = "images\\Farmer.jpg";
        Image img = Image.getInstance(logoUrl);
        img.scaleAbsolute(150, 180);
        Phrase phrase = new Phrase();
        phrase.add(new Chunk(img, 350, -200));
        System.out.println(img);

        //Creation of PDF
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();

        //Adding contents into the PDF
        Font headingFont = FontFactory.getFont("Verdana", 32, Font.BOLDITALIC);
        //new Font(Font.FontFamily.HELVETICA,32,Font.BOLDITALIC);
        Paragraph heading = new Paragraph("Kishan Application", headingFont);
        heading.setAlignment(Element.ALIGN_CENTER);
        document.add(heading);

        document.add(new Paragraph("\nHey " + f.getUsername() + "!!! Your details are updated in Kishan Application"));
        document.add(new Paragraph(phrase));
        document.add(new Paragraph("------------------------------------------------------------------"));
        document.add(new Paragraph("Farmer Details:"));
        document.add(new Paragraph("------------------------------------------------------------------"));

        document.add(new Paragraph(body));

        document.add(image);
        document.close();

        //Saving the pdf in byteArray
        byte[] pdfBytes = baos.toByteArray();
        System.out.println("PDF created successfully with " + pdfBytes.length + " bytes.");

        //Sending the mail with attachement
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
        messageHelper.setTo(f.getEmail());
        messageHelper.setSubject("Your details are updated in Kishan Application");
        messageHelper.setText("Hey " + f.getUsername() + "!!! Your details are updated in Kishan Application");
        messageHelper.addAttachment(f.getUsername() + ".pdf", new ByteArrayResource(pdfBytes));

        System.out.println("Sending mail to Farmer: " + f.getEmail());
        javaMailSender.send(message);
        System.out.println("Mail Sent to Farmer: " + f.getEmail());
        return farmerRepo.save(f);
    }

    public Farmer findFarmerByPhone(String phone) {
        return farmerRepo.findByPhone(phone);

    }

    public Product getProductDetailUsingFarmerProductId(int productId, int farmerId) {
        Optional<Farmer> farmer = farmerRepo.findById(farmerId);
        if (!farmer.isPresent()) {
            throw new FarmerNotFoundException("farmer", "id", farmerId);
        }
        Optional<Product> product = pRepo.findById(productId);
        if (!product.isPresent()) {
            throw new ProductNotFoundException("product", "id", productId);
        }
        Product productDetails = null;
        if (farmer.isPresent() && product.isPresent()) {
            productDetails = pRepo.findByFarmerProductId(farmer.get().getId(), product.get().getProductId());
        }
        if (Objects.isNull(productDetails)) {
            throw new UnauthorizedException("Unauthorized", farmerId);
        }
        return productDetails;
    }

    public List<Farmer> getAllfarmers() {

        List<Farmer> farmer = this.farmerRepo.findAll();
        return farmer;
    }
}
