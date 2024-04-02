package com.example.springdemo.persistence.dao;

import com.example.springdemo.persistence.model.TodoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoListDao extends JpaRepository<TodoList, String>, CustTodoListDao {

    // @Query
    @Query("SELECT t FROM TodoList t WHERE LOWER(t.title) LIKE '%' || LOWER(:title) || '%'")
    List<TodoList> findByTitleIgnoreCase(@Param("title") String title);

    @Query("SELECT count(oid) FROM TodoList")
    int countAll();

    // Query builder
    List<TodoList> findByTitleIgnoreCaseOrderByTitle(String title);

    Streamable<TodoList> findDistinctTitleByStatus(String status);
}
