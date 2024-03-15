package com.example.springdemo.app.service;

import com.example.springdemo.app.dao.TodoListDao;
import com.example.springdemo.app.model.TodoList;
import com.example.springdemo.rest.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ListService {

    @Autowired
    TodoListDao todoListDao;

    public TodoList saveTodoList(TodoList todoList) {
        return todoListDao.save(todoList);
    }
    public List<TodoList> saveTodoLists(List<TodoList> list) {
        return todoListDao.saveAll(list);
    }
    public TodoList getTodoList(String oid) {
        return todoListDao.findById(oid).orElseThrow(() -> new NotFoundException("Cannot find todoList!"));
    }
    public List<TodoList> getAllTodoList() {
        return todoListDao.findAll();
    }
    public void deleteTodoList(String oid) {
        todoListDao.delete(getTodoList(oid));
    }

    public TodoList createTodoList(TodoList todoList) {
        todoList.setOid(null);
        todoList.setStatus("0");
        todoList.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return todoListDao.save(todoList);
    }

    public TodoList updateTodoList(String oid, TodoList todoList) {
        TodoList originTodoList = getTodoList(oid);
        originTodoList.setStatus(todoList.getStatus());
        originTodoList.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        originTodoList.setDescription(todoList.getDescription());
        originTodoList.setTitle(todoList.getTitle());
        originTodoList.setReferenceUrl(todoList.getReferenceUrl());
        return saveTodoList(originTodoList);
    }
}
