package com.daehanins.mes.biz.mat.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.mat.entity.MatStockReal;
import com.daehanins.mes.biz.mat.entity.MatStockRealItem;
import com.daehanins.mes.biz.mat.entity.MatStockRealItemView;
import com.daehanins.mes.biz.mat.entity.MatStockRealView;
import com.daehanins.mes.biz.mat.service.IMatStockRealItemViewService;
import com.daehanins.mes.biz.mat.service.IMatStockRealService;
import com.daehanins.mes.biz.mat.service.IMatStockRealViewService;
import com.daehanins.mes.biz.mat.vo.MatStockRealReadWithItems;
import com.daehanins.mes.biz.mat.vo.MatStockRealSaveWithItems;
import com.daehanins.mes.biz.work.vo.GoodsBomExcel;
import com.daehanins.mes.biz.work.vo.GoodsBomReadWithItems;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * MatStockReal Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-13
 */
@RestController
@RequestMapping("/mat/mat-stock-real")
public class MatStockRealController extends BaseController<MatStockReal, MatStockRealView, String> {

    @Autowired
    private IMatStockRealService matStockRealService;

    @Autowired
    private IMatStockRealViewService matStockRealViewService;

    @Autowired
    private IMatStockRealItemViewService matStockRealItemViewService;

    @Override
    public IMatStockRealService getTableService() {
        return this.matStockRealService;
    }

    @Override
    public IMatStockRealViewService getViewService() {
    return this.matStockRealViewService;
    }

    @RequestMapping(value = "/getWithItems/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatStockRealReadWithItems> getWithItems(@PathVariable String id){
        MatStockRealReadWithItems msrItems = new MatStockRealReadWithItems();
        MatStockReal matStockReal = getTableService().getById(id);

        QueryWrapper<MatStockRealItemView> queryWrapper = new QueryWrapper<MatStockRealItemView>().eq(StringUtils.camelToUnderline("matStockRealId"), id);
        List<MatStockRealItemView> matStockRealItems = this.matStockRealItemViewService.list(queryWrapper);
        msrItems.setMatStockReal(matStockReal);
        msrItems.setMatStockRealItems(matStockRealItems);
        return new RestUtil<MatStockRealReadWithItems>().setData(msrItems);
    }

    @RequestMapping(value = "/saveWithItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<MatStockRealSaveWithItems> saveWithItems(@RequestBody MatStockRealSaveWithItems requestParam){

        MatStockReal msr = requestParam.getMatStockReal();
        List<MatStockRealItem> msrItems = requestParam.getMatStockRealItems();
        List<MatStockRealItem> msrDeleteItems = requestParam.getMatStockRealDeleteItems();

        MatStockRealSaveWithItems data = getTableService().saveWithItems(msr, msrItems, msrDeleteItems);

        return new RestUtil<MatStockRealSaveWithItems>().setData(data);
    }

    @RequestMapping(value = "/deleteWithItems/{ids}",method = {RequestMethod.DELETE, RequestMethod.POST})
    @ResponseBody
    public RestResponse<Object> deleteWithItems(@PathVariable String[] ids){
        List<String> idList = Arrays.asList(ids);
        String msg = getTableService().deleteWithItems(idList);
        return new RestUtil<>().setMessage(msg);
    }

    @RequestMapping(value = "/end", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatStockReal> orderEnd(@RequestParam String id, @RequestParam String endYn){
        MatStockReal entity = getTableService().getById(id);
        entity.setEndYn(endYn);
        getTableService().updateById(entity);
        return new RestUtil<MatStockReal>().setData(entity);
    }


//    @RequestMapping(value = "/uploadItems", method = {RequestMethod.POST, RequestMethod.PUT})
////    @ResponseBody
////    public RestResponse<MatStockRealReadWithItems> uploadItems(@RequestBody List<MatStockRealExcel> matStockRealExcelList) throws Exception {
////
////        MatStockRealReadWithItems data = getTableService().uploadItems(matStockRealExcelList);
////
////        return new RestUtil<MatStockRealReadWithItems>().setData(data);
////    }

}

