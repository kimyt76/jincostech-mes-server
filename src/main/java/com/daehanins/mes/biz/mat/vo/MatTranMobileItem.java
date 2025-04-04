package com.daehanins.mes.biz.mat.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatTranMobileItem {

    private String tranNo;

    private String itemCd;

    private String itemName;

    private BigDecimal qty;

    private String tranName;

}
