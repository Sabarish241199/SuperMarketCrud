package com.api.supermarketcrud.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestElemet {
    private String Batch_no;
    private String Product;
    private Double Count;
}
