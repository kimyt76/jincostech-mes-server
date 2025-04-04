package com.daehanins.mes.biz.mat.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.mat.entity.TranType;
import com.daehanins.mes.biz.mat.service.ITranTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 자재거래유형TranType Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-22
 */
@RestController
@RequestMapping("/mat/tran-type")
public class TranTypeController extends BaseController<TranType, TranType, String> {

    @Autowired
    private ITranTypeService tranTypeService;

//    @Autowired
//    private ITranTypeViewService tranTypeViewService;

    @Override
    public ITranTypeService getTableService() {
        return this.tranTypeService;
    }

    @Override
    public ITranTypeService getViewService() {
    return this.tranTypeService;
    }

}

