package com.example.springdemo.app.controller;

import com.example.springdemo.app.service.TxService;
import com.example.springdemo.persistence.model.TodoList;
import com.example.springdemo.persistence2.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

// [TODELETE] for transaction exercise
@RestController
@RequestMapping("/tx")
public class TxController {

    @Autowired
    TxService txService;

    @Autowired
    @Qualifier("postgresTemplate")
    JdbcTemplate postgresTemplate;

    @Autowired
    @Qualifier("secondTemplate")
    JdbcTemplate secondTemplate;

    @GetMapping("/findByQuery/{title}")
    public ResponseEntity<List<TodoList>> findByQuery(@PathVariable("title") String title) {
        List<TodoList> list = txService.findTodoListByTitle(title);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/updateTwoDatabase")
    public ResponseEntity<Void> updateTwoDatabase() {
        TodoList todoList = TodoList.builder()
                .title("JAX")
                .description("create JAX with lombok")
                .updateTime(new Timestamp(System.currentTimeMillis()))
                .build();

        Product product = new Product();
        product.setName("JAX");
        product.setPrice(getRandom());

        txService.updateTwoBeans(todoList, product);
        return ResponseEntity.ok(null);
    }

    private int getRandom() {
        return (int) Math.floor(Math.random() * 100);
    }

    @PostMapping("/testJdbc")
    public ResponseEntity<Void> testJdbc() {
        String sql = "INSERT INTO todolist(oid, title, description, updateTime) VALUES (gen_random_uuid(), 'KOGMOW', ?, NOW())";
        postgresTemplate.update(sql, getRandom() + "test description");
        return ResponseEntity.ok(null);
    }

}
