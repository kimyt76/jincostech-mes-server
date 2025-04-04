package com.daehanins.mes.biz.work.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.work.entity.*;
import com.daehanins.mes.biz.work.service.*;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * ProdProcess Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2023-02-17
 */
@RestController
@RequestMapping("/work/prod-process")
public class ProdProcessController extends BaseController<ProdProcess, ProdProcess, String> {

    @Autowired
    private IProdProcessService prodProcessService;

//    @Autowired
//    private IProdProcessViewService prodProcessViewService;

    @Autowired
    private IWorkOrderItemService workOrderItemService;

    @Autowired
    private IGoodsProcService goodsProcService;

    @Autowired
    private IGoodsProcViewService goodsProcViewService;

    @Autowired
    private IGoodsProcDetailService goodsProcDetailService;
    @Override
    public IProdProcessService getTableService() {
        return this.prodProcessService;
    }

    @Override
    public IProdProcessService getViewService() {
    return this.prodProcessService;
    }

    @RequestMapping(value = "/getByWorkOrderItemId/{workOrderItemId}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<ProdProcess>> getByWorkOrderItemId(@PathVariable String workOrderItemId) throws  Exception {

        //제조 공정 조회
        List<ProdProcess> list = getTableService().list(
                new QueryWrapper<ProdProcess>().eq("work_order_item_id", workOrderItemId).orderByAsc("prod_order")
        );

        //조회건이 0건 일 경우 체크
        if (list.isEmpty()){
            //제조공정 등록사항 체크
            WorkOrderItem workOrderItem = workOrderItemService.getById(workOrderItemId);
            String itemCd = workOrderItem.getItemCd();
            int cnt = goodsProcService.count(
                    new QueryWrapper<GoodsProc>().eq("item_cd", itemCd).eq("default_yn", "Y")
            );
            if(cnt == 0) {
                throw new Exception("등록된 제조공정이 없습니다. BOM 메뉴에서 등록을 진행하세요.");
            }
            GoodsProc goodsProc = goodsProcService.getOne(new QueryWrapper<GoodsProc>().eq("item_cd", itemCd).eq("default_yn", "Y"));
            List<GoodsProcDetail> goodsProcDetailList = goodsProcDetailService.list(
                    new QueryWrapper<GoodsProcDetail>().eq("goods_proc_id", goodsProc.getGoodsProcId()).orderByAsc("prod_order")
            );

            for (GoodsProcDetail item : goodsProcDetailList) {
                ProdProcess temp = new ProdProcess();
                temp.setWorkOrderItemId(workOrderItemId);
                temp.setProdOrder(item.getProdOrder());
                temp.setProdState(item.getProdState());
                temp.setDetail(item.getDetail());
                list.add(temp);
            }
            this.getTableService().saveBatch(list);
        }

        return new RestUtil<List<ProdProcess>>().setData(list);
    }

    @RequestMapping(value = "/regenProdProcess/{workOrderItemId}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<ProdProcess>> regenProdProcess(@PathVariable String workOrderItemId) throws  Exception {

        //기존 제조공정 내역 삭제
        if (!this.getTableService().remove(new QueryWrapper<ProdProcess>().eq("work_order_item_id", workOrderItemId))) {
            throw new Exception("기존 공정 내역 삭제 실패! 관리자에게 문의하세요");
        }
        return getByWorkOrderItemId(workOrderItemId);
    }

    @RequestMapping(value = "/saveProdProcess", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<List<ProdProcess>> saveProdProcess (@RequestBody List<ProdProcess> prodProcessList) {

        this.getTableService().saveOrUpdateBatch(prodProcessList);

        return new RestUtil<List<ProdProcess>>().setData(prodProcessList);
    }

    @RequestMapping(value = "/getProdWeighExcel/{workOrderItemId}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getProdWeighExcel (@PathVariable String workOrderItemId) throws Exception {
        return this.getTableService().getProdWeighExcel(workOrderItemId);
    }

    @RequestMapping(value = "/getProdProcExcel/{workOrderItemId}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getProdProcExcel (@PathVariable String workOrderItemId) throws Exception {
        return this.getTableService().getProdProcExcel(workOrderItemId);
    }

    @RequestMapping(value = "/getProdInputExcel/{workOrderItemId}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getProdInputExcel (@PathVariable String workOrderItemId) throws Exception {
        return this.getTableService().getProdInputExcel(workOrderItemId);
    }

}

