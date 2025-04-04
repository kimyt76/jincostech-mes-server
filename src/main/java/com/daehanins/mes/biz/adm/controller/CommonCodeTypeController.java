package com.daehanins.mes.biz.adm.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.adm.entity.CommonCodeType;
import com.daehanins.mes.biz.adm.service.ICommonCodeTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 공통코드유형CommonCodeType Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-10-22
 */
@RestController
@RequestMapping("/adm/common-code-type")
public class CommonCodeTypeController extends BaseController<CommonCodeType, CommonCodeType, String> {

    @Autowired
    private ICommonCodeTypeService commonCodeTypeService;

//    @Autowired
//    private ICommonCodeTypeViewService commonCodeTypeViewService;

    @Override
    public ICommonCodeTypeService getTableService() {
        return this.commonCodeTypeService;
    }

    @Override
    public ICommonCodeTypeService getViewService() {
    return this.commonCodeTypeService;
    }

}

