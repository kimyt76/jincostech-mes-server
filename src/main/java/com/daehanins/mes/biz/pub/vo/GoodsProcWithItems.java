package com.daehanins.mes.biz.pub.vo;

import com.daehanins.mes.biz.work.entity.GoodsProc;
import com.daehanins.mes.biz.work.entity.GoodsProcDetail;
import lombok.Data;

import java.util.List;

@Data
public class GoodsProcWithItems {

    private GoodsProc goodsProc;

    private List<GoodsProcDetail> goodsProcDetailList;

}
