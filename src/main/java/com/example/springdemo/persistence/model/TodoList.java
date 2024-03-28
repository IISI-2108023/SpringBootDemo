package com.example.springdemo.persistence.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Table(name="TodoList")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoList {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String oid;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private Timestamp updateTime;
    /**
     * 狀態: 進行中0、已完成1
     */
    @Column(length = 1)
    private String status;
    @Column
    private String referenceUrl;
}
