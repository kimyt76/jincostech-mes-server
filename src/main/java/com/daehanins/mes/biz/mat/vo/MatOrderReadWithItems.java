package com.daehanins.mes.biz.mat.vo;

import com.daehanins.mes.biz.mat.entity.MatOrder;
import com.daehanins.mes.biz.mat.entity.MatOrderItemView;
import com.daehanins.mes.biz.mat.entity.MatOrderView;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MatOrderReadWithItems {
    private MatOrderView matOrder;

    private List<MatOrderItemView> matOrderItems = new ArrayList<>();
}
