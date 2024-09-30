package com.api.supermarketcrud.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Calculation {
    private String Product;
    private Double qty;
    private Double totalCost;
}
