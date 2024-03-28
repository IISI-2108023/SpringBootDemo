package com.example.springdemo.persistence2.dao;

import com.example.springdemo.persistence2.model.Product;
import com.example.springdemo.app.param.ProductRequestParameter;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository // 標記為持久層
public class MockProductDAO {
    private final List<Product> productDB = new ArrayList<>();

    @PostConstruct // DI 完成後會執行一次
    private void initDB() {
        productDB.add(new Product("B0001", "Android Development (Java)", 380));
        productDB.add(new Product("B0002", "Android Development (Kotlin)", 420));
        productDB.add(new Product("B0003", "Data Structure (Java)", 250));
        productDB.add(new Product("B0004", "Finance Management", 450));
        productDB.add(new Product("B0005", "Human Resource Management", 330));
    }

    public Product insert(Product product) {
        productDB.add(product);
        return product;
    }

    public Optional<Product> find(String id) {
        return productDB.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public List<Product> find(ProductRequestParameter param) {
        String keyword = Optional.ofNullable(param.getSearchKey()).orElse("") ;
        String orderBy = param.getSortedField();
        String sortRule = param.getSortDir();
        Comparator<Product> comparator = genSortComparator(orderBy,sortRule);

        return productDB.stream()
                .filter(p -> p.getName().contains(keyword))
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public Product replace(String id, Product product) {
        Optional<Product> productO = this.find(id);
        productO.ifPresent(p -> {
            p.setName(product.getName());
            p.setPrice(product.getPrice());
        });
        return product;
    }

    public void delete(String id) {
        productDB.removeIf(p -> p.getId().equals(id));
    }

    public Comparator<Product> genSortComparator(String orderBy, String sortRule) {
        Comparator<Product> comparator = (p1, p2) -> 0;
        if (Objects.isNull(orderBy) || Objects.isNull(sortRule)) {
            return comparator;
        }
        if (orderBy.equalsIgnoreCase("price")) {
            comparator = Comparator.comparing(Product::getPrice);
        } else if (orderBy.equalsIgnoreCase("name")) {
            comparator = Comparator.comparing(Product::getName);
        }
        return sortRule.equals("desc") ?
                comparator.reversed() : comparator;
    }
}
