package com.daehanins.mes.biz.tag.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.tag.entity.UserDateLog;
import com.daehanins.mes.biz.tag.service.IUserDateLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * UserDateLog Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2022-10-30
 */
@RestController
@RequestMapping("/tag/user-date-log")
public class UserDateLogController extends BaseController<UserDateLog, UserDateLog, String> {

    @Autowired
    private IUserDateLogService userDateLogService;

//    @Autowired
//    private IUserDateLogViewService userDateLogViewService;

    @Override
    public IUserDateLogService getTableService() {
        return this.userDateLogService;
    }

    @Override
    public IUserDateLogService getViewService() {
    return this.userDateLogService;
    }

}

