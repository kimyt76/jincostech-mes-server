package com.daehanins.mes.biz.adm.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.adm.entity.CommonCode;
import com.daehanins.mes.biz.adm.service.ICommonCodeService;
import com.daehanins.mes.biz.common.schedule.BatchSchedule;
import com.daehanins.mes.biz.common.service.SmartFactoryLogService;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 공통코드CommonCode Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-04
 */
@RestController
@RequestMapping("/adm/common-code")
public class CommonCodeController extends BaseController<CommonCode, CommonCode, String> {

    @Autowired
    private ICommonCodeService commonCodeService;

//    @Autowired
//    private ICommonCodeViewService commonCodeViewService;

    @Autowired
    SmartFactoryLogService smartFactoryLogService;

    @Override
    public ICommonCodeService getTableService() {
        return this.commonCodeService;
    }

    @Override
    public ICommonCodeService getViewService() {
        return this.commonCodeService;
    }

    @RequestMapping(value = "/getByType/{codeType}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<CommonCode>> getByType(@PathVariable String codeType){
        return new RestUtil<List<CommonCode>>().setData(getTableService().getByCodeType(codeType));
    }

    @RequestMapping(value = "/sendTest",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<Object> sendTest(){
        smartFactoryLogService.sendSystemLogData();
        String msg = "";
        return new RestUtil<>().setMessage(msg);
    }

    @RequestMapping(value = "/saveTest",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<Object> saveTest(){
        smartFactoryLogService.saveSystemLogData();
        String msg = "";
        return new RestUtil<>().setMessage(msg);
    }

    @RequestMapping(value = "/sendTestDt/{dt}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<Object> sendTestDt(@PathVariable String dt){
        smartFactoryLogService.sendSystemLogDataByDate(dt);
        String msg = "";
        return new RestUtil<>().setMessage(msg);
    }

    @RequestMapping(value = "/saveTestDt/{dt}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<Object> saveTestDt(@PathVariable String dt){
        smartFactoryLogService.saveSystemLogDataByDate(dt);
        String msg = "";
        return new RestUtil<>().setMessage(msg);
    }
}

