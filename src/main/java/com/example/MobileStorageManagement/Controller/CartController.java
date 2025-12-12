package com.example.MobileStorageManagement.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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

    @GetMapping("")
    public ResponseEntity<List<CartDTO>> getAllCart() {
        return ResponseEntity.ok(this.cartService.getAllCarts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getCartByUser(@PathVariable Integer id) {
        return ResponseEntity.ok(this.cartService.getCartByUserId(id));
    }

    @PostMapping
    public ResponseEntity<CartDTO> create(@RequestBody CartDTO dto) {
        return ResponseEntity.ok(cartService.createCart(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCartByUser(@PathVariable Integer id) {
        cartService.deleteCartByUserId(id);
        return ResponseEntity.ok("Deleted cart of userId = " + id);
    }


}