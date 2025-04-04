package com.daehanins.mes.biz.pub.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.pub.entity.ProcTestType;
import com.daehanins.mes.biz.pub.entity.ProcTestTypeMethod;
import com.daehanins.mes.biz.pub.service.IProcTestTypeMethodService;
import com.daehanins.mes.biz.pub.service.IProcTestTypeService;
import com.daehanins.mes.biz.pub.vo.ProcTestTypeWithItems;
import com.daehanins.mes.biz.pub.vo.ProcTestWithItems;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * ProcTestType Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2023-04-03
 */
@RestController
@RequestMapping("/pub/proc-test-type")
public class ProcTestTypeController extends BaseController<ProcTestType, ProcTestType, String> {

    @Autowired
    private IProcTestTypeService procTestTypeService;

    @Autowired
    private IProcTestTypeMethodService procTestTypeMethodService;

    @Override
    public IProcTestTypeService getTableService() {
        return this.procTestTypeService;
    }

    @Override
    public IProcTestTypeService getViewService() {
        return this.procTestTypeService;
    }

    @RequestMapping(value = "/getProcTestTypeWithItems/{id}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<ProcTestTypeWithItems> getProcTestTypeWithItems(@PathVariable String id){

        ProcTestType procTestType = this.getTableService().getById(id);
        List<ProcTestTypeMethod> procTestTypeMethodList = procTestTypeMethodService.getByTestType(procTestType.getTestType());

        ProcTestTypeWithItems result = new ProcTestTypeWithItems();
        result.setProcTestType(procTestType);
        result.setMethodList(procTestTypeMethodList);
        return new RestUtil<ProcTestTypeWithItems>().setData(result);
    }

    @RequestMapping(value = "/saveProcTestTypeWithItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<ProcTestTypeWithItems> saveProcTestTypeWithItems(@RequestBody ProcTestTypeWithItems procTestTypeWithItems) {

        this.getTableService().saveOrUpdate(procTestTypeWithItems.getProcTestType());
        procTestTypeMethodService.saveOrUpdateBatch(procTestTypeWithItems.getMethodList());

        return new RestUtil<ProcTestTypeWithItems>().setData(procTestTypeWithItems);
    }

}

