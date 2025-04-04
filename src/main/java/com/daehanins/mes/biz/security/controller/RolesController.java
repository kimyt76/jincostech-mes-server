package com.daehanins.mes.biz.security.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.security.entity.Role;
import com.daehanins.mes.biz.security.service.IRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 역할Roles Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-26
 */
@RestController
@RequestMapping("/security/roles")
public class RolesController extends BaseController<Role, Role, String> {

    @Autowired
    private IRolesService rolesService;

//    @Autowired
//    private IRolesViewService rolesViewService;

    @Override
    public IRolesService getTableService() {
        return this.rolesService;
    }

    @Override
    public IRolesService getViewService() {
    return this.rolesService;
    }

}

