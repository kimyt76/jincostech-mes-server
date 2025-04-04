package com.daehanins.mes.biz.mat.vo;

import com.daehanins.mes.biz.mat.entity.MatPointStock;
import com.daehanins.mes.biz.mat.entity.MatPointStockItemView;
import com.daehanins.mes.biz.mat.entity.MatStockReal;
import com.daehanins.mes.biz.mat.entity.MatStockRealItemView;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MatPointStockReadWithItems {

    private MatPointStock matPointStock;

    private List<MatPointStockItemView> matPointStockItems = new ArrayList<>();
}
