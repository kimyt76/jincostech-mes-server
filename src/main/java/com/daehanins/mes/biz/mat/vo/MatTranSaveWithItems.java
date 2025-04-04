package com.daehanins.mes.biz.mat.vo;

import com.daehanins.mes.biz.mat.entity.MatTran;
import com.daehanins.mes.biz.mat.entity.MatTranItem;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MatTranSaveWithItems {

    private MatTran matTran;

    private List<MatTranItem> matTranItems = new ArrayList<>();

    private List<MatTranItem> matTranDeleteItems = new ArrayList<>();

}
