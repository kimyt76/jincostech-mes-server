package com.daehanins.mes.biz.mat.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatchStockRealItem {

    private String matStockItemId;
    private String matStockRealItemId;
    private String itemCd;
    private String lotNo;
    private String testNo;
    private BigDecimal checkQty;
    private BigDecimal realQty;
    private String memo;
}
