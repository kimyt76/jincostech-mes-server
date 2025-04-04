package com.daehanins.mes.biz.pub.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.pub.entity.Process;
import com.daehanins.mes.biz.pub.entity.ProcessView;
import com.daehanins.mes.biz.pub.service.IProcessService;
import com.daehanins.mes.biz.pub.service.IProcessViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 공정Process Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-04
 */
@RestController
@RequestMapping("/pub/process")
public class ProcessController extends BaseController<Process, ProcessView, String> {

    @Autowired
    private IProcessService processService;

    @Autowired
    private IProcessViewService processViewService;

    @Override
    public IProcessService getTableService() {
        return this.processService;
    }

    @Override
    public IProcessViewService getViewService() {
    return this.processViewService;
    }

}

