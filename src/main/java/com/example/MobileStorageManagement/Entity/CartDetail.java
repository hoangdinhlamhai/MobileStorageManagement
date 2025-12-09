package com.example.MobileStorageManagement.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartDetailsId;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false) // Foreign key column
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false) // Foreign key column
    private Product product;
}
