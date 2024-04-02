package com.example.springdemo.app.controller;

import com.example.springdemo.app.service.ListService;
import com.example.springdemo.app.service.TxService;
import com.example.springdemo.persistence.model.TodoList;
import com.example.springdemo.persistence2.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.List;

// [TODELETE] for transaction exercise
@RestController
@RequestMapping("/tx")
public class TxController {

    @Autowired
    TxService txService;

    @Autowired
    ListService listService;

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    JdbcTemplate postgresJdbcTemplate;

    @Autowired
    @Qualifier("secondJdbcTemplate")
    JdbcTemplate secondJdbcTemplate;

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

        Product product = Product.builder()
                .name("Product meow")
                .price(getRandom())
                .build();

        txService.updateTwoBeans(todoList, product);
        return ResponseEntity.ok(null);
    }

    private int getRandom() {
        return (int) Math.floor(Math.random() * 100);
    }


    // JDBC 應該是放 Dao，練習用先放這
    @PostMapping("/testJdbcUpdate")
    public ResponseEntity<Void> testJdbcUpdate() {
        String sql = "INSERT INTO todolist(oid, title, description, updateTime) VALUES (gen_random_uuid(), 'KOGMOW', ?, NOW())";
        postgresJdbcTemplate.update(sql, getRandom() + "test description333333");
        return ResponseEntity.ok(null);
    }

    @PostMapping("/testJdbcQuery")
    public ResponseEntity<List<Product>> testJdbcQuery() {
        String sql = "SELECT * FROM product";
//        List<TodoList> list = postgresTemplate.query(sql, new TodoListRowMapper());
        List<Product> list = secondJdbcTemplate.query(sql, new RowMapper<Product>() {
            @Override
            public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
                Product product = Product.builder()
                        .name("JDBC")
                        .price(1)
                        .build();
                return product;
            }
        });
        return ResponseEntity.ok(list);
    }

    @GetMapping("/testJdbcPS/{title}")
    public ResponseEntity<TodoList> testJdbcPS(@PathVariable String title) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int result = postgresJdbcTemplate.update(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                        String sql = "INSERT INTO todolist(oid, title, description, updateTime) VALUES (gen_random_uuid(), ?, 'add by preparestatment', NOW())";
                        PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"oid"});
                        preparedStatement.setString(1, title);
                        return preparedStatement;
                    }
                }, keyHolder
        );
        System.out.println(keyHolder.getKeyList().get(0));
        String oid = keyHolder.getKeyAs(String.class);
        TodoList todoList = listService.getTodoList(oid);
        return ResponseEntity.ok(todoList);
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> countTodoList() {
        int count = txService.countTodoList();
        return ResponseEntity.ok(count);
    }

    @DeleteMapping("/delete/{oid}")
    public ResponseEntity<Void> deleteTodoListByOid(@PathVariable String oid) {
        if (StringUtils.hasLength(oid)) {
            txService.deleteTodoListByOid(oid);
        }
        return ResponseEntity.ok(null);
    }

    @GetMapping("/findByTitleIgnoreCaseOrderByTitle/{title}")
    public ResponseEntity<List<TodoList>> findByTitleIgnoreCaseOrderByTitle(@PathVariable("title") String title) {
        List<TodoList> list = txService.findByTitleIgnoreCaseOrderByTitle(title);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/findTitleDistinct")
    public ResponseEntity<List<String>> findTitleDistinct() {
        List<String> list = txService.findTitleDistinct();
        return ResponseEntity.ok(list);
    }
}
