package com.daehanins.mes.biz.common.vo;

import com.daehanins.mes.common.utils.BarcodeUtil;
import lombok.Data;

import java.awt.image.BufferedImage;

@Data
public class LabelPrint {

    private String labelTitle;
    private String itemCd;
    private String itemName;
    private String itemCondition;
    private String testNo;
    private String lotNo;
    private String expiryDate;
    private String strQty;
    private String procStateName;
    private BufferedImage barcodeImage;

    public LabelPrint(
            String itemCd,
            String itemName,
            String itemCondition,
            String testNo,
            String lotNo,
            String expiryDate,
            String strQty,
            String procStateName) {
        try {
            this.barcodeImage = BarcodeUtil.generateQRCodeImage(testNo);
        } catch(Exception ex) {
            this.barcodeImage = null;
        }
        this.itemCd = itemCd;
        this.itemName = itemName;
        this.itemCondition = itemCondition;
        this.testNo = testNo;
        this.lotNo = lotNo;
        this.expiryDate = expiryDate;
        this.strQty = strQty;
        this.procStateName = procStateName;
    }
}

