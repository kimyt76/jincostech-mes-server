package com.daehanins.mes.biz.mat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.mat.entity.MatOrder;
import com.daehanins.mes.biz.mat.entity.MatOrderItem;
import com.daehanins.mes.biz.mat.vo.MatOrderSaveWithItems;

import java.util.List;

/**
 * <p>
 * 자재지시(요청)MatOrder 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-22
 */
public interface IMatOrderService extends IService<MatOrder> {

    MatOrderSaveWithItems saveWithItems(MatOrder matOrder, List<MatOrderItem> matOrderItems, List<MatOrderItem> matOrderDeleteItems);

    String deleteWithItems(List<String> idList);
    
}
