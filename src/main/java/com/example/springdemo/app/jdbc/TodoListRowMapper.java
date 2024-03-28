package com.example.springdemo.app.jdbc;

import com.example.springdemo.persistence.model.TodoList;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TodoListRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        TodoList todoList = TodoList.builder()
                .oid(rs.getString("oid"))
                .title(rs.getString("title"))
                .description(rs.getString("description"))
                .updateTime(rs.getTimestamp("updateTime"))
                .status(rs.getString("status"))
                .referenceUrl(rs.getString("referenceUrl"))
                .build();
        return todoList;
    }
}
