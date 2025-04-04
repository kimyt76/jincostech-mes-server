package com.daehanins.mes.biz.mat.vo;

import com.daehanins.mes.biz.mat.entity.MatPointStock;
import com.daehanins.mes.biz.mat.entity.MatPointStockItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MatPointStockSaveWithItems {

    private MatPointStock matPointStock;

    private List<MatPointStockItem> matPointStockItems = new ArrayList<>();

    private List<MatPointStockItem> matPointStockDeleteItems = new ArrayList<>();
}
