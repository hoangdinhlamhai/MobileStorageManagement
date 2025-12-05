package com.example.MobileStorageManagement.DTO;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewRequest {

    private Long orderID;
    private String comment;

    private MultipartFile photo;  // file ảnh từ máy local
    private MultipartFile video;  // file video từ máy local
}
