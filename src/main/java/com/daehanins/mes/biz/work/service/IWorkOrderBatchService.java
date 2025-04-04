package com.daehanins.mes.biz.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.work.entity.WorkOrderBatch;

import java.util.List;

/**
 * <p>
 * WorkOrderBatch 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2021-02-22
 */
public interface IWorkOrderBatchService extends IService<WorkOrderBatch> {

    List<WorkOrderBatch> getByOrderId ( String workOrderId );

}
