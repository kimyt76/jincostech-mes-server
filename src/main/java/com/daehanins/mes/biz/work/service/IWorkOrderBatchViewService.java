package com.daehanins.mes.biz.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.work.entity.WorkOrderBatchView;

import java.util.List;

/**
 * <p>
 * WorkOrderBatchView 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2021-02-22
 */
public interface IWorkOrderBatchViewService extends IService<WorkOrderBatchView> {

    List<WorkOrderBatchView> getByWorkOrderId (String workOrderId );

}
