package com.daehanins.mes.biz.mat.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.mat.entity.MatStockRealItem;
import com.daehanins.mes.biz.mat.service.IMatStockRealItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * MatStockRealItem Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-13
 */
@RestController
@RequestMapping("/mat/mat-stock-real-item")
public class MatStockRealItemController extends BaseController<MatStockRealItem, MatStockRealItem, String> {

    @Autowired
    private IMatStockRealItemService matStockRealItemService;

//    @Autowired
//    private IMatStockRealItemViewService matStockRealItemViewService;

    @Override
    public IMatStockRealItemService getTableService() {
        return this.matStockRealItemService;
    }

    @Override
    public IMatStockRealItemService getViewService() {
    return this.matStockRealItemService;
    }

}

