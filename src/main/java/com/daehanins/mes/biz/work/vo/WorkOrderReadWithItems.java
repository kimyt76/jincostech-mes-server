package com.daehanins.mes.biz.work.vo;

import com.daehanins.mes.biz.work.entity.WorkOrder;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;
import com.daehanins.mes.biz.work.entity.WorkOrderView;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WorkOrderReadWithItems {

    private WorkOrderView workOrderView;

    private List<WorkOrderBatchItem> workOrderBatchItems = new ArrayList<>();

}
