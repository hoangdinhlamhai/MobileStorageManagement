package com.example.MobileStorageManagement.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private Long reviewID;
    private Long orderID;
    private String comment;

    private String photoUrl;
    private String videoUrl;
}
