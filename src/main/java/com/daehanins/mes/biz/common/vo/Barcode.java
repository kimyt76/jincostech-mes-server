package com.daehanins.mes.biz.common.vo;

import com.daehanins.mes.common.utils.BarcodeUtil;
import lombok.Data;

import java.awt.image.BufferedImage;

@Data
public class Barcode {
    private BufferedImage barcodeImage;
    private String itemName;
    private String testNo;
    private String expiryDate;
    private String procStateName;

    public Barcode(String barcode, String itemName, String testNo, String expiryDate, String procStateName) {
            try {
                this.barcodeImage = BarcodeUtil.generateQRCodeImage(barcode);
            } catch(Exception ex) {
                this.barcodeImage = null;
            }
        this.itemName = itemName;
        this.testNo = testNo;
        this.expiryDate = expiryDate;
        this.procStateName = procStateName;
    }
}
