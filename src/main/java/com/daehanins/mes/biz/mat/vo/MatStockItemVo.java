package com.daehanins.mes.biz.mat.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatStockItemVo {

    private String storageCd;

    private String itemCd;

    private String itemName;

    private String testNo;

    private String lotNo;

    private BigDecimal stockQty;

}
