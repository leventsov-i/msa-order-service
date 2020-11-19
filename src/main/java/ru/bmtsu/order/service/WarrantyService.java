package ru.bmtsu.order.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.bmtsu.order.exception.WarrantyServiceNotAvailableException;


import java.net.URI;
import java.util.UUID;

@Service
@Slf4j
public class WarrantyService {
    private final RestTemplate client;
    private final String host;
    private final String path;

    @Autowired
    public WarrantyService(RestTemplate client,
                           @Value("${service.warranty.host}") String host,
                           @Value("${service.warranty.path}") String path) {
        this.client = client;
        this.host = host;
        this.path = path;
    }

    public void startWarranty(UUID itemUid) {
        URI uri = UriComponentsBuilder.fromUriString(host)
                .path(path + "/" + itemUid.toString())
                .build()
                .encode()
                .toUri();
        try {
            client.postForLocation(uri, HttpEntity.EMPTY);
        } catch (Exception e) {
            throw new WarrantyServiceNotAvailableException();
        }
    }

    public void deleteWarranty(UUID itemUid) {
        URI uri = UriComponentsBuilder.fromUriString(host)
                .path(path + "/" + itemUid.toString())
                .build()
                .encode()
                .toUri();

        try {
            client.delete(uri);
        } catch (Exception e) {
            throw new WarrantyServiceNotAvailableException();
        }
    }
}
