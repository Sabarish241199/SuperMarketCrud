package com.api.supermarketcrud.service;


import com.api.supermarketcrud.jdbcImpl.JdbcUtils;
import com.api.supermarketcrud.model.*;
import com.api.supermarketcrud.repository.RepositoryForBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class MyService {

    @Autowired
    RepositoryForBatch repositoryForBatch;

    @Autowired
    JdbcUtils jdbcUtils;

    @Autowired
    private RestTemplate restTemplate;

    public List<Batch> getBatchData() {
        return repositoryForBatch.findAll();
    }

    public ProductCost getSingleProductCostElement(String Batch_no, String product){
        return jdbcUtils.getSingleProductCostElement(Batch_no, product);
    }

    public List getProductCostData() {
        return jdbcUtils.getProductCost();
    }

    public String updateBatchTable(UpdateRq updateRq) {
        return jdbcUtils.updateBatchCount(updateRq);
    }


    @Async
    public CompletableFuture<Calculation> returnBillCalculation(RequestElemet requestElemet) {

        String url = "http://localhost:8081/api/inventory/retrieve/batch";
        SingleBatchRq singleBatchRq = new SingleBatchRq(requestElemet.getBatch_no(), requestElemet.getProduct());
        ResponseEntity<ProductCost> batch = restTemplate.postForEntity(url, singleBatchRq ,ProductCost.class);
        return CompletableFuture.completedFuture(new Calculation(requestElemet.getProduct(), requestElemet.getCount(),
                requestElemet.getCount() * batch.getBody().getCost()));
    }
}
