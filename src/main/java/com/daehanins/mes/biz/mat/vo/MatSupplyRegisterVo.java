package com.daehanins.mes.biz.mat.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatSupplyRegisterVo {

    private String itemCd;

    private String itemName;

    private String itemTypeName;

    private BigDecimal inPrice;

    private String itemGrp1;

    private String itemGrp1Name;

    private String customerCd;

    private String customerName;

    private BigDecimal aQty;

    private BigDecimal eQty;

    private BigDecimal pQty;

    private BigDecimal rQty;

    private BigDecimal tQty;

    private BigDecimal qQty;

    private BigDecimal sQty;

    private BigDecimal vQty;

    private BigDecimal wQty;

    private BigDecimal xQty;

    private BigDecimal uQty;

    private BigDecimal aAmount;

    private BigDecimal eAmount;

    private BigDecimal startQty;

    private BigDecimal startAmount;

    private BigDecimal endQty;

    private BigDecimal endAmount;

}
