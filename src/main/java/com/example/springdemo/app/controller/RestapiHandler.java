package com.example.springdemo.app.controller;

import com.example.springdemo.persistence.model.TodoList;
import com.example.springdemo.app.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class RestapiHandler {
    @Autowired
    ListService listService;

    @GetMapping("/todolist/{oid}")
    public ResponseEntity<TodoList> getTodoList(@PathVariable("oid") String oid) {
        TodoList todoList = listService.getTodoList(oid);
        return ResponseEntity.ok(todoList);
    }

    @GetMapping("/todolist")
    public ResponseEntity<List<TodoList>> getTodoLists() {
        List<TodoList> list = listService.getAllTodoList();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/todolist")
    public ResponseEntity<TodoList> createTodoList(@RequestBody TodoList requestTodoList) {
        TodoList todoList = listService.createTodoList(requestTodoList);
        return ResponseEntity.ok(todoList);
    }

    @PutMapping("/todolist")
    public ResponseEntity<TodoList> updateTodoList(@RequestBody TodoList requestTodoList) {
        TodoList todoList = listService.updateTodoList(requestTodoList.getOid(), requestTodoList);
        return ResponseEntity.ok(todoList);
    }

    @DeleteMapping("/todolist/{oid}")
    public ResponseEntity<Void> deleteTodoList(@PathVariable("oid") String oid) {
        listService.deleteTodoList(oid);
        return ResponseEntity.noContent().build();
    }
}
