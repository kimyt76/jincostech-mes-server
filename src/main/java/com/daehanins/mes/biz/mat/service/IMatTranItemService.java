package com.daehanins.mes.biz.mat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.mat.entity.MatTran;
import com.daehanins.mes.biz.mat.entity.MatTranItem;
import com.daehanins.mes.biz.mat.vo.MatTranMobileItem;
import com.daehanins.mes.biz.mobile.vo.MatTranSearchHistory;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;

import java.util.List;

/**
 * <p>
 * 자재거래품목MatTranItem 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-09
 */
public interface IMatTranItemService extends IService<MatTranItem> {

    List<MatTranMobileItem> getMobileItemList (String testNo );

    List<MatTranSearchHistory> getMatTranSearchHistory (String testNo );

    List<MatTranSearchHistory> getMatTranHistory ( String tranCd, String tran_date );

    MatTranItem saveMatTranItemByWorkEnd (MatTran matTran, WorkOrderItemView workOrderItemView);
}
