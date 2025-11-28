package com.utetea.backend.dto;
//VU VAN THONG 23162098
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class DrinkSizeDto {
    private Long id;
    private String sizeName;
    private BigDecimal extraPrice;
}
