package com.daehanins.mes.biz.mat.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.mat.entity.*;
import com.daehanins.mes.biz.mat.service.IMatPointStockItemViewService;
import com.daehanins.mes.biz.mat.service.IMatPointStockService;
import com.daehanins.mes.biz.mat.service.IMatPointStockViewService;
import com.daehanins.mes.biz.mat.vo.*;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.utils.SearchUtil;
import com.daehanins.mes.common.vo.PageVo;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * MatPointStock Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-08-27
 */
@RestController
@RequestMapping("/mat/mat-point-stock")
public class MatPointStockController extends BaseController<MatPointStock, MatPointStockView, String> {

    @Autowired
    private IMatPointStockService matPointStockService;

    @Autowired
    private IMatPointStockViewService matPointStockViewService;

    @Autowired
    private IMatPointStockItemViewService matPointStockItemViewService;


    @Override
    public IMatPointStockService getTableService() {
        return this.matPointStockService;
    }

    @Override
    public IMatPointStockViewService getViewService() {
    return this.matPointStockViewService;
    }


    /**
     * 시점재고 생성
     */
    @RequestMapping(value = "/makeMatPointStock/{paramDate}", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<String> makeMatPointStock(@PathVariable String paramDate){

        // pdate : yyyy/mm/dd
        LocalDate calcDate = LocalDate.parse(paramDate);

        boolean result = matPointStockService.makeMatPointStock(calcDate);

        String message = result ? "OK":"Error";

        return new RestUtil<String>().setData(message);
    }


    /**
     * 시점재고 창고별 재생성 처리
     */
    @RequestMapping(value = "/renewMatPointStock/{id}", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<String> renewMatPointStock(@PathVariable String id){

        boolean result = matPointStockService.renewMatPointStock(id);

        String message = result ? "OK":"Error";

        return new RestUtil<String>().setData(message);
    }

    @RequestMapping(value = "/getWithItems/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatPointStockReadWithItems> getWithItems(@PathVariable String id){
        MatPointStockReadWithItems mpsItems = new MatPointStockReadWithItems();
        MatPointStock matPointStock = getTableService().getById(id);

        QueryWrapper<MatPointStockItemView> queryWrapper = new QueryWrapper<MatPointStockItemView>().eq(StringUtils.camelToUnderline("matPointStockId"), id);
        List<MatPointStockItemView> matPointStockItems = this.matPointStockItemViewService.list(queryWrapper);
        mpsItems.setMatPointStock(matPointStock);
        mpsItems.setMatPointStockItems(matPointStockItems);
        return new RestUtil<MatPointStockReadWithItems>().setData(mpsItems);
    }

    @RequestMapping(value = "/saveWithItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<MatPointStockSaveWithItems> saveWithItems(@RequestBody MatPointStockSaveWithItems requestParam){

        MatPointStock mps = requestParam.getMatPointStock();
        List<MatPointStockItem> mpsItems = requestParam.getMatPointStockItems();
        List<MatPointStockItem> mpsDeleteItems = requestParam.getMatPointStockDeleteItems();

        MatPointStockSaveWithItems data = getTableService().saveWithItems(mps, mpsItems, mpsDeleteItems);

        return new RestUtil<MatPointStockSaveWithItems>().setData(data);
    }

    @RequestMapping(value = "/deleteWithItems/{ids}",method = {RequestMethod.DELETE, RequestMethod.POST})
    @ResponseBody
    public RestResponse<Object> deleteWithItems(@PathVariable String[] ids){
        List<String> idList = Arrays.asList(ids);
        String msg = getTableService().deleteWithItems(idList);
        return new RestUtil<>().setMessage(msg);
    }

    @RequestMapping(value = "/getCurrentStockPage",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<Page<MatPointStockRead>> getCurrentStockPage(@ModelAttribute PageVo pageVo, @RequestParam (name="condition", required=false ) String conditionJson){
        Page<MatPointStockRead> page = new Page<>(pageVo.getCurrentPage(), pageVo.getPageSize());

        Map<String, Object> param = SearchUtil.parseParam(conditionJson);
        param.put("calcDate", LocalDate.now());
        Page<MatPointStockRead> data =getTableService().getPointStockItemPage(page, param);
        return new RestUtil<Page<MatPointStockRead>>().setData(data);
    }

}
