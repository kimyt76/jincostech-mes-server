package com.daehanins.mes.biz.pub.vo;

import com.daehanins.mes.biz.pub.entity.*;
import lombok.Data;

import java.util.List;

@Data
public class PdrSellWithItems {

    private PdrSellView pdrSellView;

    private List<PdrMatView> pdrMatViewList;

    private List<PdrMatSubView> pdrMatSubViewList;

    private List<PdrWorkerView> pdrWorkerViewList;

}
