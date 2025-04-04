package com.daehanins.mes.biz.mat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.mat.entity.MatTran;
import com.daehanins.mes.biz.mat.entity.MatTranItem;
import com.daehanins.mes.biz.mat.vo.MatTranSaveWithItems;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;

import java.util.List;

/**
 * <p>
 * 자재거래MatTran 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-09
 */
public interface IMatTranService extends IService<MatTran> {

    MatTranSaveWithItems saveWithItems(MatTran matTran, List<MatTranItem> matTranItems, List<MatTranItem> matTranDeleteItems);

    MatTranSaveWithItems saveWithItemsByMobile(MatTran matTran, List<MatTranItem> matTranItems);

    String deleteWithItems(List<String> idList);

    String saveMatTranByWeighEnd (WorkOrderItemView workOrderItemView);

    String saveMatUseEtcTranByWorkEnd (WorkOrderItemView workOrderItemView);

    boolean isExistMatUseEtc (WorkOrderItemView workOrderItemView);

    String updateMatUseEtcTran (WorkOrderItemView workOrderItemView);

    MatTran saveMatTranByWorkEnd (WorkOrderItemView workOrderItemView);

}
