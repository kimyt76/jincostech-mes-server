package com.daehanins.mes.biz.mat.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.mat.entity.MatStockItem;
import com.daehanins.mes.biz.mat.service.IMatStockItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * MatStockItem Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-13
 */
@RestController
@RequestMapping("/mat/mat-stock-item")
public class MatStockItemController extends BaseController<MatStockItem, MatStockItem, String> {

    @Autowired
    private IMatStockItemService matStockItemService;

//    @Autowired
//    private IMatStockItemViewService matStockItemViewService;

    @Override
    public IMatStockItemService getTableService() {
        return this.matStockItemService;
    }

    @Override
    public IMatStockItemService getViewService() {
    return this.matStockItemService;
    }

}

