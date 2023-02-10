package com.ust.kishon.Service;

import com.ust.kishon.Entity.Farmer;
import com.ust.kishon.Entity.Product;
import com.ust.kishon.Exception.FarmerNotFoundException;
import com.ust.kishon.Exception.ProductNotFoundException;
import com.ust.kishon.Exception.UnauthorizedException;
import com.ust.kishon.Repo.FarmerRepo;
import com.ust.kishon.Repo.ProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public Farmer addFarmer(Farmer farmer) {
        logger.info("Inside the FarmerService and RegisterFarmer Method");
        Optional<Farmer> existingPhone = Optional.ofNullable(farmerRepo.findByPhone(farmer.getPhone()));
        Optional<Farmer> existingUserName = farmerRepo.findByUsername(farmer.getUsername());

        if (existingPhone.isPresent()) {
            throw new FarmerNotFoundException("farmer", "Phone", farmer.getPhone());
        } else if (existingUserName.isPresent()) {
            throw new FarmerNotFoundException("Farmer", "Phone", farmer.getUsername());
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(farmer.getEmail());
        message.setSubject("Your details are registered in Kishan Application");
        message.setText(farmer.getUsername() + " is registered in Kishan Application\n" +
                "Your Details are " +
                "\nCustomer Name: " + farmer.getUsername() +
                "\nMail: " + farmer.getEmail() +
                "\nAddress: " + farmer.getAddress() +
                "\nPhone: " + farmer.getPhone() +
                "\nAadhar No: " + farmer.getAdharno() +
                "\nAddress: " + farmer.getAddress() +
                "\nAccount No: " + farmer.getAccountno() +
                "\nIFSC : " + farmer.getIfcno() +
                "\nUPI : " + farmer.getUpi()
        );
        javaMailSender.send(message);

        farmer.setPassword(passwordEncoder.encode(farmer.getPassword()));
        return farmerRepo.save(farmer);
    }

    public String deleteFarmer(int id) {
        logger.info("Inside the FarmerService and Delete Method");
        SimpleMailMessage message = new SimpleMailMessage();
        Farmer farmer = farmerRepo.findById(id).orElseThrow(() ->
                new FarmerNotFoundException("Farmer", "Id", id));
        message.setTo(farmer.getEmail());
        message.setSubject("Your details are deleted in Kishan Application");
        message.setText("Name: " + farmer.getUsername() + "\nId: " + farmer.getId() + " is deleted in Kishan Application");
        javaMailSender.send(message);

        farmerRepo.deleteById(id);
        return "Farmer Deleted";
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
        List<Product> product=null;
        if(farmer.isPresent()){
            product=pRepo.findByFarmerId(farmer.get().getId());
            if(product.isEmpty()){
                throw new ProductNotFoundException("Product","Farmer Id",farmerId);
            }
        }
        return product;
    }

    public Farmer updateFarmer(int farmerId, Farmer farmer) {
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

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(farmer.getEmail());
        message.setSubject("Your details are registered in Kishan Application");
        message.setText(farmer.getUsername() + " is registered in Kishan Application\n" +
                "Your Details are " +
                "\nFarmer ID: " + f.getId() +
                "\nFarmer Name: " + f.getUsername() +
                "\nMail: " + f.getEmail() +
                "\nAddress: " + f.getAddress() +
                "\nPhone: " + f.getPhone() +
                "\nAadhar No: " + f.getAdharno() +
                "\nAddress: " + f.getAddress() +
                "\nAccount No: " + f.getAccountno() +
                "\nIFSC : " + f.getIfcno() +
                "\nUPI : " + f.getUpi()
        );
        javaMailSender.send(message);

        return farmerRepo.save(f);
    }

    public Farmer findFarmerByPhone(String phone) {
        return farmerRepo.findByPhone(phone);

    }

    public Product getProductDetailUsingFarmerProductId(int productId, int farmerId) {
        Optional<Farmer> farmer = farmerRepo.findById(farmerId);
        if(!farmer.isPresent()){
            throw new FarmerNotFoundException("farmer","id",farmerId);
        }
        Optional<Product> product=pRepo.findById(productId);
        if(!product.isPresent()){
            throw new ProductNotFoundException("product","id",productId);
        }
        Product productDetails=null;
        if(farmer.isPresent()&& product.isPresent()) {
            productDetails = pRepo.findByFarmerProductId(farmer.get().getId(), product.get().getProductId());
        }
        if(Objects.isNull(productDetails)){
            throw new UnauthorizedException("Unauthorized",farmerId);
        }
       return productDetails;
    }
}
