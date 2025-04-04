package com.daehanins.mes.biz.work.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.work.entity.MatUse;
import com.daehanins.mes.biz.work.entity.MatUseView;
import com.daehanins.mes.biz.work.entity.ProdIng;
import com.daehanins.mes.biz.work.entity.ProdIngView;
import com.daehanins.mes.biz.work.service.IProdIngService;
import com.daehanins.mes.biz.work.service.IProdIngViewService;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 제조진행ProdIng Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-22
 */
@RestController
@RequestMapping("/work/prod-ing")
public class ProdIngController extends BaseController<ProdIng, ProdIngView, String> {

    @Autowired
    private IProdIngService prodIngService;

    @Autowired
    private IProdIngViewService prodIngViewService;

    @Override
    public IProdIngService getTableService() {
        return this.prodIngService;
    }

    @Override
    public IProdIngViewService getViewService() {
    return this.prodIngViewService;
    }

    @RequestMapping(value = "/saveItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<List<ProdIng>> saveItems(@RequestBody List<ProdIng> prodIngList){

        List<ProdIng> data = new ArrayList<ProdIng>();
        // item 신규,수정 처리
        prodIngList.forEach( item -> {
            this.prodIngService.saveOrUpdate(item);
            data.add(item);
        });

        return new RestUtil<List<ProdIng>>().setData(data);

    }

    @RequestMapping(value = "/getByWorkOrderItemId/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<ProdIngView>> getByWorkOrderItemId(@PathVariable String id){

        List<ProdIngView>  prodIngViewList= getViewService().list(new QueryWrapper<ProdIngView>().eq("work_order_item_id", id));
        return new RestUtil<List<ProdIngView>>().setData(prodIngViewList);
    }

}

