package com.example.MobileStorageManagement.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.MobileStorageManagement.DTO.ProductDTO;
import com.example.MobileStorageManagement.DTO.ProductImageDTO;
import com.example.MobileStorageManagement.DTO.SpecificationDTO;
import com.example.MobileStorageManagement.Entity.Product;
import com.example.MobileStorageManagement.Entity.ProductImage;
import com.example.MobileStorageManagement.Entity.Specification;
import com.example.MobileStorageManagement.Repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDTO toDTO(Product product) {
        return ProductDTO.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .description(product.getDescription())
                .brandId(product.getBrand() != null ? product.getBrand().getBrandId() : null)
                .categoryId(product.getCategory() != null ? product.getCategory().getCategoryId() : null)
                .specification(product.getSpecification() != null ? SpecificationDTO.builder()
                        .specId(product.getSpecification().getSpecId())
                        .screen(product.getSpecification().getScreen())
                        .os(product.getSpecification().getOs())
                        .cpu(product.getSpecification().getCpu())
                        .ram(product.getSpecification().getRam())
                        .battery(product.getSpecification().getBattery())
                        .camera(product.getSpecification().getCamera())
                        .storage(product.getSpecification().getStorage())
                        .build() : null)
                .productImages(product.getProductImages().stream()
                        .map(img -> ProductImageDTO.builder()
                                .id(img.getId())
                                .url(img.getUrl())
                                .img_index(img.getImg_index())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setProductId(dto.getProductId());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setDescription(dto.getDescription());

        if (dto.getSpecification() != null) {
            Specification s = new Specification();
            s.setSpecId(dto.getSpecification().getSpecId());
            s.setScreen(dto.getSpecification().getScreen());
            s.setOs(dto.getSpecification().getOs());
            s.setCpu(dto.getSpecification().getCpu());
            s.setRam(dto.getSpecification().getRam());
            s.setBattery(dto.getSpecification().getBattery());
            s.setStorage(dto.getSpecification().getStorage());
            s.setCamera(dto.getSpecification().getCamera());
            product.setSpecification(s);
        }

        if (dto.getProductImages() != null) {
            List<ProductImage> images = dto.getProductImages().stream()
                    .map(i -> {
                        ProductImage img = new ProductImage();
                        img.setId(i.getId());
                        img.setUrl(i.getUrl());
                        img.setImg_index(i.getImg_index());
                        img.setProduct(product);
                        return img;
                    })
                    .collect(Collectors.toList());

            product.setProductImages(images);
        }

        return product;
    }

    public List<ProductDTO> getAll() {
        return productRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getById(Integer id) {
        return productRepository.findById(id)
                .map(this::toDTO)
                .orElse(null);
    }

    public ProductDTO create(ProductDTO dto) {
        Product saved = productRepository.save(toEntity(dto));
        return toDTO(saved);
    }

    public ProductDTO update(Integer id, ProductDTO dto) {
        Product exist = productRepository.findById(id).orElse(null);
        if (exist == null)
            return null;

        exist.setName(dto.getName());
        exist.setPrice(dto.getPrice());
        exist.setStockQuantity(dto.getStockQuantity());
        exist.setDescription(dto.getDescription());

        // Update specification
        if (dto.getSpecification() != null) {
            exist.getSpecification().setScreen(dto.getSpecification().getScreen());
            exist.getSpecification().setOs(dto.getSpecification().getOs());
            exist.getSpecification().setCpu(dto.getSpecification().getCpu());
            exist.getSpecification().setRam(dto.getSpecification().getRam());
            exist.getSpecification().setBattery(dto.getSpecification().getBattery());
            exist.getSpecification().setStorage(dto.getSpecification().getStorage());
            exist.getSpecification().setCamera(dto.getSpecification().getCamera());
        }

        // Update images properly
        exist.getProductImages().clear();

        if (dto.getProductImages() != null) {
            for (ProductImageDTO i : dto.getProductImages()) {
                ProductImage img = new ProductImage();
                img.setUrl(i.getUrl());
                img.setImg_index(i.getImg_index());
                img.setProduct(exist); // gắn lại quan hệ
                exist.getProductImages().add(img);
            }
        }

        return toDTO(productRepository.save(exist));
    }

    public void delete(Integer id) {
        productRepository.deleteById(id);
    }
}
