package com.daehanins.mes.biz.mat.vo;

import com.daehanins.mes.biz.mat.entity.MatStockReal;
import com.daehanins.mes.biz.mat.entity.MatStockRealItemView;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MatStockRealReadWithItems {

    private MatStockReal matStockReal;

    private List<MatStockRealItemView> matStockRealItems = new ArrayList<>();
}
