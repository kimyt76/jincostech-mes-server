package com.daehanins.mes.biz.work.vo;

import com.daehanins.mes.biz.work.entity.WorkOrder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WorkOrderSaveWithItems {

    private WorkOrder workOrder;

    private List<WorkOrderBatchSaveWithItems> workOrderBatchItems = new ArrayList<>();

    private List<WorkOrderBatchSaveWithItems> workOrderDeleteItems = new ArrayList<>();

}
