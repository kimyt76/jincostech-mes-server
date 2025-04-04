package com.daehanins.mes.biz.mat.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 장부상 재고
 */
@Data
public class LedgerStockItem {

    private String storageCd;
    private String itemCd;
    private String lotNo;
    private String testNo;

    private BigDecimal baseQty;
    private BigDecimal inQty;
    private BigDecimal outQty;
    private BigDecimal useQty;
    private BigDecimal adjQty;
    private BigDecimal resultQTy;

}

