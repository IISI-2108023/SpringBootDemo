package com.example.springdemo.app.dao;

import com.example.springdemo.app.model.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoListDao extends JpaRepository<TodoList, String> {

    @Query("SELECT t FROM TodoList t WHERE t.title LIKE %:title%")
    List<TodoList> findByTitle(@Param("title") String title);

}
