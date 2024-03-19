package com.example.springdemo.rest.dao;

import com.example.springdemo.rest.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDao extends JpaRepository<Product, String> {

}
