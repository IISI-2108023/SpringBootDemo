package com.example.springdemo.app.controller;

import com.example.springdemo.app.model.TodoList;
import com.example.springdemo.app.service.TxService;
import com.example.springdemo.rest.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// [TODELETE] for transaction exercise
@RestController
@RequestMapping("/tx")
public class TxController {

    @Autowired
    TxService txService;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @GetMapping("/findByQuery/{title}")
    public ResponseEntity<List<TodoList>> findByQuery(@PathVariable("title") String title) {
        List<TodoList> list = txService.findTodoListByTitle(title);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/updateTwoDatabase")
    public ResponseEntity<Void> updateTwoDatabase() {
        TodoList todoList = new TodoList();
        todoList.setTitle("JAX");
        todoList.setDescription("create JAX");

        Product product = new Product();
        product.setName("JAX");
        product.setPrice(getRandom());

        txService.updateTwoBeans(todoList, product);
        return ResponseEntity.ok(null);
    }

    private int getRandom() {
        return (int) Math.floor(Math.random()* 100);
    }

    @PostMapping("/testJdbc")
    public ResponseEntity<Void> testJdbc() {
        String sql = "INSERT INTO todolist(oid, title, description, updateTime) VALUES (gen_random_uuid(), 'KOGMOW', :description, NOW())";
        Map<String, Object> map = new HashMap<>();
        map.put("description", "description" + getRandom());
        namedParameterJdbcTemplate.update(sql, map);
        return ResponseEntity.ok(null);
    }

}
