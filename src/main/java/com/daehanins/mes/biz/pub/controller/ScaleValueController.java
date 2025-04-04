package com.daehanins.mes.biz.pub.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.pub.entity.ScaleValue;
import com.daehanins.mes.biz.pub.entity.ScaleValueView;
import com.daehanins.mes.biz.pub.service.IScaleValueService;
import com.daehanins.mes.biz.pub.service.IScaleValueViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * ScaleValue Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2022-10-28
 */
@RestController
@RequestMapping("/pub/scale-value")
public class ScaleValueController extends BaseController<ScaleValue, ScaleValueView, String> {

    @Autowired
    private IScaleValueService scaleValueService;

    @Autowired
    private IScaleValueViewService scaleValueViewService;

    @Override
    public IScaleValueService getTableService() {
        return this.scaleValueService;
    }

    @Override
    public IScaleValueViewService getViewService() {
    return this.scaleValueViewService;
    }

}

