package com.daehanins.mes.biz.common.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ErpPurchaseListVo {
    @JsonProperty("PurchasesList")
    private List<ErpPurchaseLine> PurchasesList = new ArrayList<>();

}
