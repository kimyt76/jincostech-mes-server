package com.daehanins.mes.biz.work.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.daehanins.mes.biz.common.code.ProcessCd;
import com.daehanins.mes.biz.pub.entity.ItemMaster;
import com.daehanins.mes.biz.pub.service.IItemMasterService;
import com.daehanins.mes.biz.work.entity.ProdRecordView;
import com.daehanins.mes.biz.work.entity.WorkOrderItem;
import com.daehanins.mes.biz.work.service.IProdRecordViewService;
import com.daehanins.mes.biz.work.service.IWorkOrderItemService;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.work.entity.ProdRecord;
import com.daehanins.mes.biz.work.service.IProdRecordService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * ProdRecord Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2021-06-14
 */
@RestController
@RequestMapping("/work/prod-record")
public class ProdRecordController extends BaseController<ProdRecord, ProdRecordView, String> {

    @Autowired
    private IProdRecordService prodRecordService;

    @Autowired
    private IProdRecordViewService prodRecordViewService;

    @Override
    public IProdRecordService getTableService() {
        return this.prodRecordService;
    }

    @Override
    public IProdRecordViewService getViewService() {
        return this.prodRecordViewService;
    }


    @RequestMapping(value = "/saveProdRecord", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<ProdRecord> saveProdRecord (@RequestBody ProdRecord entity) {

        ProdRecord result = this.getTableService().saveProdRecord(entity);

    return new RestUtil<ProdRecord>().setData(result);
    }

}

