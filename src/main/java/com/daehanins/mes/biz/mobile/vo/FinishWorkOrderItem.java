package com.daehanins.mes.biz.mobile.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinishWorkOrderItem {

    private String workOrderItemId;

    private String confirmMemberCd;

    private BigDecimal prodQty;

}
