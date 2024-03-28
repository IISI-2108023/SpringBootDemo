package com.example.springdemo.app.service;

import com.example.springdemo.app.exception.NotFoundException;
import com.example.springdemo.persistence2.dao.ProductDao;
import com.example.springdemo.persistence2.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired// DI
    ProductDao productDao;

    public Product createProduct(Product request) {
//        boolean isIdDuplicated = productDao.findById(request.getId()).isPresent();
//        if (isIdDuplicated) {
//            throw new UnprocessableEntityException("The id of the product is duplicated.");
//        }
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        return productDao.save(product);
    }

    public Product getProduct(String id) {
        return productDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Can't find product."));
    }

    public Product replaceProduct(String id, Product request) {
        Product product = getProduct(id);
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        return productDao.save(product);
    }

    public void deleteProduct(String id) {
        Product product = getProduct(id);
        productDao.deleteById(product.getId());
    }

//    public List<Product> getProducts(ProductRequestParameter param) {
//        return productDao.find(param);
//    }
}
