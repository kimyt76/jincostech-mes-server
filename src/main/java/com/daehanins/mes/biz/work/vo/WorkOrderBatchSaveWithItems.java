package com.daehanins.mes.biz.work.vo;
import com.daehanins.mes.biz.work.entity.WorkOrderBatch;
import com.daehanins.mes.biz.work.entity.WorkOrderBatchView;
import com.daehanins.mes.biz.work.entity.WorkOrderItem;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;
import lombok.Data;


@Data
public class WorkOrderBatchSaveWithItems {

    private WorkOrderBatch batchInfo;

    private WorkOrderItem weighInfo;

    private WorkOrderItem prodInfo;

    private WorkOrderItem coatingInfo;

    private WorkOrderItem chargingInfo;

    private WorkOrderItem packingInfo;

}