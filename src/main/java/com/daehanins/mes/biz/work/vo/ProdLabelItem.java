package com.daehanins.mes.biz.work.vo;

import lombok.Data;

@Data
public class ProdLabelItem {

    private String itemCd;
    private String itemName;
    private String lotNo;
    private String prodNo;

    private String itemTestNo;
    private String expiryDate;
    private Double prodQty;
    private String okYn;
    private int    printCnt;

}
