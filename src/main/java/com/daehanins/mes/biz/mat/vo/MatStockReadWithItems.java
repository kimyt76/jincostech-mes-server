package com.daehanins.mes.biz.mat.vo;

import com.daehanins.mes.biz.mat.entity.MatStock;
import com.daehanins.mes.biz.mat.entity.MatStockItemView;
import com.daehanins.mes.biz.mat.entity.MatStockView;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MatStockReadWithItems {

    private MatStockView matStock;

    private List<MatStockItemView> matStockItems = new ArrayList<>();
}
