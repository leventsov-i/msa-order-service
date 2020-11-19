package ru.bmtsu.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.bmtsu.order.entity.Order;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OrderResponseDTO {
    private UUID orderUid;
    private String orderDate;
    private UUID itemUid;
    private String status;

    public static OrderResponseDTO from(Order order) {
        return OrderResponseDTO.builder()
                .itemUid(order.getItemUid())
                .orderDate(order.getOrderDate().toString())
                .orderUid(order.getOrderUid())
                .status(order.getStatus().toString())
                .build();
    }
}
