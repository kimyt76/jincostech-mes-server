package com.daehanins.mes.biz.work.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.work.entity.MatUseEtcResult;
import com.daehanins.mes.biz.work.entity.MatUseEtcResultView;
import com.daehanins.mes.biz.work.service.IMatUseEtcResultService;
import com.daehanins.mes.biz.work.service.IMatUseEtcResultViewService;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * MatUseEtcResult Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2021-05-03
 */
@RestController
@RequestMapping("/work/mat-use-etc-result")
public class MatUseEtcResultController extends BaseController<MatUseEtcResult, MatUseEtcResultView, String> {

    @Autowired
    private IMatUseEtcResultService matUseEtcResultService;

    @Autowired
    private IMatUseEtcResultViewService matUseEtcResultViewService;

    @Override
    public IMatUseEtcResultService getTableService() {
        return this.matUseEtcResultService;
    }

    @Override
    public IMatUseEtcResultViewService getViewService() {
    return this.matUseEtcResultViewService;
    }

    @RequestMapping(value = "/saveItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<List<MatUseEtcResult>> saveItems(@RequestBody List<MatUseEtcResult> matUseEtcResultList) throws Exception {
        List<MatUseEtcResult> data = this.getTableService().saveItems(matUseEtcResultList);
        return new RestUtil<List<MatUseEtcResult>>().setData(data);
    }

    @RequestMapping(value = "/getByMatUseEtcId/{matUseEctId}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<MatUseEtcResultView>>  getByMatUseId(@PathVariable String matUseEctId){
        List<MatUseEtcResultView>  matUseEtcResultList = this.getViewService().list(new QueryWrapper<MatUseEtcResultView>().eq("mat_use_etc_id", matUseEctId));
        return new RestUtil<List<MatUseEtcResultView>>().setData(matUseEtcResultList);
    }

}

