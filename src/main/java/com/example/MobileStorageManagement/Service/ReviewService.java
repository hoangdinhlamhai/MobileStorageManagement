package com.example.MobileStorageManagement.Service;

import com.example.MobileStorageManagement.Adapter.CloudinaryAdapter;
import com.example.MobileStorageManagement.DTO.ReviewRequest;
import com.example.MobileStorageManagement.DTO.ReviewResponse;
import com.example.MobileStorageManagement.Entity.Order;
import com.example.MobileStorageManagement.Entity.Review;
import com.example.MobileStorageManagement.Repository.OrderRepository;
import com.example.MobileStorageManagement.Repository.ReviewRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final CloudinaryAdapter cloudinaryAdapter;


    // ===================== CREATE ===================== //
    public Review createReview(ReviewRequest req) {

        Order order = orderRepository.findById(req.getOrderID())
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        String photoUrl = null;
        String videoUrl = null;

        if (req.getPhoto() != null && !req.getPhoto().isEmpty()) {
            photoUrl = cloudinaryAdapter.uploadImage(req.getPhoto(), "reviews/photos");
        }

        if (req.getVideo() != null && !req.getVideo().isEmpty()) {
            videoUrl = cloudinaryAdapter.uploadVideo(req.getVideo(), "reviews/videos");
        }

        Review review = Review.builder()
                .order(order)
                .comment(req.getComment())
                .photoUrl(photoUrl)
                .videoUrl(videoUrl)
                .build();

        return reviewRepository.save(review);
    }


    // ===================== GET ===================== //
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    public List<Review> findByOrderId(Long orderId) {
        return reviewRepository.findByOrder_OrderID(orderId);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }


    // ===================== UPDATE ===================== //
    public Review updateReview(Long id, ReviewRequest req) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review không tồn tại"));

        if (req.getComment() != null) {
            review.setComment(req.getComment());
        }

        // Update photo
        if (req.getPhoto() != null && !req.getPhoto().isEmpty()) {

            if (review.getPhotoUrl() != null) {
                cloudinaryAdapter.deleteFile(extractPublicId(review.getPhotoUrl()), "image");
            }

            review.setPhotoUrl(
                    cloudinaryAdapter.uploadImage(req.getPhoto(), "reviews/photos")
            );
        }

        // Update video
        if (req.getVideo() != null && !req.getVideo().isEmpty()) {

            if (review.getVideoUrl() != null) {
                cloudinaryAdapter.deleteFile(extractPublicId(review.getVideoUrl()), "video");
            }

            review.setVideoUrl(
                    cloudinaryAdapter.uploadVideo(req.getVideo(), "reviews/videos")
            );
        }

        return reviewRepository.save(review);
    }


    // ===================== DELETE ===================== //
    public void deleteReview(Long id) {

        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review không tồn tại"));

        if (review.getPhotoUrl() != null) {
            cloudinaryAdapter.deleteFile(extractPublicId(review.getPhotoUrl()), "image");
        }

        if (review.getVideoUrl() != null) {
            cloudinaryAdapter.deleteFile(extractPublicId(review.getVideoUrl()), "video");
        }

        reviewRepository.delete(review);
    }


    // ===================== Extract public_id ===================== //
    public String extractPublicId(String url) {
        try {
            String noExt = url.substring(0, url.lastIndexOf('.'));
            int idx = noExt.indexOf("/upload/") + "/upload/".length();
            return noExt.substring(idx);
        } catch (Exception e) {
            throw new RuntimeException("Cannot extract public_id: " + url);
        }
    }


    // ===================== MAP DTO ===================== //
    public ReviewResponse toResponse(Review review) {

        if (review == null) return null;

        Long orderId = review.getOrder() != null ? review.getOrder().getOrderID() : null;

        return ReviewResponse.builder()
                .reviewID(review.getReviewID())
                .orderID(orderId)
                .comment(review.getComment())
                .photoUrl(review.getPhotoUrl())
                .videoUrl(review.getVideoUrl())
                .build();
    }
}
