package com.example.MobileStorageManagement.Service;

import com.example.MobileStorageManagement.DTO.OrderRequest;
import com.example.MobileStorageManagement.DTO.OrderResponse;
import com.example.MobileStorageManagement.Entity.Order;
import com.example.MobileStorageManagement.Entity.User;
import com.example.MobileStorageManagement.Repository.OrderRepository;
import com.example.MobileStorageManagement.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    // GET ALL ORDER BY USER
    public List<Order> getOrdersByUser(Integer userID) {
        return orderRepository.findByUser_UserId(userID);
    }

    // GET ALL
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
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

    public static OrderResponse toResponse(Order order) {
        OrderResponse res = new OrderResponse();
        res.setOrderID(order.getOrderID());
        res.setStatus(order.getStatus());
        res.setPaymentStatus(order.getPaymentStatus());
        res.setUserID(order.getUser().getUserId());
        return res;
    }

}
