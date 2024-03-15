package com.example.springdemo.rest.dao;

import com.example.springdemo.rest.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDao extends CrudRepository<Product, String> {

}
