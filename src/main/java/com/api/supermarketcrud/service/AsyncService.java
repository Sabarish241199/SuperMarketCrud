package com.api.supermarketcrud.service;

import com.api.supermarketcrud.model.UpdateRq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AsyncService {

    @Autowired
    RestTemplate restTemplate;

    @Async
    public void triggerPutApi(String url, UpdateRq requestBody) {
        // Perform the PUT call asynchronously
        restTemplate.put(url, requestBody);
    }
}
