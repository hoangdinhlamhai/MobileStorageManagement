package com.example.MobileStorageManagement.Entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "`order`") // tên bảng gợi ý, bạn đổi tùy DB
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID")
    private Long orderID;

    @Column(name = "Order_Date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "Status", nullable = false)
    private String status;

    @Column(name = "PaymentStatus", nullable = false)
    private String paymentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

}
