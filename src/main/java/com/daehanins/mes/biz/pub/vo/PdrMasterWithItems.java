package com.daehanins.mes.biz.pub.vo;

import com.daehanins.mes.biz.pub.entity.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PdrMasterWithItems {

    private PdrMasterView pdrMasterView;

    private List<PdrSellView> pdrSellList;

    private List<PdrLaborView> pdrLaborList;

    private List<PdrExpense> pdrExpenseList;

}
