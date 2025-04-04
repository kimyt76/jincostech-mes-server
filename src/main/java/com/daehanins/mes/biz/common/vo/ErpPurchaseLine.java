package com.daehanins.mes.biz.common.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ErpPurchaseLine {

    @JsonProperty("Line")
    private String Line;

    @JsonProperty("BulkDatas")
    private ErpPurchase BulkDatas;

    public ErpPurchaseLine(String line) {
        this.Line = line;
    }

    public ErpPurchaseLine(String line, ErpPurchase bulkDatas) {
        this.Line = line;
        this.BulkDatas = bulkDatas;
    }
}
