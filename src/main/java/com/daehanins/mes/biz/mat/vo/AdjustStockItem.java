package com.daehanins.mes.biz.mat.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdjustStockItem {
    private String matStockItemId;
    private String itemCd;
    private String itemName;
    private String lotNo;
    private String testNo;
    private BigDecimal adjustQty;
    private String memo;
}
