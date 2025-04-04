package com.daehanins.mes.biz.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.mat.entity.MatTran;
import com.daehanins.mes.biz.mat.entity.MatTranItem;
import com.daehanins.mes.biz.mat.vo.MatTranSaveWithItems;
import com.daehanins.mes.biz.work.entity.WorkOrder;
import com.daehanins.mes.biz.work.entity.WorkOrderItem;
import com.daehanins.mes.biz.work.vo.WorkOrderBatchItem;
import com.daehanins.mes.biz.work.vo.WorkOrderBatchSaveWithItems;
import com.daehanins.mes.biz.work.vo.WorkOrderReadWithItems;
import com.daehanins.mes.biz.work.vo.WorkOrderSaveWithItems;

import java.util.List;

/**
 * <p>
 * 작업지시WorkOrder 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-28
 */
public interface IWorkOrderService extends IService<WorkOrder> {

    WorkOrderReadWithItems getWithItems (String workOrderId);

    WorkOrderReadWithItems saveWithItems (WorkOrder wo, List<WorkOrderBatchSaveWithItems> woItems, List<WorkOrderBatchSaveWithItems> woDeleteItems) throws Exception;

    String deleteWithItems(String workOrderId) throws Exception;

    WorkOrderReadWithItems cancelBatchItems ( String workOrderBatchId ) throws Exception;

    boolean reorderWorkBatch (String workOrderBatchId);
}
