package com.daehanins.mes.biz.security.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.security.entity.UserRole;
import com.daehanins.mes.biz.security.service.IUserRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * UserRoles Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-26
 */
@RestController
@RequestMapping("/security/user-roles")
public class UserRolesController extends BaseController<UserRole, UserRole, String> {

    @Autowired
    private IUserRolesService userRolesService;

//    @Autowired
//    private IUserRolesViewService userRolesViewService;

    @Override
    public IUserRolesService getTableService() {
        return this.userRolesService;
    }

    @Override
    public IUserRolesService getViewService() {
    return this.userRolesService;
    }

}

