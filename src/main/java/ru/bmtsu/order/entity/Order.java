package ru.bmtsu.order.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.bmtsu.order.utils.PaymentStatus;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_uid", nullable = false)
    private UUID itemUid;

    @Column(name = "order_date", nullable = false)
    private Timestamp orderDate;

    @Column(name = "order_uid", nullable = false)
    private UUID orderUid;

    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "user_uid", nullable = false)
    private UUID userUid;
}
