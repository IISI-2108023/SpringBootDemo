package com.example.springdemo.persistence.dao.impl;

import com.example.springdemo.persistence.dao.CustTodoListDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

public class CustTodoListDaoImpl implements CustTodoListDao {

    @Autowired
    @Qualifier("postgresJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    public void deleteByOid(String oid) {
        String sql = "DELETE FROM TodoList WHERE oid = ?";
        jdbcTemplate.update(sql, oid);
    }
}
