package com.example.MobileStorageManagement.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartID")
    private Integer cartId;

    private String status;

    @OneToOne
    @JoinColumn(name = "UserID") // Foreign key
    private User user;
}
