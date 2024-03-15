package com.example.springdemo.app.dao;

import com.example.springdemo.app.model.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoListDao extends JpaRepository<TodoList, String> {

}
