package com.daehanins.mes.biz.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;

import java.util.List;

/**
 * <p>
 * WorkOrderItemView 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-28
 */
public interface IWorkOrderItemViewService extends IService<WorkOrderItemView> {

    List<WorkOrderItemView> getByWorkOrderId ( String workOrderId );

    int countUnmodifiableItem ( String workOrderId );

}
