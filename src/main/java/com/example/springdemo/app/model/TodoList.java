package com.example.springdemo.app.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name="TodoList")
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

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReferenceUrl() {
        return referenceUrl;
    }

    public void setReferenceUrl(String referenceUrl) {
        this.referenceUrl = referenceUrl;
    }

}
