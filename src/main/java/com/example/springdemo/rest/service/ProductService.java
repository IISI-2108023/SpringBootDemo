package com.example.springdemo.rest.service;

import com.example.springdemo.rest.dao.MockProductDAO;
import com.example.springdemo.rest.exception.NotFoundException;
import com.example.springdemo.rest.exception.UnprocessableEntityException;
import com.example.springdemo.rest.model.Product;
import com.example.springdemo.rest.param.ProductRequestParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired// DI
    MockProductDAO productDAO;

    public Product createProduct(Product request) {
        boolean isIdDuplicated = productDAO.find(request.getId()).isPresent();
        if (isIdDuplicated) {
            throw new UnprocessableEntityException("The id of the product is duplicated.");
        }

        Product product = new Product();
        product.setId(request.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return productDAO.insert(product);
    }

    public Product getProduct(String id) {
        return productDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Can't find product."));
    }

    public Product replaceProduct(String id, Product request) {
        Product product = getProduct(id);
        return productDAO.replace(product.getId(), request);
    }

    public void deleteProduct(String id) {
        Product product = getProduct(id);
        productDAO.delete(product.getId());
    }

    public List<Product> getProducts(ProductRequestParameter param) {
        return productDAO.find(param);
    }
}
