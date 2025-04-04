package com.daehanins.mes.biz.pub.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.pub.entity.ProcTestTypeMethod;
import com.daehanins.mes.biz.pub.service.IProcTestTypeMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * ProcTestTypeMethod Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2023-03-14
 */
@RestController
@RequestMapping("/pub/proc-test-type-method")
public class ProcTestTypeMethodController extends BaseController<ProcTestTypeMethod, ProcTestTypeMethod, String> {

    @Autowired
    private IProcTestTypeMethodService procTestTypeMethodService;

//    @Autowired
//    private IProcTestTypeMethodViewService procTestTypeMethodViewService;

    @Override
    public IProcTestTypeMethodService getTableService() {
        return this.procTestTypeMethodService;
    }

    @Override
    public IProcTestTypeMethodService getViewService() {
    return this.procTestTypeMethodService;
    }

}

