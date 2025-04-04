package com.daehanins.mes.biz.mat.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatOrderTranItemSum {

    private String itemCd;
    private String itemName;
    private BigDecimal orderQty;
    private BigDecimal tranQty;
}
