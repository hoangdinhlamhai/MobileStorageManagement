package com.example.MobileStorageManagement.Service;

import com.example.MobileStorageManagement.DTO.*;
import com.example.MobileStorageManagement.Entity.*;
import com.example.MobileStorageManagement.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final DiscountService discountService;

    /* ================= CREATE ================= */

    @Transactional
    public Order createOrder(OrderRequest dto) {

        // 1️⃣ Check user
        User user = userRepository.findById(dto.getUserID())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new RuntimeException("Order phải có ít nhất 1 sản phẩm");
        }

        // 2️⃣ Tạo order (backend quyết định)
        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .status("PENDING")
                .paymentStatus("UNPAID")
                .user(user)
                .build();

        orderRepository.save(order);

        // 3️⃣ OrderDetails + subtotal
        double subTotal = 0;
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (OrderItemRequest item : dto.getItems()) {

            if (item.getQuantity() <= 0) {
                throw new RuntimeException("Số lượng phải > 0");
            }

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product không tồn tại"));

            OrderDetail detail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .quantity(item.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();

            subTotal += detail.getUnitPrice() * detail.getQuantity();
            orderDetails.add(detail);
        }

        orderDetailRepository.saveAll(orderDetails);
        order.setOrderDetails(orderDetails);

        // 4️⃣ Discount (optional – không user)
        double discountAmount = 0;

        if (dto.getDiscountCode() != null && !dto.getDiscountCode().isBlank()) {

            Discount discount = discountService.validate(
                    dto.getDiscountCode(), subTotal
            );

            discountAmount = discountService.calculate(discount, subTotal);

            // snapshot discount
            order.setDiscountCode(discount.getCode());
            order.setDiscountType(discount.getType());
            order.setDiscountValue(discount.getValue());
            order.setDiscountAmount(discountAmount);

            discountService.markUsed(discount);
        }

        // 5️⃣ Set tiền
        order.setSubTotal(subTotal);
        order.setTotalAmount(subTotal - discountAmount);

        return orderRepository.save(order);
    }

    /* ================= GET ================= */

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

    /* ================= UPDATE ================= */

    @Transactional
    public Order updateOrder(Long id, OrderRequest dto) {

        Order order = getOrderById(id);

        // chỉ cho phép admin / hệ thống update
        if (dto.getStatus() != null) {
            order.setStatus(dto.getStatus());
        }

        if (dto.getPaymentStatus() != null) {
            order.setPaymentStatus(dto.getPaymentStatus());
        }

        return orderRepository.save(order);
    }

    /* ================= DELETE ================= */

    @Transactional
    public void deleteOrder(Long id) {
        Order order = getOrderById(id);
        orderRepository.delete(order);
    }

    /* ================= MAPPER ================= */

    private OrderFullResponse toFullResponse(Order order) {

        OrderFullResponse res = new OrderFullResponse();
        res.setOrderID(order.getOrderID());
        res.setOrderDate(order.getOrderDate());
        res.setStatus(order.getStatus());
        res.setPaymentStatus(order.getPaymentStatus());
        res.setUserID(order.getUser().getUserId());
        res.setSubTotal(order.getSubTotal());
        res.setDiscountAmount(order.getDiscountAmount());
        res.setTotalAmount(order.getTotalAmount());

        List<OrderProductDTO> products = order.getOrderDetails()
                .stream()
                .map(od -> {
                    OrderProductDTO p = new OrderProductDTO();
                    p.setProductID(od.getProduct().getProductId());
                    p.setName(od.getProduct().getName());
                    p.setPrice(od.getUnitPrice());
                    p.setQuantity(od.getQuantity());

                    String imageUrl = od.getProduct()
                            .getProductImages()
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
