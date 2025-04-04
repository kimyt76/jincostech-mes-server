package com.daehanins.mes.biz.common.vo;

import com.daehanins.mes.common.utils.BarcodeUtil;
import lombok.Data;

import java.awt.image.BufferedImage;

@Data
public class WeighLabelPrint {

    private String matUseId;
    private String prodItemCd;
    private String prodItemName;
    private String itemAlias;
    private String itemName;
    private String prodQty;
    private String bagWeight;
    private String weighQty;
    private String totalQty;
    private String lotNo;
    private String prodNo;
    private BufferedImage barcodeImage;

    public WeighLabelPrint(
            String matUseId,
            String prodItemCd,
            String prodItemName,
            String itemAlias,
            String itemName,
            String prodQty,
            String bagWeight,
            String weighQty,
            String totalQty,
            String lotNo,
            String prodNo) {
        try {
            this.barcodeImage = BarcodeUtil.generateQRCodeImage(matUseId);
        } catch(Exception ex) {
            this.barcodeImage = null;
        }
        this.matUseId		= matUseId;
        this.prodItemCd		= prodItemCd;
        this.prodItemName	= prodItemName;
        this.itemAlias		= itemAlias;
        this.itemName		= itemName;
        this.prodQty		= prodQty;
        this.bagWeight		= bagWeight;
        this.weighQty		= weighQty;
        this.totalQty		= totalQty;
        this.lotNo			= lotNo;
        this.prodNo			= prodNo;
    }
}
