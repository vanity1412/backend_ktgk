package com.utetea.backend.dto;
//VU VAN THONG 23162098
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DrinkCategoryDto {
    private Long id;
    private String name;
    private String description;
    private Integer displayOrder;
    private Boolean isActive;
}
