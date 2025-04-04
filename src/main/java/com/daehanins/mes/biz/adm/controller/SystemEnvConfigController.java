package com.daehanins.mes.biz.adm.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.adm.entity.SystemEnvConfig;
import com.daehanins.mes.biz.adm.service.ISystemEnvConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * SystemEnvConfig Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2022-07-22
 */
@RestController
@RequestMapping("/admin/system-env-config")
public class SystemEnvConfigController extends BaseController<SystemEnvConfig, SystemEnvConfig, String> {

    @Autowired
    private ISystemEnvConfigService systemEnvConfigService;

//    @Autowired
//    private ISystemEnvConfigViewService systemEnvConfigViewService;

    @Override
    public ISystemEnvConfigService getTableService() {
        return this.systemEnvConfigService;
    }

    @Override
    public ISystemEnvConfigService getViewService() {
    return this.systemEnvConfigService;
    }

}

