package com.daehanins.mes.biz.work.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.work.entity.*;
import com.daehanins.mes.biz.work.service.IMatUseService;
import com.daehanins.mes.biz.work.service.IMatWeighService;
import com.daehanins.mes.biz.work.vo.GoodsBomSaveWithItems;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.utils.SearchUtil;
import com.daehanins.mes.common.vo.PageVo;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * MatWeigh Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-09
 */
@RestController
@RequestMapping("/work/mat-weigh")
public class MatWeighController extends BaseController<MatWeigh, MatWeigh, String> {

    @Autowired
    private IMatWeighService matWeighService;

    @Autowired
    private IMatUseService matUseService;
//    @Autowired
//    private IMatWeighViewService matWeighViewService;

    @Override
    public IMatWeighService getTableService() {
        return this.matWeighService;
    }

    @Override
    public IMatWeighService getViewService() {
    return this.matWeighService;
    }

    @RequestMapping(value = "/saveItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<List<MatWeigh>> saveItems(@RequestBody List<MatWeigh> matWeighList) throws Exception {

        List<MatWeigh> data = this.getTableService().saveItems(matWeighList);
        return new RestUtil<List<MatWeigh>>().setData(data);
    }

    @RequestMapping(value = "/getByMatUseId/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<MatWeigh>>  getByMatUseId(@PathVariable String id){

        List<MatWeigh>  matWeighList= getViewService().list(new QueryWrapper<MatWeigh>().eq("mat_use_id", id));
        return new RestUtil<List<MatWeigh>>().setData(matWeighList);
    }

    @RequestMapping(value = "/deleteMatWeighOne/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<MatWeigh>>  deleteMatWeighOne(@PathVariable String id) throws Exception{

        List<MatWeigh> matWeighList= getViewService().deleteMatWeighOne(id);
        return new RestUtil<List<MatWeigh>>().setData(matWeighList);
    }
}

