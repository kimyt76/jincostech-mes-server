package com.daehanins.mes.biz.work.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.work.entity.Worker;
import com.daehanins.mes.biz.work.entity.WorkerView;
import com.daehanins.mes.biz.work.service.IWorkerService;
import com.daehanins.mes.biz.work.service.IWorkerViewService;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.utils.SearchUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * Worker Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-09-09
 */
@RestController
@RequestMapping("/work/worker")
public class WorkerController extends BaseController<Worker, WorkerView, String> {

    @Autowired
    private IWorkerService workerService;

    @Autowired
    private IWorkerViewService workerViewService;

    @Override
    public IWorkerService getTableService() {
        return this.workerService;
    }

    @Override
    public IWorkerViewService getViewService() {
    return this.workerViewService;
    }

}