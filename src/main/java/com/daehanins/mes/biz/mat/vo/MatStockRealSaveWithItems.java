package com.daehanins.mes.biz.mat.vo;

import com.daehanins.mes.biz.mat.entity.MatStockReal;
import com.daehanins.mes.biz.mat.entity.MatStockRealItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MatStockRealSaveWithItems {

    private MatStockReal matStockReal;

    private List<MatStockRealItem> matStockRealItems = new ArrayList<>();

    private List<MatStockRealItem> matStockRealDeleteItems = new ArrayList<>();
}
