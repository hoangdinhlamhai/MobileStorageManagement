package com.example.MobileStorageManagement.Controller;

import com.example.MobileStorageManagement.DTO.StockInRequest;
import com.example.MobileStorageManagement.DTO.StockInResponse;
import com.example.MobileStorageManagement.Entity.StockIn;
import com.example.MobileStorageManagement.Service.StockInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stockin")
public class StockinController {

    @Autowired
    private StockInService stockInService;

    // GET ALL
    @PreAuthorize("hasRole ('ADMIN')")
    @GetMapping
    public ResponseEntity<List<StockInResponse>> getAll() {
        List<StockInResponse> list = stockInService.getAll().stream()
                .map(stockInService::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // GET BY ID
    @PreAuthorize("hasRole ('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<StockInResponse> getById(@PathVariable Integer id) {
        StockIn stock = stockInService.getById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập"));

        return ResponseEntity.ok(stockInService.toResponse(stock));
    }

    // CREATE
    @PreAuthorize("hasRole ('ADMIN')")
    @PostMapping
    public ResponseEntity<StockInResponse> create(@RequestBody StockInRequest request) throws IOException {
        StockIn created = stockInService.create(request);
        return ResponseEntity.ok(stockInService.toResponse(created));
    }

    // UPDATE
    @PreAuthorize("hasRole ('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<StockInResponse> update(
            @PathVariable Integer id,
            @RequestBody StockInRequest request
    ) throws IOException {

        StockIn updated = stockInService.update(id, request);
        return ResponseEntity.ok(stockInService.toResponse(updated));
    }

    // DELETE
    @PreAuthorize("hasRole ('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        stockInService.delete(id);
        return ResponseEntity.ok("Đã xoá phiếu nhập hàng ID = " + id);
    }
}
