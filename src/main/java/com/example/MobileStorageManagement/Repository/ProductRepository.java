package com.example.MobileStorageManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.MobileStorageManagement.Entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("""
    SELECT DISTINCT p
    FROM Product p
    LEFT JOIN FETCH p.productImages
    LEFT JOIN FETCH p.specification
    WHERE (:categoryId IS NULL OR :categoryId = 0 OR p.category.categoryId = :categoryId)
      AND (
        :keyword IS NULL 
        OR :keyword = '' 
        OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
    """)
    List<Product> searchProduct(
            @Param("categoryId") Integer categoryId,
            @Param("keyword") String keyword
    );

    // Lấy tất cả sản phẩm còn hàng
    List<Product> findByStockQuantityGreaterThan(Integer stockQuantity);

    // Fallback: lấy 3 sản phẩm rẻ nhất còn hàng
    List<Product> findTop3ByStockQuantityGreaterThanOrderByPriceAsc(Integer stockQuantity);
}
