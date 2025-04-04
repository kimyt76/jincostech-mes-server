package com.daehanins.mes.biz.work.vo;
import com.daehanins.mes.biz.pub.entity.ItemMasterView;
import com.daehanins.mes.biz.work.entity.MatUseEtcView;
import com.daehanins.mes.biz.work.entity.ProdRecord;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;
import lombok.Data;

import java.util.List;


@Data
public class WorkOrderItemWithRecord {

    private WorkOrderItemView workOrderItemInfo;

    private ItemMasterView itemInfo;

    private List<MatUseEtcView> matUseEtcList;

    private List<ProdRecord> prodRecordList;

}