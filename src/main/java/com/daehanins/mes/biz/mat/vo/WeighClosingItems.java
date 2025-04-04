package com.daehanins.mes.biz.mat.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WeighClosingItems {

    private String itemCd;

    private String itemName;

    private BigDecimal startQty;

    private BigDecimal allStartQty;

    private BigDecimal endQty;

    private BigDecimal allEndQty;

    private BigDecimal inQty;

    private BigDecimal useQty;

    private BigDecimal testQty;

    private BigDecimal studyQty;

    private BigDecimal disuseQty;

    private BigDecimal adjQty;

}
