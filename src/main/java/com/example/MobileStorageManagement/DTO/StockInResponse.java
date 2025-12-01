package com.example.MobileStorageManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class StockInResponse {

    private Long stockInID;
    private Long batchID;
    private Integer quantity;
    private String note;
    private Integer userId;
    private LocalDateTime date;

    public StockInResponse() {
    }

    public Long getStockInID() {
        return stockInID;
    }

    public void setStockInID(Long stockInID) {
        this.stockInID = stockInID;
    }

    public Long getBatchID() {
        return batchID;
    }

    public void setBatchID(Long batchID) {
        this.batchID = batchID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
