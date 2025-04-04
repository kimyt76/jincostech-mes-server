package com.daehanins.mes.biz.work.vo;
import com.daehanins.mes.biz.work.entity.WorkOrderBatch;
import com.daehanins.mes.biz.work.entity.WorkOrderBatchView;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;
import lombok.Data;


@Data
public class WorkOrderBatchItem {

    private WorkOrderBatch batchInfo;

    private WorkOrderItemView weighInfo;

    private WorkOrderItemView prodInfo;

    private WorkOrderItemView coatingInfo;

    private WorkOrderItemView chargingInfo;

    private WorkOrderItemView packingInfo;

    public void initWorkOrderBatchItem ( WorkOrderBatch workOrderBatch ) {
        this.batchInfo = workOrderBatch;
        this.weighInfo = new WorkOrderItemView();
        this.prodInfo = new WorkOrderItemView();
        this.coatingInfo = new WorkOrderItemView();
        this.chargingInfo = new WorkOrderItemView();
        this.packingInfo = new WorkOrderItemView();
    }
}