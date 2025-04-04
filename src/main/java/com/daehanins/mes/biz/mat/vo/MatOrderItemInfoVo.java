package com.daehanins.mes.biz.mat.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class MatOrderItemInfoVo {

    private String itemCd;
    private String itemName;
    private String areaName;
    private String customerName;
    private String orderDate;
    private String deliveryDate;
    private String spec;
    private String memberCd;

    private BigDecimal orderQty;
    private BigDecimal price;
    private BigDecimal vat;
    private BigDecimal supplyAmt;


    private String areaCcd;
    private String customerCd;

    private MatOrderItemInfoVo matOrderItemInfo;

    private List<MatOrderItemInfoVo> matOrderItemList = new ArrayList<>();

}
