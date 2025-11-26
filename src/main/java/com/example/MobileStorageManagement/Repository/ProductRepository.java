package com.example.MobileStorageManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.MobileStorageManagement.Entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
