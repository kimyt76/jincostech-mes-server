package com.daehanins.mes.biz.adm.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.adm.entity.ProgramGrp;
import com.daehanins.mes.biz.adm.service.IProgramGrpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * ProgramGrp Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2022-10-03
 */
@RestController
@RequestMapping("/adm/program-grp")
public class ProgramGrpController extends BaseController<ProgramGrp, ProgramGrp, String> {

    @Autowired
    private IProgramGrpService programGrpService;

//    @Autowired
//    private IProgramGrpViewService programGrpViewService;

    @Override
    public IProgramGrpService getTableService() {
        return this.programGrpService;
    }

    @Override
    public IProgramGrpService getViewService() {
    return this.programGrpService;
    }

}

