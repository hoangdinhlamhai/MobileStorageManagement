package com.example.MobileStorageManagement.DTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderFullResponse {
    private Long orderID;
    private LocalDateTime orderDate;
    private String status;
    private String paymentStatus;
    private Integer userID;
    private List<OrderProductDTO> products;
}
