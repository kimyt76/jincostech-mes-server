package com.daehanins.mes.biz.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.work.entity.MatUseEtc;
import com.daehanins.mes.biz.work.entity.WorkOrderItem;

/**
 * <p>
 * MatUseEtc 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2021-05-03
 */
public interface IMatUseEtcService extends IService<MatUseEtc> {

    void saveByWorkOrderItem (WorkOrderItem workOrderItem) throws Exception;

    void deleteByWorkOrderItemId (String workOrderItemId);

    String finishMatUseEtc (String workOrderItemId);
}
