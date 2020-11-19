package ru.bmtsu.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bmtsu.order.controller.dto.*;
import ru.bmtsu.order.exception.*;
import ru.bmtsu.order.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{userUid}/{orderUid}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable UUID userUid, @PathVariable UUID orderUid) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.getOrder(userUid, orderUid));
    }

    @GetMapping("/{userUid}")
    public ResponseEntity<List<OrderResponseDTO>> getOrders(@PathVariable UUID userUid) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.getOrders(userUid));
    }

    @PostMapping("/{userUid}")
    public ResponseEntity<CreateOrderResponseDTO> createOrder(@PathVariable UUID userUid, @RequestBody CreateOrderRequestDTO body) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.createOrder(userUid, body));
    }

    @DeleteMapping("/{orderUid}")
    public ResponseEntity deleteOrder(@PathVariable UUID orderUid) {
        orderService.deleteOrder(orderUid);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PostMapping("/{orderUid}/warranty")
    public ResponseEntity<WarrantyResponseDTO> warranty(@PathVariable UUID orderUid, @RequestBody WarrantyRequestDTO warranty) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.warranty(orderUid, warranty));
    }

    @ExceptionHandler({NotFoundOrderException.class, NotFoundItemException.class})
    public ResponseEntity<ErrorDTO> handlerNotFoundException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDTO(e.getMessage()));
    }

    @ExceptionHandler({ItemNotAvailableException.class})
    public ResponseEntity<ErrorDTO> handlerItemNotAvailable(Exception e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorDTO(e.getMessage()));
    }

    @ExceptionHandler({WarehouseServiceNotAvailableException.class, WarrantyServiceNotAvailableException.class})
    public ResponseEntity<ErrorDTO> handlerServiceNotAvailableException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorDTO(e.getMessage()));
    }
}
