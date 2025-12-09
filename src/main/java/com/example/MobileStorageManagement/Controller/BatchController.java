package com.example.MobileStorageManagement.Controller;

import com.example.MobileStorageManagement.DTO.BatchRequest;
import com.example.MobileStorageManagement.DTO.BatchResponse;
import com.example.MobileStorageManagement.Service.BatchService;
import com.example.MobileStorageManagement.Entity.Batch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/batch")
public class BatchController {

    @Autowired
    private BatchService batchService;

    // Lấy tất cả batch
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping
    public List<Batch> getAll() {
        return batchService.getAll();
    }

    // Lấy batch theo ID
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{id}")
    public BatchResponse getById(@PathVariable Long id) {
        return batchService.getById(id)
                .map(batch -> {
                    // convert tạm (controller không nên tự convert nên tốt nhất chuyển sang service)
                    BatchResponse dto = new BatchResponse();
                    dto.setBatchID(batch.getBatchID());
                    dto.setProductID(batch.getProduct().getProductId());
                    dto.setProductionDate(batch.getProductionDate());
                    dto.setQuantity(batch.getQuantity());
                    dto.setPriceIn(batch.getPriceIn());
                    dto.setExpiry(batch.getExpiry());
                    return dto;
                })
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lô hàng"));
    }

    // Tạo batch mới
    @PreAuthorize("hasRole ('ADMIN')")
    @PostMapping
    public BatchResponse create(@RequestBody BatchRequest request) throws IOException {
        return batchService.create(request);
    }

    // Update batch
    @PreAuthorize("hasRole ('ADMIN')")
    @PutMapping("/{id}")
    public BatchResponse update(
            @PathVariable Long id,
            @RequestBody BatchRequest request
    ) {
        return batchService.update(id, request);
    }

    // Xóa batch
    @PreAuthorize("hasRole ('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        batchService.delete(id);
    }

}
