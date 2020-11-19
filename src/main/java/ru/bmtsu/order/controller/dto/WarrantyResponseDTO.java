package ru.bmtsu.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class WarrantyResponseDTO {
    private String warrantyDate;
    private String decision;
}
