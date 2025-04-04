package com.daehanins.mes.biz.pub.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.pub.entity.Area;
import com.daehanins.mes.biz.pub.service.IAreaService;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 구역Area Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-22
 */
@RestController
@RequestMapping("/pub/area")
public class AreaController extends BaseController<Area, Area, String> {

    @Autowired
    private IAreaService areaService;

//    @Autowired
//    private IAreaViewService areaViewService;

    @Override
    public IAreaService getTableService() {
        return this.areaService;
    }

    @Override
    public IAreaService getViewService() {
    return this.areaService;
    }


    @RequestMapping(value = "/getProd",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<Area>> getProd(){

        QueryWrapper<Area> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("prod_yn","Y");
        List<Area> list = getTableService().list(queryWrapper);
        return new RestUtil<List<Area>>().setData(list);
    }

    @RequestMapping(value = "/getInProd",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<Area>> getInProd(){

        QueryWrapper<Area> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("prod_yn", "Y")
                .eq("inbound_yn", "Y");
        List<Area> list = getTableService().list(queryWrapper);
        return new RestUtil<List<Area>>().setData(list);
    }

    @RequestMapping(value = "/getInbound",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<Area>> getInbound(){

        QueryWrapper<Area> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("inbound_yn", "Y");
        List<Area> list = getTableService().list(queryWrapper);
        return new RestUtil<List<Area>>().setData(list);
    }

}
