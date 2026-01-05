package com.example.MobileStorageManagement.Service;

import com.example.MobileStorageManagement.DTO.OrderDetailRequest;
import com.example.MobileStorageManagement.DTO.OrderDetailResponse;
import com.example.MobileStorageManagement.DTO.StockOutResponse;
import com.example.MobileStorageManagement.Entity.Order;
import com.example.MobileStorageManagement.Entity.OrderDetail;
import com.example.MobileStorageManagement.Entity.Product;
import com.example.MobileStorageManagement.Entity.StockOut;
import com.example.MobileStorageManagement.Repository.OrderDetailRepository;
import com.example.MobileStorageManagement.Repository.OrderRepository;
import com.example.MobileStorageManagement.Repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderDetailService(OrderDetailRepository orderDetailRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderDetailRepository = orderDetailRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    // CREATE
    public OrderDetail createOrderDetail(OrderDetailRequest dto) {

        Order order = orderRepository.findById(dto.getOrderID())
                .orElseThrow(() -> new RuntimeException("Order không tồn tại"));

        Product product = productRepository.findById(dto.getProductID())
                .orElseThrow(() -> new RuntimeException("Product không tồn tại"));

        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .quantity(dto.getQuantity())
                .build();

        return orderDetailRepository.save(orderDetail);
    }

    // GET BY ID
    public OrderDetail getOrderDetailById(Long id) {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderDetail không tồn tại"));
    }

    // GET ALL BY ORDER ID
    public List<OrderDetail> getDetailsByOrderID(Long orderID) {
        return orderDetailRepository.findByOrder_OrderID(orderID);
    }

    // GET ALL
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    // UPDATE
    public OrderDetail updateOrderDetail(Long id, OrderDetailRequest dto) {
        OrderDetail detail = getOrderDetailById(id);

        // update quantity
        detail.setQuantity(dto.getQuantity());

        // update order nếu gửi OrderID khác
        if (dto.getOrderID() != null) {
            Order newOrder = orderRepository.findById(dto.getOrderID())
                    .orElseThrow(() -> new RuntimeException("Order không tồn tại"));
            detail.setOrder(newOrder);
        }

        // update product nếu gửi ProductID khác
        if (dto.getProductID() != null) {
            Product newProduct = productRepository.findById(dto.getProductID())
                    .orElseThrow(() -> new RuntimeException("Product không tồn tại"));
            detail.setProduct(newProduct);
        }

        return orderDetailRepository.save(detail);
    }

    // DELETE
    public void deleteOrderDetail(Long id) {
        OrderDetail detail = getOrderDetailById(id);
        orderDetailRepository.delete(detail);
    }

    public OrderDetailResponse toResponse(OrderDetail orderDetail) {
        OrderDetailResponse dto = new OrderDetailResponse();

        dto.setId(orderDetail.getOrderDetailID());
        dto.setQuantity(orderDetail.getQuantity());

        if (orderDetail.getOrder() != null) {
            dto.setOrderId(orderDetail.getOrder().getOrderID());
        }

        if (orderDetail.getProduct() != null) {
            dto.setProductId(orderDetail.getProduct().getProductId());
        }

        return dto;
    }

}
