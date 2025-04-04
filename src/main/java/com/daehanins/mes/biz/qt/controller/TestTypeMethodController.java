package com.daehanins.mes.biz.qt.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.qt.entity.TestTypeMethod;
import com.daehanins.mes.biz.qt.service.ITestTypeMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 품질검사유형상세TestTypeMethod Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-03
 */
@RestController
@RequestMapping("/qt/test-type-method")
public class TestTypeMethodController extends BaseController<TestTypeMethod, TestTypeMethod, String> {

    @Autowired
    private ITestTypeMethodService testTypeMethodService;

//    @Autowired
//    private ITestTypeMethodViewService testTypeMethodViewService;

    @Override
    public ITestTypeMethodService getTableService() {
        return this.testTypeMethodService;
    }

    @Override
    public ITestTypeMethodService getViewService() {
    return this.testTypeMethodService;
    }

}

