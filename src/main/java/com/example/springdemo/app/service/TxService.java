package com.example.springdemo.app.service;

import com.example.springdemo.persistence.dao.TodoListDao;
import com.example.springdemo.persistence.model.TodoList;
import com.example.springdemo.persistence2.dao.ProductDao;
import com.example.springdemo.persistence2.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

// [TODELETE] for transaction exercise
@Service
public class TxService {

    @Autowired
    TodoListDao todoListDao;

    @Autowired
    ProductDao productDao;

    public List<TodoList> findTodoListByTitle(String title) {
        return todoListDao.findByTitleIgnoreCase(title);
    }

    @Transactional
    public void updateTwoBeans(TodoList todoList, Product product) {
        todoListDao.save(todoList);
        productDao.save(product);
    }

    public int countTodoList() {
        return todoListDao.countAll();
    }

    public void deleteTodoListByOid(String oid) {
        todoListDao.deleteByOid(oid);
    }

    public List<TodoList> findByTitleIgnoreCaseOrderByTitle(String title) {
        return todoListDao.findByTitleIgnoreCaseOrderByTitle(title);
    }

    public List<String> findTitleDistinct() {
        List<String> list = new ArrayList<>();
        todoListDao.findDistinctTitleByStatus("0").toSet().forEach(e -> {
            list.add(e.getTitle());
        });
        return list;
    }
}
