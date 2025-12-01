package com.example.MobileStorageManagement.Service;

import com.example.MobileStorageManagement.DTO.StockInRequest;
import com.example.MobileStorageManagement.DTO.StockInResponse;
import com.example.MobileStorageManagement.Entity.Batch;
import com.example.MobileStorageManagement.Entity.StockIn;
import com.example.MobileStorageManagement.Entity.User;
import com.example.MobileStorageManagement.Repository.BatchRepository;
import com.example.MobileStorageManagement.Repository.StockInRepository;
import com.example.MobileStorageManagement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class StockInService {

    @Autowired
    private StockInRepository stockInRepository;

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<StockIn> getById(Integer id) {
        return stockInRepository.findById(id);
    }

    public StockIn create(StockInRequest stockInRequest) throws IOException {

        StockIn dto = new StockIn();
        dto.setQuantity(stockInRequest.getQuantity());
        dto.setDate(stockInRequest.getDate());
        dto.setNote(stockInRequest.getNote());

        // batch
        if (stockInRequest.getBatchId() != null) {
            Batch batch = batchRepository.findById(stockInRequest.getBatchId())
                    .orElseThrow(() -> new RuntimeException("Lô hàng không tồn tại"));
            dto.setBatch(batch);
        }

        // tìm user dựa vào giải token lấy mail hoặc sdt rồi truy ngược lại vào db
        String identity = SecurityContextHolder.getContext().getAuthentication().getName();
        User user;
        if (identity.contains("@")) {
            // nếu login bằng mail
            user = userRepository.findByEmail(identity)
                    .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại (email)!"));
        } else {
            // nếu login bằng sdt
            user = userRepository.findBySdt(identity)
                    .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại (sdt)!"));
        }
        dto.setUser(user);
        return stockInRepository.save(dto);
    }

    public StockIn update(Integer id, StockInRequest stockInRequest) throws IOException {

        StockIn dto = stockInRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập"));

        if (stockInRequest.getQuantity() != null) {
            dto.setQuantity(stockInRequest.getQuantity());
        }
        if (stockInRequest.getDate() != null) {
            dto.setDate(stockInRequest.getDate());
        }
        if (stockInRequest.getNote() != null) {
            dto.setNote(stockInRequest.getNote());
        }

        if (stockInRequest.getBatchId() != null) {
            Batch batch = batchRepository.findById(stockInRequest.getBatchId())
                    .orElseThrow(() -> new RuntimeException("Lô hàng không tồn tại"));
            dto.setBatch(batch);
        }

        // tìm user dựa vào giải token lấy mail hoặc sdt rồi truy ngược lại vào db
        String identity = SecurityContextHolder.getContext().getAuthentication().getName();
        User user;
        if (identity.contains("@")) {
            // nếu login bằng mail
            user = userRepository.findByEmail(identity)
                    .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại (email)!"));
        } else {
            // nếu login bằng sdt
            user = userRepository.findBySdt(identity)
                    .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại (sdt)!"));
        }
        dto.setUser(user);

        return stockInRepository.save(dto);
    }

    public void delete(Integer id) {
        stockInRepository.deleteById(id);
    }

    public List<StockIn> getAll() {
        return stockInRepository.findAll();
    }

    public StockInResponse toResponse(StockIn stock) {

        StockInResponse dto = new StockInResponse();
        dto.setStockInID(stock.getStockInID());
        dto.setQuantity(stock.getQuantity());
        dto.setDate(stock.getDate());
        dto.setNote(stock.getNote());

        if (stock.getBatch() != null) {
            dto.setBatchID(stock.getBatch().getBatchID());
        }

        if (stock.getUser() != null) {
            dto.setUserId(stock.getUser().getUserId());
        }

        return dto;
    }
}
