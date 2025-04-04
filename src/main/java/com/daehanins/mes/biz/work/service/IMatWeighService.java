package com.daehanins.mes.biz.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.work.entity.MatWeigh;
import com.daehanins.mes.biz.work.entity.WorkOrder;
import com.daehanins.mes.biz.work.entity.WorkOrderItem;
import com.daehanins.mes.biz.work.vo.WorkOrderSaveWithItems;

import java.util.List;

/**
 * <p>
 * MatWeigh 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-09
 */
public interface IMatWeighService extends IService<MatWeigh> {

    List<MatWeigh> saveItems(List<MatWeigh> matWeighList) throws Exception;

    List<MatWeigh> deleteMatWeighOne(String matWeighId) throws Exception;
}
