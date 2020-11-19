package ru.bmtsu.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.bmtsu.order.entity.Order;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CreateOrderResponseDTO {
    private UUID orderUid;

    public static CreateOrderResponseDTO from(Order order) {
        return CreateOrderResponseDTO.builder()
                .orderUid(order.getOrderUid())
                .build();
    }
}
