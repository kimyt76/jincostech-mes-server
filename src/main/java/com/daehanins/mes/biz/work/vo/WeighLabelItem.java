package com.daehanins.mes.biz.work.vo;

import lombok.Data;

@Data
public class WeighLabelItem {

    private String itemCd;
    private String itemName;
    private String lotNo;
    private String testNo;
    private String expiryDate;
    private Double qty;
    private String okYn;
    private int    printCnt;

}
