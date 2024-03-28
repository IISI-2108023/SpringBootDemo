package com.example.springdemo.persistence2.dao;

import com.example.springdemo.persistence2.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDao extends JpaRepository<Product, String> {

}
