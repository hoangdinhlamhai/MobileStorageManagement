package com.example.MobileStorageManagement.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;

    private String name;

    private Double price;

    private Integer stockQuantity;

    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, // Lưu, cập nhật, xóa ProductImage cùng với Product
            fetch = FetchType.LAZY, orphanRemoval = true // Xóa ProductImage nếu nó không còn liên kết với Product nào
    )
    private List<ProductImage> productImages = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "brand_id") // Foreign key column
    @JsonIgnore
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id") // Foreign key column
    @JsonIgnore
    private Category category;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "spec_id") // Foreign key column
    private Specification specification;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss a", timezone = "GMT+7")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss a", timezone = "GMT+7")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
