package com.daehanins.mes.biz.mat.vo;

import lombok.Data;
import java.util.List;

@Data
public class MatStockItemsWithStorages {

    private List<StockStorageVo> storageList;

    private List<MatStockItemVo> testNoList;

}
