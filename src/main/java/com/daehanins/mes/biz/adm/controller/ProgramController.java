package com.daehanins.mes.biz.adm.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.adm.entity.Program;
import com.daehanins.mes.biz.adm.service.IProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 프로그램기능Program Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2022-07-25
 */
@RestController
@RequestMapping("/adm/program")
public class ProgramController extends BaseController<Program, Program, String> {

    @Autowired
    private IProgramService programService;

//    @Autowired
//    private IProgramViewService programViewService;

    @Override
    public IProgramService getTableService() {
        return this.programService;
    }

    @Override
    public IProgramService getViewService() {
    return this.programService;
    }

}

