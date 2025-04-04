package com.daehanins.mes.biz.mat.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MatPointStockLast {

    private String matPointStockId;

    private LocalDate stockDate;
}
