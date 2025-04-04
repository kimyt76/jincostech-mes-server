package com.daehanins.mes.biz.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.work.entity.ProdIng;
import com.daehanins.mes.biz.work.entity.WorkOrderItem;

/**
 * <p>
 * 제조진행ProdIng 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-22
 */
public interface IProdIngService extends IService<ProdIng> {

    void saveByWorkOrderItemId (String workOrderItemId, String workOrderBatchId);

    void deleteByWorkOrderItemId (String workOrderItemId);
}
