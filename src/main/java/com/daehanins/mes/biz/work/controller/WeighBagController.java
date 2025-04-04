package com.daehanins.mes.biz.work.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.work.entity.WeighBag;
import com.daehanins.mes.biz.work.service.IWeighBagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 칭량용기WeighBag Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-09-10
 */
@RestController
@RequestMapping("/work/weigh-bag")
public class WeighBagController extends BaseController<WeighBag, WeighBag, String> {

    @Autowired
    private IWeighBagService weighBagService;

//    @Autowired
//    private IWeighBagViewService weighBagViewService;

    @Override
    public IWeighBagService getTableService() {
        return this.weighBagService;
    }

    @Override
    public IWeighBagService getViewService() {
    return this.weighBagService;
    }

}

