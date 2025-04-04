package com.daehanins.mes.biz.mat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.mat.entity.MatOrderItem;
import com.daehanins.mes.biz.mat.vo.MatOrderItemInfoVo;
import com.daehanins.mes.biz.mat.vo.MatOrderTranItemSum;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 자재지시(요청)품목MatOrderItem 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-22
 */
public interface IMatOrderItemService extends IService<MatOrderItem> {

    List<MatOrderTranItemSum> getMatOrderTranItemSum(String matOrderId );

    boolean updateOrderItemEndYn(String id, String itemEndYn);
    HashMap<String, Object> getItemEndYnInfo(String id);

    List<MatOrderItemInfoVo> getMatOrderItemList(String id);
}
