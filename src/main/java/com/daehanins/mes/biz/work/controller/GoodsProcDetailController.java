package com.daehanins.mes.biz.work.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.pub.vo.GoodsProcWithItems;
import com.daehanins.mes.biz.work.entity.GoodsProcDetail;
import com.daehanins.mes.biz.work.service.IGoodsProcDetailService;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * GoodsProcDetail Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2023-02-16
 */
@RestController
@RequestMapping("/work/goods-proc-detail")
public class GoodsProcDetailController extends BaseController<GoodsProcDetail, GoodsProcDetail, String> {

    @Autowired
    private IGoodsProcDetailService goodsProcDetailService;

//    @Autowired
//    private IGoodsProcDetailViewService goodsProcDetailViewService;

    @Override
    public IGoodsProcDetailService getTableService() {
        return this.goodsProcDetailService;
    }

    @Override
    public IGoodsProcDetailService getViewService() {
        return this.goodsProcDetailService;
    }

}

