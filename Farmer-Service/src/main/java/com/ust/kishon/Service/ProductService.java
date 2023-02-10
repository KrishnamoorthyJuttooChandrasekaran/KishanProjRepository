package com.ust.kishon.Service;
import com.ust.kishon.Entity.Product;
import com.ust.kishon.Exception.FarmerNotFoundException;
import com.ust.kishon.Exception.ProductNotFoundException;
import com.ust.kishon.Repo.FarmerRepo;
import com.ust.kishon.Repo.ProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    @Autowired
      private FarmerRepo farmerRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private JavaMailSender javaMailSender;


    public Product submitProduct( Product product,int farmerId){
        logger.info("Inside the ProductService and submitProduct Method");
        Product prd= farmerRepo.findById(farmerId).map(farmer -> {
            product.setFarmer(farmer);
            return   productRepo.save(product);
        }).orElseThrow(() -> new FarmerNotFoundException("Farmer","Id", farmerId));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(prd.getFarmer().getEmail());
        message.setSubject("Your product details "+ prd.getProductName() +" is added in Kishan Application");
        message.setText("Hi "+prd.getFarmer().getUsername() +"\n" +
                        "Your product " + prd.getProductName() + " is added in Kishan Application\n" +
                "\nYour Product Detail is " +
                "\nProduct Name: " +prd.getProductName() +
                "\nOrganic/Not: " + product.isOrganic() +
                "\nProduct Price: " +prd.getProductPrice() +
                "\nIn Stock: " +prd.isInStock() +
                "\nProduct Quantity: " +prd.getProductQty() +
                "\nFarmer ID: " + product.getFarmer().getId()+
                "\nFarmer Name: " + product.getFarmer().getUsername()

        );
        javaMailSender.send(message);


        return prd;

    }

    public List<Product> getProductDetails() {
        logger.info("Inside the ProductService and getProductProduct Method");
        return productRepo.findAll();
    }

    public Product getproductById(int productId){
        logger.info("Inside the ProductService and getProductById Method");
        return productRepo.findById(productId).orElseThrow(() ->
                new ProductNotFoundException("Product","Id",productId));}

    public Product updateProduct(int productId, Product productDao){
        logger.info("Inside the ProductService and updateProduct Method");
        Product product= productRepo.findById(productId).orElseThrow(
                ()-> new ProductNotFoundException("Product","Id" ,productId));
        product.setProductName(productDao.getProductName());
        product.setOrganic(productDao.isOrganic());
        product.setInStock(productDao.isInStock());
        product.setProductPrice(productDao.getProductPrice());
        product.setUsedBy(productDao.getUsedBy());
        product.setProductQty(productDao.getProductQty());

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(product.getFarmer().getEmail());
        message.setSubject("Your product details "+ product.getProductName() +" is updated in Kishan Application");
        message.setText("Hi "+product.getFarmer().getUsername() +"\n" +
                "Your product " + product.getProductName() + " is added in Kishan Application\n" +
                "\nYour Product Detail is " +
                "\nProduct Name: " +product.getProductName() +
                "\nOrganic/Not: " + product.isOrganic() +
                "\nProduct Price: " +product.getProductPrice() +
                "\nIn Stock: " +product.isInStock() +
                "\nProduct Quantity: " +product.getProductQty() +
                "\nFarmer ID: " + product.getFarmer().getId()+
                "\nFarmer Name: " + product.getFarmer().getUsername()
        );
        javaMailSender.send(message);

        return  productRepo.save(product);


    }

    public String deleteProduct(int productId) {
        logger.info("Inside the ProductService and deletProduct Method");
        SimpleMailMessage message = new SimpleMailMessage();
        Product product = productRepo.findById(productId).orElseThrow(() ->
                new FarmerNotFoundException("Product", "Id", productId));
        message.setTo(product.getFarmer().getEmail());
        logger.info(product.getFarmer().getEmail());
        message.setSubject("Your product "+ product.getProductName() +" is removed from Kishan Application");
        message.setText("Hi "+product.getFarmer().getUsername() +"\n" +
                "Your product "+ product.getProductName() +" is removed from Kishan Application");
        productRepo.deleteById(productId);
        javaMailSender.send(message);
        return "Product Deleted";
    }
}
