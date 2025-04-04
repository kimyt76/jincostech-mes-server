package com.daehanins.mes.biz.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.work.entity.MatUse;
import com.daehanins.mes.biz.work.entity.MatUseView;
import com.daehanins.mes.biz.mobile.vo.MatUseSaveItem;
import com.daehanins.mes.biz.work.entity.WorkOrderItem;

import java.util.List;

/**
 * <p>
 * 자재소요량MatUse 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-09
 */
public interface IMatUseService extends IService<MatUse> {

    List<MatUse> saveProdItems(List<MatUse> matUseList);

    void saveByWorkOrderItem (WorkOrderItem workOrderItem) throws Exception;

    void deleteByWorkOrderItemId (String workOrderItemId);

    List<MatUseView> investmentMatUse (MatUseSaveItem matUseSaveItem);

}
