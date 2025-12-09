package com.example.MobileStorageManagement.Controller;

import com.example.MobileStorageManagement.DTO.OrderDetailRequest;
import com.example.MobileStorageManagement.DTO.OrderDetailResponse;
import com.example.MobileStorageManagement.Entity.OrderDetail;
import com.example.MobileStorageManagement.Service.OrderDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-details")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    public OrderDetailController(OrderDetailService orderDetailService) {
        this.orderDetailService = orderDetailService;
    }

    // CREATE
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping
    public ResponseEntity<OrderDetailResponse> createOrderDetail(@RequestBody OrderDetailRequest dto) {
        OrderDetail detail = orderDetailService.createOrderDetail(dto);
        return ResponseEntity.ok(orderDetailService.toResponse(detail));
    }

    // GET DETAIL BY ID
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> getOrderDetailById(@PathVariable Long id) {
        OrderDetail detail = orderDetailService.getOrderDetailById(id);
        return ResponseEntity.ok(orderDetailService.toResponse(detail));
    }

    // GET ALL DETAILS BY ORDER ID
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderDetail>> getDetailsByOrderId(@PathVariable Long orderId) {
        List<OrderDetail> details = orderDetailService.getDetailsByOrderID(orderId);
        return ResponseEntity.ok(details);
    }

    // GET ALL
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<OrderDetail>> getAllOrderDetails() {
        List<OrderDetail> list = orderDetailService.getAllOrderDetails();
        return ResponseEntity.ok(list);
    }

    // UPDATE
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<OrderDetail> updateOrderDetail(
            @PathVariable Long id,
            @RequestBody OrderDetailRequest dto
    ) {
        OrderDetail updated = orderDetailService.updateOrderDetail(id, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderDetail(@PathVariable Long id) {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok("OrderDetail đã được xoá thành công");
    }
}
