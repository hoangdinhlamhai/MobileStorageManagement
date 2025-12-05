package com.example.MobileStorageManagement.Repository;

import com.example.MobileStorageManagement.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByOrder_OrderID(Long orderID);
}
