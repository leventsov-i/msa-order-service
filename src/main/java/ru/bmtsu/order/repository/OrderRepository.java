package ru.bmtsu.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bmtsu.order.entity.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderUidAndUserUid(UUID orderUid, UUID userUid);
    List<Order> findByUserUid(UUID userUid);
    Optional<Order> findByOrderUid(UUID orderUid);
}
