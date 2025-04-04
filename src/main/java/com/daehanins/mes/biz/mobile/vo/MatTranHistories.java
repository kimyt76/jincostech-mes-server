package com.daehanins.mes.biz.mobile.vo;

import com.daehanins.mes.biz.mobile.vo.MatTranSearchHistory;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MatTranHistories {

    private List<MatTranSearchHistory> matMoveList = new ArrayList<>();

    private List<MatTranSearchHistory> matAdjustList = new ArrayList<>();

}
