package com.daehanins.mes.biz.work.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatSubItem {

    private String itemCd;
    private String itemName;
    private BigDecimal amt;
    private BigDecimal price;

}
