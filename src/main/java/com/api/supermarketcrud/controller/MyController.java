package com.api.supermarketcrud.controller;

import com.api.supermarketcrud.model.*;
import com.api.supermarketcrud.service.AsyncService;
import com.api.supermarketcrud.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/inventory")
public class MyController {


    @Autowired
    MyService myServices;

    @Autowired
    AsyncService asyncService;

    @GetMapping("/retrieve/batch")
    public List<Batch> getBatchData() {
        return myServices.getBatchData();
    }

    @PostMapping(value= "/retrieve/batch", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProductCost getBatch(@RequestBody SingleBatchRq singleBatchRq) {
        return myServices.getSingleProductCostElement(singleBatchRq.getBatch_no(),singleBatchRq.getProduct());
    }

    @PutMapping(value = "/count/batchTable/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Object updateBatchTable(@RequestBody UpdateRq updateRq) {
        return myServices.updateBatchTable(updateRq);

    }

    @GetMapping("/retrieve/ProductCost")
    public List getProductCost() {
        return myServices.getProductCostData();
    }
    @PostMapping(value= "/generate/bill", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BillResponse> generateBill(@RequestBody BillRequest request){
        BillResponse billResponse = new BillResponse();
        List<Calculation> calculationList = new ArrayList<>();
        AtomicReference<Double> totalBillAmt= new AtomicReference<>(Double.valueOf(0.0));
        if(!ObjectUtils.isEmpty(request) && !CollectionUtils.isEmpty(request.getElementList())){
            request.getElementList().forEach(x->{
                UpdateRq updateRq = new UpdateRq(x.getBatch_no(), x.getProduct(), x.getCount());
                String url = "http://localhost:8081/api/inventory/count/batchTable/update";
                asyncService.triggerPutApi(url,updateRq);
                CompletableFuture<Calculation> future = myServices.returnBillCalculation(x);
                calculationList.add(future.join());
                totalBillAmt.updateAndGet(v -> v + future.join().getTotalCost());
            });
            billResponse.setTotalBillAmt(totalBillAmt.get());
            billResponse.setListOfCalculation(calculationList);
        }
    return new ResponseEntity<>(billResponse, HttpStatus.OK);
    }
}
