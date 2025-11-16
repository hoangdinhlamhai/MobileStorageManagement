package com.example.MobileStorageManagement.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.MobileStorageManagement.DTO.SpecificationDTO;
import com.example.MobileStorageManagement.Service.SpecificationService;

@RestController
@RequestMapping("/api/specifications")
@CrossOrigin(origins = "*")
public class SpecificationController {
    private final SpecificationService specificationService;

    public SpecificationController(SpecificationService specificationService) {
        this.specificationService = specificationService;
    }

    @GetMapping
    public List<SpecificationDTO> getAllSpecifications() {
        return this.specificationService.getAllSpecifications();
    }

    @GetMapping("/{id}")
    public SpecificationDTO getSpecificationById(@PathVariable Integer id) {
        return this.specificationService.getSpecificationById(id);
    }

    @PostMapping
    public SpecificationDTO createSpecification(@RequestBody SpecificationDTO specDTO) {
        return this.specificationService.createSpecification(specDTO);
    }

    @PutMapping("/{id}")
    public SpecificationDTO updateSpecification(
            @PathVariable Integer id,
            @RequestBody SpecificationDTO specDTO) {
        return this.specificationService.updateSpecification(id, specDTO);
    }

    @DeleteMapping("/{id}")
    public Void deleteSpecification(@PathVariable Integer id) {
        this.specificationService.deleteSpecification(id);
        return null;
    }
}
