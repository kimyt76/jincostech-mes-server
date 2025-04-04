package com.daehanins.mes.biz.work.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.work.entity.MatUseEtc;
import com.daehanins.mes.biz.work.entity.MatUseEtcView;
import com.daehanins.mes.biz.work.service.IMatUseEtcService;
import com.daehanins.mes.biz.work.service.IMatUseEtcViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * MatUseEtc Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2021-05-03
 */
@RestController
@RequestMapping("/work/mat-use-etc")
public class MatUseEtcController extends BaseController<MatUseEtc, MatUseEtcView, String> {

    @Autowired
    private IMatUseEtcService matUseEtcService;

    @Autowired
    private IMatUseEtcViewService matUseEtcViewService;

    @Override
    public IMatUseEtcService getTableService() {
        return this.matUseEtcService;
    }

    @Override
    public IMatUseEtcViewService getViewService() {
    return this.matUseEtcViewService;
    }

}

