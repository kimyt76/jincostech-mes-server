package com.daehanins.mes.biz.adm.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.adm.entity.UserLog;
import com.daehanins.mes.biz.adm.service.IUserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 사용자로그UserLog Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2022-07-25
 */
@RestController
@RequestMapping("/adm/user-log")
public class UserLogController extends BaseController<UserLog, UserLog, String> {

    @Autowired
    private IUserLogService userLogService;

//    @Autowired
//    private IUserLogViewService userLogViewService;

    @Override
    public IUserLogService getTableService() {
        return this.userLogService;
    }

    @Override
    public IUserLogService getViewService() {
    return this.userLogService;
    }

}

