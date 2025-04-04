package com.daehanins.mes.biz.mat.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatSupplyRegisterReqVo {

    private String itemCd;

    private String itemName;

    private String startDate;

    private String endDate;

    private String itemTypeCd;

    private String itemGrp1;

}
