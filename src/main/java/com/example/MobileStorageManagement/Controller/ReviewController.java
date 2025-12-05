package com.example.MobileStorageManagement.Controller;

import com.example.MobileStorageManagement.DTO.ReviewRequest;
import com.example.MobileStorageManagement.DTO.ReviewResponse;
import com.example.MobileStorageManagement.Entity.Review;
import com.example.MobileStorageManagement.Service.ReviewService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ReviewResponse> createReview(
            @RequestParam Long orderID,
            @RequestParam(required = false) String comment,
            @RequestPart(required = false) MultipartFile photo,
            @RequestPart(required = false) MultipartFile video
    ) {

        ReviewRequest req = ReviewRequest.builder()
                .orderID(orderID)
                .comment(comment)
                .photo(photo)
                .video(video)
                .build();

        Review created = reviewService.createReview(req);

        return ResponseEntity.ok(reviewService.toResponse(created));
    }


    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long id,
            @RequestParam(required = false) String comment,
            @RequestPart(required = false) MultipartFile photo,
            @RequestPart(required = false) MultipartFile video
    ) {

        ReviewRequest req = ReviewRequest.builder()
                .comment(comment)
                .photo(photo)
                .video(video)
                .build();

        Review updated = reviewService.updateReview(id, req);

        return ResponseEntity.ok(reviewService.toResponse(updated));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReview(@PathVariable Long id) {
        return reviewService.findById(id)
                .map(reviewService::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<ReviewResponse>> getByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(
                reviewService.findByOrderId(orderId)
                        .stream()
                        .map(reviewService::toResponse)
                        .toList()
        );
    }


    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAll() {
        return ResponseEntity.ok(
                reviewService.findAll()
                        .stream()
                        .map(reviewService::toResponse)
                        .toList()
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok("Review deleted");
    }
}
