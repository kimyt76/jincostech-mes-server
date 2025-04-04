package com.daehanins.mes.biz.adm.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.adm.entity.ErpConfig;
import com.daehanins.mes.biz.adm.service.IErpConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * ErpConfig Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-09
 */
@RestController
@RequestMapping("/adm/erp-config")
public class ErpConfigController extends BaseController<ErpConfig, ErpConfig, String> {

    @Autowired
    private IErpConfigService erpConfigService;

//    @Autowired
//    private IErpConfigViewService erpConfigViewService;

    @Override
    public IErpConfigService getTableService() {
        return this.erpConfigService;
    }

    @Override
    public IErpConfigService getViewService() {
    return this.erpConfigService;
    }

}

