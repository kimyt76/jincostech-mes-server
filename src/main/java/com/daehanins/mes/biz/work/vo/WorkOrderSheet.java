package com.daehanins.mes.biz.work.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WorkOrderSheet {

    private String orderNo;
    private String customerName;
    private String memberName;
    private String deliveryDate;
    private String itemName;
    private BigDecimal sumOrderQty;
    private BigDecimal sumProdQty;

}