package com.example.MobileStorageManagement.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.MobileStorageManagement.DTO.CartDTO;
import com.example.MobileStorageManagement.Service.CartService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("")
    public ResponseEntity<List<CartDTO>> getAllCart() {
        return ResponseEntity.ok(this.cartService.getAllCarts());
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getCartByUser(@PathVariable Integer id) {
        return ResponseEntity.ok(this.cartService.getCartByUserId(id));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("")
    public ResponseEntity<CartDTO> create(@RequestBody CartDTO dto) {
        return ResponseEntity.ok(cartService.createCart(dto));
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCartByUser(@PathVariable Integer id) {
        return ResponseEntity.ok("Cart deleted successfully");
    }
}