package com.daehanins.mes.biz.mat.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MatStockResultItems {

    private List<StockStorageVo> storageList;
    private StockStorageVo storageMap;
    private List<Map<String, Object>> resultMap;
}
