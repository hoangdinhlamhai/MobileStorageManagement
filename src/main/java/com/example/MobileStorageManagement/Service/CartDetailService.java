package com.example.MobileStorageManagement.Service;

import java.util.List;
import java.util.Optional;

import com.example.MobileStorageManagement.DTO.CartDetailResponse;
import com.example.MobileStorageManagement.DTO.StockInResponse;
import com.example.MobileStorageManagement.Entity.StockIn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.MobileStorageManagement.DTO.CartDetailRequestDTO;
import com.example.MobileStorageManagement.Entity.Cart;
import com.example.MobileStorageManagement.Entity.CartDetail;
import com.example.MobileStorageManagement.Entity.Product;
import com.example.MobileStorageManagement.Repository.CartDetailRepository;
import com.example.MobileStorageManagement.Repository.CartRepository;
import com.example.MobileStorageManagement.Repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class CartDetailService {
    private final CartDetailRepository cartDetailRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public List<CartDetail> getAll() {
        return cartDetailRepository.findAll();
    }

    public Optional<CartDetail> getById(Integer id) {
        return cartDetailRepository.findById(id);
    }

    public List<CartDetail> getByCartId(Integer cartId) {
        return cartDetailRepository.findByCartCartId(cartId);
    }

    public CartDetail createCartDetail(CartDetailRequestDTO dto) {
        System.out.println("Creating CartDetail with cartId=" + dto.getCartId() + ", productId=" + dto.getProductId());
        // Lấy Cart theo cartId
        Cart cart = cartRepository.findById(dto.getCartId())
                .orElseThrow(() -> new RuntimeException("Cart not found with id " + dto.getCartId()));

        // Lấy Product theo productId
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id " + dto.getProductId()));

        // Tạo CartDetail
        CartDetail cartDetail = CartDetail.builder()
                .cart(cart)
                .product(product)
                .build();

        System.out.println("Created CartDetail: " + dto.getCartId() + ", " + dto.getProductId());

        return cartDetailRepository.save(cartDetail);
    }

    public CartDetail update(Integer id, CartDetailRequestDTO dto) {
        return cartDetailRepository.findById(id).map(existing -> {

            Cart cart = cartRepository.findById(dto.getCartId())
                    .orElseThrow(() -> new RuntimeException("Cart not found"));

            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            existing.setCart(cart);
            existing.setProduct(product);

            return cartDetailRepository.save(existing);

        }).orElseThrow(() -> new RuntimeException("CartDetail not found with id " + id));
    }


    public void delete(Integer id) {
        cartDetailRepository.deleteById(id);
    }

    public CartDetailResponse toResponse(CartDetail cartDetail) {

        CartDetailResponse dto = new CartDetailResponse();
        dto.setCartDetailsId(cartDetail.getCartDetailsId());
        dto.setCartId(cartDetail.getCart().getCartId());
        dto.setProductId(cartDetail.getProduct().getProductId());
        return dto;
    }
}
