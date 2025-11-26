package com.example.MobileStorageManagement.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDTO {
    private long id;
    private String url;
    private long img_index;
}
