package ru.bmtsu.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import ru.bmtsu.order.controller.dto.OrderItemDTO;
import ru.bmtsu.order.controller.dto.WarrantyRequestDTO;
import ru.bmtsu.order.controller.dto.WarrantyResponseDTO;
import ru.bmtsu.order.exception.ItemNotAvailableException;
import ru.bmtsu.order.exception.NotFoundItemException;
import ru.bmtsu.order.exception.WarehouseServiceNotAvailableException;

import java.net.URI;
import java.util.UUID;

@Service
@Slf4j
public class WarehouseService {
    private final RestTemplate client;
    private final String host;
    private final String path;

    @Autowired
    public WarehouseService(RestTemplate client,
                                 @Value("${service.warehouse.host}") String host,
                                 @Value("${service.warehouse.path}") String path) {
        this.client = client;
        this.host = host;
        this.path = path;
    }

    public OrderItemDTO takeWarehouse(UUID orderUid, String model, String size) {
        URI uri = UriComponentsBuilder.fromUriString(host)
                .path(path)
                .build()
                .encode()
                .toUri();

        ResponseEntity<OrderItemDTO> response = null;
        try {
            response = client.postForEntity(uri, new OrderItemDTO(orderUid, null, model, size), OrderItemDTO.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new NotFoundItemException();
            } else if (e.getStatusCode().equals(HttpStatus.CONFLICT)) {
                throw new ItemNotAvailableException();
            } else {
                throw new WarehouseServiceNotAvailableException();
            }
        } catch (Exception e) {
            throw new WarehouseServiceNotAvailableException();
        }

        return response.getBody();
    }

    public void deleteWarehouse(UUID itemUid) {
        URI uri = UriComponentsBuilder.fromUriString(host)
                .path(path + "/" + itemUid)
                .build()
                .encode()
                .toUri();

        try {
            client.delete(uri);
        } catch (Exception e) {
            throw new WarehouseServiceNotAvailableException();
        }
    }

    public WarrantyResponseDTO useWarranty(UUID itemUid, WarrantyRequestDTO orderItemWarranty) {
        URI uri = UriComponentsBuilder.fromUriString(host)
                .path(path + "/" + itemUid + "/warranty")
                .build()
                .encode()
                .toUri();

        ResponseEntity<WarrantyResponseDTO> response = null;
        try {
            response = client.postForEntity(uri, orderItemWarranty, WarrantyResponseDTO.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new NotFoundItemException();
            } else if (e.getStatusCode().equals(HttpStatus.CONFLICT)) {
                throw new ItemNotAvailableException();
            } else {
                throw new WarehouseServiceNotAvailableException();
            }
        } catch (Exception e) {
            throw new WarehouseServiceNotAvailableException();
        }

        return response.getBody();
    }
}
