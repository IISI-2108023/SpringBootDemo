package com.example.springdemo.app.service;

import com.example.springdemo.app.dao.TodoListDao;
import com.example.springdemo.app.model.TodoList;
import com.example.springdemo.rest.dao.ProductDao;
import com.example.springdemo.rest.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

// [TODELETE] for transaction exercise
@Service
public class TxService {

    @Autowired
    TodoListDao todoListDao;

    @Autowired
    ProductDao productDao;

    public List<TodoList> findTodoListByTitle(String title) {
        if (StringUtils.hasLength(title)) {
            return todoListDao.findByTitle(title);
        }
        return null;
    }
    @Transactional
    public void updateTwoBeans(TodoList todoList, Product product) {
        todoListDao.save(todoList);
        productDao.save(product);
    }
}
