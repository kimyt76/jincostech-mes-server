package com.daehanins.mes.biz.work.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConsumptionItem {

    private String itemCd;
    private String itemType;
    private String itemName;
    private String spec;
    private BigDecimal stockQty;
    private BigDecimal reqQty;
    private BigDecimal diff;
    private String customerName;
    private String inOut;
    private BigDecimal inPrice;
    private BigDecimal matOrderQty;

}
