package com.daehanins.mes.biz.mat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.mat.entity.MatStockReal;
import com.daehanins.mes.biz.mat.entity.MatStockRealItem;
import com.daehanins.mes.biz.mat.vo.MatStockRealSaveWithItems;

import java.util.List;

/**
 * <p>
 * MatStockReal 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-13
 */
public interface IMatStockRealService extends IService<MatStockReal> {

    MatStockRealSaveWithItems saveWithItems(MatStockReal matStockReal, List<MatStockRealItem> matStockRealItems, List<MatStockRealItem> matStockRealDeleteItems);

    String deleteWithItems(List<String> idList);

}
