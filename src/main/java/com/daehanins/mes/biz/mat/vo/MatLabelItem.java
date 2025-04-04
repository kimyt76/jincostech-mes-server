package com.daehanins.mes.biz.mat.vo;
import lombok.Data;

@Data
public class MatLabelItem {

    private String itemCd;
    private String itemName;
    private String lotNo;
    private String testNo;
    private String expiryDate;
    private Double qty;
    private String okYn;
    private int    printCnt;

}
