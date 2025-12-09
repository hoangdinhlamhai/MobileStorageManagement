package com.example.MobileStorageManagement.Controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.MobileStorageManagement.DTO.ProductDTO;
import com.example.MobileStorageManagement.Service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole ('ADMIN') or hasRole('USER')")
    @GetMapping
    public List<ProductDTO> getAll() {
        return productService.getAll();
    }

    @PreAuthorize("hasRole ('ADMIN')")
    @GetMapping("/{id}")
    public ProductDTO getById(@PathVariable Integer id) {
        return productService.getById(id);
    }

    @PreAuthorize("hasRole ('ADMIN')")
    @PostMapping
    public ProductDTO create(@RequestBody ProductDTO dto) {
        return productService.create(dto);
    }

    @PreAuthorize("hasRole ('ADMIN')")
    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable Integer id, @RequestBody ProductDTO dto) {
        return productService.update(id, dto);
    }

    @PreAuthorize("hasRole ('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        productService.delete(id);
    }
}
