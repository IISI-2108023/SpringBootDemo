package com.example.springdemo.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "SubList")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubList {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String oid;
    @Column
    private String pid;
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
