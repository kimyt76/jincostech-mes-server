package com.daehanins.mes.biz.mat.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.mat.entity.MatPointStockItem;
import com.daehanins.mes.biz.mat.service.IMatPointStockItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * MatPointStockItem Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-08-27
 */
@RestController
@RequestMapping("/mat/mat-point-stock-item")
public class MatPointStockItemController extends BaseController<MatPointStockItem, MatPointStockItem, String> {

    @Autowired
    private IMatPointStockItemService matPointStockItemService;

//    @Autowired
//    private IMatPointStockItemViewService matPointStockItemViewService;

    @Override
    public IMatPointStockItemService getTableService() {
        return this.matPointStockItemService;
    }

    @Override
    public IMatPointStockItemService getViewService() {
    return this.matPointStockItemService;
    }

}

