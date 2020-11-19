package ru.bmtsu.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bmtsu.order.controller.dto.*;
import ru.bmtsu.order.entity.Order;
import ru.bmtsu.order.exception.NotFoundOrderException;
import ru.bmtsu.order.repository.OrderRepository;
import ru.bmtsu.order.utils.PaymentStatus;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final WarehouseService warehouseService;
    private final WarrantyService warrantyService;

    @Autowired
    public OrderService(OrderRepository orderRepository, WarehouseService warehouseService, WarrantyService warrantyService) {
        this.orderRepository = orderRepository;
        this.warehouseService = warehouseService;
        this.warrantyService = warrantyService;
    }

    public OrderResponseDTO getOrder(UUID userUid, UUID orderUid) {
        Order order = orderRepository.findByOrderUidAndUserUid(orderUid, userUid).orElseThrow(NotFoundOrderException::new);

        return OrderResponseDTO.from(order);
    }

    public List<OrderResponseDTO> getOrders(UUID userUid) {
        return orderRepository.findByUserUid(userUid).stream()
                .map(OrderResponseDTO::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public CreateOrderResponseDTO createOrder(UUID userUid, CreateOrderRequestDTO additionalOrderInfo) {
        UUID orderUid = UUID.randomUUID();

        OrderItemDTO orderItem = warehouseService.takeWarehouse(orderUid, additionalOrderInfo.getModel(), additionalOrderInfo.getSize());
        warrantyService.startWarranty(orderItem.getOrderItemUid());

        Order order = Order.builder()
                .itemUid(orderItem.getOrderItemUid())
                .orderDate(new Timestamp(System.currentTimeMillis()))
                .orderUid(orderUid)
                .status(PaymentStatus.PAID)
                .userUid(userUid)
                .build();

        orderRepository.save(order);
        return CreateOrderResponseDTO.from(order);
    }

    public void deleteOrder(UUID orderUid) {
        Order order = orderRepository
                .findByOrderUid(orderUid)
                .orElseThrow(NotFoundOrderException::new);
        warehouseService.deleteWarehouse(order.getItemUid());
        warrantyService.deleteWarranty(order.getItemUid());
        orderRepository.delete(order);
    }

    public WarrantyResponseDTO warranty(UUID orderUid, WarrantyRequestDTO warranty) {
        Order order = orderRepository
                .findByOrderUid(orderUid)
                .orElseThrow(NotFoundOrderException::new);

        return warehouseService.useWarranty(order.getItemUid(), warranty);
    }
}
