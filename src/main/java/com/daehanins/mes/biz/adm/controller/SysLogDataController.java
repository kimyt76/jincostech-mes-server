package com.daehanins.mes.biz.adm.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.adm.entity.SysLogData;
import com.daehanins.mes.biz.adm.service.ISysLogDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * SysLogData Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2023-05-30
 */
@RestController
@RequestMapping("/adm/sys-log-data")
public class SysLogDataController extends BaseController<SysLogData, SysLogData, String> {

    @Autowired
    private ISysLogDataService sysLogDataService;

//    @Autowired
//    private ISysLogDataViewService sysLogDataViewService;

    @Override
    public ISysLogDataService getTableService() {
        return this.sysLogDataService;
    }

    @Override
    public ISysLogDataService getViewService() {
    return this.sysLogDataService;
    }

}

