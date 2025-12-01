package com.example.MobileStorageManagement.Service;

import com.example.MobileStorageManagement.DTO.BatchRequest;
import com.example.MobileStorageManagement.DTO.BatchResponse;
import com.example.MobileStorageManagement.Entity.Batch;
import com.example.MobileStorageManagement.Entity.Product;
import com.example.MobileStorageManagement.Repository.BatchRepository;
import com.example.MobileStorageManagement.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BatchService {
    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private ProductRepository productRepository;

    public Optional<Batch> getById(Long id) {
        return batchRepository.findById(id);
    }

    public List<Batch> getAll() {
        return batchRepository.findAll();
    }

    public BatchResponse create(BatchRequest batchRequest) throws IOException {
        Batch sp = new Batch();

        sp.setQuantity(batchRequest.getQuantity());
        sp.setPriceIn(batchRequest.getPriceIn());
        sp.setExpiry(batchRequest.getExpiry());
        sp.setProductionDate(batchRequest.getProductionDate());

        if(batchRequest.getProductID() != null){
            Product product = productRepository.findById(batchRequest.getProductID())
                    .orElseThrow(() -> new RuntimeException("sản phẩm không tồn tại"));
            sp.setProduct(product);
        }


        return toResponse(batchRepository.save(sp));
    }

    public BatchResponse update(Long id, BatchRequest batchRequest) {
        Batch sp = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lô hàng"));

        if (batchRequest.getQuantity() != null) {
            sp.setQuantity(batchRequest.getQuantity());
        }
        if (batchRequest.getPriceIn() != null) {
            sp.setPriceIn(batchRequest.getPriceIn());
        }
        if (batchRequest.getExpiry() != null) {
            sp.setExpiry(batchRequest.getExpiry());
        }
        if (batchRequest.getProductionDate() != null) {
            sp.setProductionDate(batchRequest.getProductionDate());
        }

        if (batchRequest.getProductID() != null) {
            Product product = productRepository.findById(batchRequest.getProductID())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
            sp.setProduct(product);
        }

        return toResponse(batchRepository.save(sp));
    }

    public void delete(Long id) {
        batchRepository.deleteById(id);
    }

    // Chuyển từ Entity -> Response DTO
    private BatchResponse toResponse(Batch batch) {
        BatchResponse dto = new BatchResponse();
        dto.setBatchID(batch.getBatchID());
        dto.setProductionDate(batch.getProductionDate());
        dto.setExpiry(batch.getExpiry());
        dto.setPriceIn(batch.getPriceIn());
        dto.setQuantity(batch.getQuantity());

        if (batch.getProduct() != null) {
            dto.setProductID(batch.getProduct().getProductId());  // hoặc tên
        }

        return dto;
    }
}
