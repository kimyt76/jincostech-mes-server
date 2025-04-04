package com.daehanins.mes.biz.work.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsBomItemByCosts {

    private String itemCd;

    private String itemName;

    private String prodItemCd;

    private String prodItemName;

    private String bomVer;

    private BigDecimal sumRatio;

    private BigDecimal prodQty;

    private BigDecimal qty;

}
