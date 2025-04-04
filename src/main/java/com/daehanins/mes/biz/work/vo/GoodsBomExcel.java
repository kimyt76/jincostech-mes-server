package com.daehanins.mes.biz.work.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsBomExcel {

    private String prodItemCd;
    private String bomVer;
    private String defaultYn;
    private String prodState;
    private String itemCd;
    private BigDecimal contentRatio;
    private BigDecimal qty;

}
