package com.daehanins.mes.biz.mat.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatPointStockRead {

    private String storageCd;
    private String storageName;

    private String itemTypeCd;
    private String itemCd;
    private String itemName;

    private String lotNo;
    private String testNo;

    private BigDecimal stockQty;
    private BigDecimal orderQty;
    private BigDecimal safeStockQty;

}
