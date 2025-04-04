package com.daehanins.mes.biz.work.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.work.entity.CosFormula;
import com.daehanins.mes.biz.work.service.ICosFormulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * CosFormula Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-08-24
 */
@RestController
@RequestMapping("/work/cos-formula")
public class CosFormulaController extends BaseController<CosFormula, CosFormula, String> {

    @Autowired
    private ICosFormulaService cosFormulaService;

//    @Autowired
//    private ICosFormulaViewService cosFormulaViewService;

    @Override
    public ICosFormulaService getTableService() {
        return this.cosFormulaService;
    }

    @Override
    public ICosFormulaService getViewService() {
    return this.cosFormulaService;
    }

}

