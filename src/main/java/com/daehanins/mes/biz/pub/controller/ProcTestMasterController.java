package com.daehanins.mes.biz.pub.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.pub.entity.ProcTestMaster;
import com.daehanins.mes.biz.pub.entity.ProcTestMasterView;
import com.daehanins.mes.biz.pub.entity.ProcTestMethod;
import com.daehanins.mes.biz.pub.service.*;
import com.daehanins.mes.biz.pub.vo.ProcTestSaveItems;
import com.daehanins.mes.biz.pub.vo.ProcTestWithItems;
import com.daehanins.mes.biz.work.entity.WorkOrderItem;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;
import com.daehanins.mes.biz.work.service.IWorkOrderItemService;
import com.daehanins.mes.biz.work.service.IWorkOrderItemViewService;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * ProcTestMaster Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2023-03-14
 */
@RestController
@RequestMapping("/pub/proc-test-master")
public class ProcTestMasterController extends BaseController<ProcTestMaster, ProcTestMasterView, String> {

    @Autowired
    private IProcTestMasterService procTestMasterService;

    @Autowired
    private IProcTestMasterViewService procTestMasterViewService;

    @Autowired
    private IWorkOrderItemViewService workOrderItemViewService;

    @Autowired
    private IProcTestMethodService procTestMethodService;

    @Autowired
    private IProcTestEquipService procTestEquipService;

    @Autowired
    private IProcTestWorkerService procTestWorkerService;

    @Override
    public IProcTestMasterService getTableService() {
        return this.procTestMasterService;
    }

    @Override
    public IProcTestMasterViewService getViewService() {
    return this.procTestMasterViewService;
    }


    @RequestMapping(value = "/getProcTestWithItems/{id}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<ProcTestWithItems> getProcTestMaster(@PathVariable String id){

        //workOrderItemView 조회
        WorkOrderItemView workOrderItemView = workOrderItemViewService.getById(id);

        //prcTestMaster 유무 체크
        QueryWrapper<ProcTestMaster> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_order_item_id", id);
        int cnt = procTestMasterService.count(queryWrapper);

        if(cnt == 0) {
            ProcTestMaster procTestMaster = new ProcTestMaster();
            procTestMaster.setWorkOrderItemId(workOrderItemView.getWorkOrderItemId());
            procTestMaster.setAreaCd(workOrderItemView.getAreaCd());
            procTestMaster.setStorageCd(workOrderItemView.getStorageCd());
            procTestMaster.setTestState("ING");
            procTestMasterService.save(procTestMaster);
            procTestMethodService.initProcTestMethod(workOrderItemView.getProcessCd(), procTestMaster.getProcTestMasterId());
        }

        ProcTestMaster procTestMaster = this.getTableService().getByWorkOrderItemId(id);

        ProcTestWithItems result = new ProcTestWithItems();

        result.setWorkOrderItemView(workOrderItemView);
        result.setProcTestMaster(procTestMaster);
        result.setMethodList(procTestMethodService.getByMasterId(procTestMaster.getProcTestMasterId()));
        result.setEquipList(procTestEquipService.getByMasterId(procTestMaster.getProcTestMasterId()));
        result.setWorkerList(procTestWorkerService.getByMasterId(procTestMaster.getProcTestMasterId()));

        return new RestUtil<ProcTestWithItems>().setData(result);
    }

    @RequestMapping(value = "/saveProcTestMaster", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<ProcTestSaveItems> saveProcTestMaster(@RequestBody ProcTestSaveItems procTestSaveItems) {

        procTestMasterService.saveOrUpdate(procTestSaveItems.getProcTestMaster());

        return new RestUtil<ProcTestSaveItems>().setData(procTestSaveItems);
    }

    @RequestMapping(value = "/saveProcTestMethod", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<ProcTestSaveItems> saveProcTestMethod(@RequestBody ProcTestSaveItems procTestSaveItems) {

        procTestMethodService.saveOrUpdateBatch(procTestSaveItems.getMethodList());

        return new RestUtil<ProcTestSaveItems>().setData(procTestSaveItems);
    }

    @RequestMapping(value = "/saveProcTestEquip", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<ProcTestSaveItems> saveProcTestEquip(@RequestBody ProcTestSaveItems procTestSaveItems) {

         procTestEquipService.saveOrUpdateBatch(procTestSaveItems.getEquipList());

        return new RestUtil<ProcTestSaveItems>().setData(procTestSaveItems);
    }

    @RequestMapping(value = "/saveProcTestWorker", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<ProcTestSaveItems> saveProcTestWorker(@RequestBody ProcTestSaveItems procTestSaveItems) {

        procTestWorkerService.saveOrUpdateBatch(procTestSaveItems.getWorkerList());

        return new RestUtil<ProcTestSaveItems>().setData(procTestSaveItems);
    }

    @RequestMapping(value = "/getProcTestExcel/{workOrderItemId}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getProcTestExcel (@PathVariable String workOrderItemId) throws Exception {
        return this.getTableService().getProcTestExcel(workOrderItemId);
    }



}

