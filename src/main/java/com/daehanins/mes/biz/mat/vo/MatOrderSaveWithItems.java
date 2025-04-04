package com.daehanins.mes.biz.mat.vo;

import com.daehanins.mes.biz.mat.entity.MatOrder;
import com.daehanins.mes.biz.mat.entity.MatOrderItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MatOrderSaveWithItems {
    private MatOrder matOrder;

    private List<MatOrderItem> matOrderItems = new ArrayList<>();

    private List<MatOrderItem> matOrderDeleteItems = new ArrayList<>();

}
