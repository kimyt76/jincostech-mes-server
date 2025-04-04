package com.daehanins.mes.biz.mat.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatStockStorageRead {

    private String itemTypeCd;
    private String itemCd;
    private String itemName;

    private String lotNo;
    private String testNo;

    private BigDecimal stockQty1 = BigDecimal.ZERO;
    private BigDecimal stockQty2 = BigDecimal.ZERO;
    private BigDecimal stockQty3 = BigDecimal.ZERO;
    private BigDecimal stockQty4 = BigDecimal.ZERO;
    private BigDecimal stockQty5 = BigDecimal.ZERO;

    public BigDecimal getTotalQty() {
        BigDecimal totalQty = BigDecimal.ZERO;
        return totalQty.add(stockQty1)
                .add(stockQty2)
                .add(stockQty3)
                .add(stockQty4)
                .add(stockQty5);
    }

}
