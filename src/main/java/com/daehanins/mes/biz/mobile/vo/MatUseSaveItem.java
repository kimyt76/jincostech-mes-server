package com.daehanins.mes.biz.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatUseSaveItem {

    private String matUseId;

    private String workOrderItemId;

    private String equipmentCd;

    private String memberCd;

    private BigDecimal weighQty;
}
