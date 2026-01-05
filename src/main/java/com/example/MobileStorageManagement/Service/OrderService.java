package com.example.MobileStorageManagement.Service;

import com.example.MobileStorageManagement.DTO.OrderFullResponse;
import com.example.MobileStorageManagement.DTO.OrderProductDTO;
import com.example.MobileStorageManagement.DTO.OrderRequest;
import com.example.MobileStorageManagement.DTO.OrderResponse;
import com.example.MobileStorageManagement.Entity.Order;
import com.example.MobileStorageManagement.Entity.ProductImage;
import com.example.MobileStorageManagement.Entity.User;
import com.example.MobileStorageManagement.Repository.OrderRepository;
import com.example.MobileStorageManagement.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // CREATE
    public Order createOrder(OrderRequest dto) {

        User user = userRepository.findById(dto.getUserID())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        Order order = Order.builder()
                .orderDate(dto.getOrderDate() != null ? dto.getOrderDate() : LocalDateTime.now())
                .status(dto.getStatus())
                .paymentStatus(dto.getPaymentStatus())
                .user(user)
                .build();

        return orderRepository.save(order);
    }

    // GET BY ID
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order không tồn tại"));
    }

    @Transactional(readOnly = true)
    public List<OrderFullResponse> getOrdersByUser(Integer userId) {
        return orderRepository.findByUser_UserId(userId)
                .stream()
                .map(this::toFullResponse)
                .toList();
    }


    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderService::toResponse)
                .toList();
    }

    // UPDATE
    public Order updateOrder(Long id, OrderRequest dto) {
        Order order = getOrderById(id);

        if (dto.getOrderDate() != null) {
            order.setOrderDate(dto.getOrderDate());
        }

        if (dto.getStatus() != null) {
            order.setStatus(dto.getStatus());
        }

        if (dto.getPaymentStatus() != null) {
            order.setPaymentStatus(dto.getPaymentStatus());
        }

        return orderRepository.save(order);
    }


    // DELETE
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        orderRepository.delete(order);
    }

    public OrderFullResponse toFullResponse(Order order) {
        OrderFullResponse res = new OrderFullResponse();
        res.setOrderID(order.getOrderID());
        res.setOrderDate(order.getOrderDate());
        res.setStatus(order.getStatus());
        res.setPaymentStatus(order.getPaymentStatus());
        res.setUserID(order.getUser().getUserId());

        List<OrderProductDTO> products = order.getOrderDetails().stream()
                .map(od -> {
                    OrderProductDTO p = new OrderProductDTO();
                    p.setProductID(od.getProduct().getProductId());
                    p.setName(od.getProduct().getName());
                    p.setPrice(od.getProduct().getPrice());
                    p.setQuantity(od.getQuantity());

                    String imageUrl = od.getProduct().getProductImages()
                            .stream()
                            .filter(img -> img.getImg_index() == 0)
                            .map(ProductImage::getUrl)
                            .findFirst()
                            .orElse(null);

                    p.setImageUrl(imageUrl);

                    return p;
                })
                .toList();

        res.setProducts(products);
        return res;
    }


    public static OrderResponse toResponse(Order order) {
        OrderResponse res = new OrderResponse();
        res.setOrderID(order.getOrderID());
        res.setStatus(order.getStatus());
        res.setPaymentStatus(order.getPaymentStatus());
        res.setUserID(order.getUser().getUserId());
        return res;
    }
}
