package com.daehanins.mes.biz.work.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReqItemQty {

    private String itemCd;

    private String itemName;

    private BigDecimal orderQty;

    private String memo;
}
