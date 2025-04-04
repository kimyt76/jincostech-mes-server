package com.daehanins.mes.biz.mat.vo;

import com.daehanins.mes.biz.mat.entity.MatOrderItemView;
import com.daehanins.mes.biz.mat.entity.MatOrderView;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MatOrderReadWithItemsTranSum {

    private MatOrderView matOrder;

    private List<MatOrderTranItemSum> matOrderItems = new ArrayList<>();
}
