package com.daehanins.mes.biz.work.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.work.entity.*;
import com.daehanins.mes.biz.work.service.IWorkOrderBatchViewService;
import com.daehanins.mes.biz.work.service.IWorkOrderItemViewService;
import com.daehanins.mes.biz.work.service.IWorkOrderService;
import com.daehanins.mes.biz.work.service.IWorkOrderViewService;
import com.daehanins.mes.biz.work.vo.WorkOrderBatchItem;
import com.daehanins.mes.biz.work.vo.WorkOrderBatchSaveWithItems;
import com.daehanins.mes.biz.work.vo.WorkOrderReadWithItems;
import com.daehanins.mes.biz.work.vo.WorkOrderSaveWithItems;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 작업지시WorkOrder Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2021-02-22
 */
@RestController
@RequestMapping("/work/work-order")
public class WorkOrderController extends BaseController<WorkOrder, WorkOrderView, String> {

    @Autowired
    private IWorkOrderService workOrderService;

    @Autowired
    private IWorkOrderViewService workOrderViewService;

    @Override
    public IWorkOrderService getTableService() {
        return this.workOrderService;
    }

    @Override
    public IWorkOrderViewService getViewService() {
        return this.workOrderViewService;
    }

    /** 주문 & 작업배치 & 작업지시 조회 **/
    @RequestMapping(value = "/getWithItems/{workOrderId}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<WorkOrderReadWithItems> getWithItems(@PathVariable String workOrderId) {

        WorkOrderReadWithItems resultData = this.getTableService().getWithItems(workOrderId);

        return new RestUtil<WorkOrderReadWithItems>().setData(resultData);
    }

    /** 주문 & 작업배치 & 작업지시 저장(수정) **/
    @RequestMapping(value = "/saveWithItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<WorkOrderReadWithItems> saveWithItems(@RequestBody WorkOrderSaveWithItems requestParam) throws Exception {

        WorkOrder wo = requestParam.getWorkOrder();
        List<WorkOrderBatchSaveWithItems> woItems = requestParam.getWorkOrderBatchItems();
        List<WorkOrderBatchSaveWithItems> woDeleteItems = requestParam.getWorkOrderDeleteItems();

        this.getTableService().saveWithItems(wo, woItems, woDeleteItems);
        WorkOrderReadWithItems resultData = this.getTableService().getWithItems(wo.getWorkOrderId());

        return new RestUtil<WorkOrderReadWithItems>().setData(resultData);
    }

    /** 주문 & 작업배치 & 작업지시 삭제 **/
    @RequestMapping(value = "/deleteWithItems/{workOrderId}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<String> deleteWithItems(@PathVariable String workOrderId) throws Exception {

        String message = workOrderService.deleteWithItems(workOrderId);

        return new RestUtil<String>().setData(message);
    }

    /** 작업배치 하위의 작업지시 취소 **/
    //TODO 해당 서비스가 미사용이 결정되면 삭제한다.
    @RequestMapping(value = "/cancelBatchItems/{workOrderBatchId}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<WorkOrderReadWithItems> cancelBatchItems(@PathVariable String workOrderBatchId) throws Exception {

        WorkOrderReadWithItems resultData = workOrderService.cancelBatchItems(workOrderBatchId);

        return new RestUtil<WorkOrderReadWithItems>().setData(resultData);
    }

    /** 작업배치 하위의 작업지시 초기화(재지시) **/
    @RequestMapping(value = "/reorderBatchItems/{workOrderBatchId}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<Boolean> reorderBatchItems(@PathVariable String workOrderBatchId) throws Exception {

        Boolean result = workOrderService.reorderWorkBatch(workOrderBatchId);

        return new RestUtil<Boolean>().setData(result);
    }
}


