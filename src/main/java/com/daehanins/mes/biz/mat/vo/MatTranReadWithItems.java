package com.daehanins.mes.biz.mat.vo;

import com.daehanins.mes.biz.mat.entity.MatTran;
import com.daehanins.mes.biz.mat.entity.MatTranItemView;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MatTranReadWithItems {
    private MatTran matTran;

    private List<MatTranItemView> matTranItems = new ArrayList<>();

}
