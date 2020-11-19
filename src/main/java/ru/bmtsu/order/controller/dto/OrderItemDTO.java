package ru.bmtsu.order.controller.dto;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class OrderItemDTO {
    private UUID orderUid;
    @Setter
    private UUID orderItemUid;
    private String model;
    private String size;
}
