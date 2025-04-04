package com.daehanins.mes.biz.web.controller;


import com.daehanins.mes.biz.mat.entity.MatOrder;
import com.daehanins.mes.biz.mat.service.IMatOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 구매발주PurchaseOrder Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-02
 */
@Controller
@RequestMapping("/web/purchase")
public class PurchaseWebController {

    @Autowired
    private IMatOrderService matOrderService;

    @RequestMapping(value = "/order/{matOrderId}", method = RequestMethod.GET)
    public ModelAndView get(@PathVariable String matOrderId){
        ModelAndView mnv = new ModelAndView();

        MatOrder mo = this.matOrderService.getById(matOrderId);

        mnv.addObject("matOrder", mo);

        mnv.setViewName("purchaseOrderSheet");

        return mnv;
    }

}
