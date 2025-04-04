package com.daehanins.mes.biz.mat.vo;

import com.daehanins.mes.biz.mat.entity.MatStock;
import com.daehanins.mes.biz.mat.entity.MatStockItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MatStockSaveWithitems {

    private MatStock matStock;

    private List<MatStockItem> matStockItems = new ArrayList<>();

    private List<MatStockItem> matStockDeleteItems = new ArrayList<>();

}
