package com.daehanins.mes.biz.work.controller;


import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.common.code.ConfirmState;
import com.daehanins.mes.biz.common.code.ItemTypeCd;
import com.daehanins.mes.biz.common.code.TranCd;
import com.daehanins.mes.biz.common.code.WorkItemState;
import com.daehanins.mes.biz.common.vo.LabelPrint;
import com.daehanins.mes.biz.mat.entity.*;
import com.daehanins.mes.biz.mat.service.IMatTranItemService;
import com.daehanins.mes.biz.mat.service.IMatTranService;
import com.daehanins.mes.biz.mat.vo.MatLabelItem;
import com.daehanins.mes.biz.pub.entity.ItemMaster;
import com.daehanins.mes.biz.pub.entity.ItemMasterView;
import com.daehanins.mes.biz.pub.service.IItemMasterService;
import com.daehanins.mes.biz.pub.service.IItemMasterViewService;
import com.daehanins.mes.biz.qt.entity.ItemTestNoView;
import com.daehanins.mes.biz.qt.service.IItemTestNoViewService;
import com.daehanins.mes.biz.work.entity.ProdResult;
import com.daehanins.mes.biz.work.entity.ProdResultView;
import com.daehanins.mes.biz.work.entity.WorkOrderItem;
import com.daehanins.mes.biz.work.service.IProdResultService;
import com.daehanins.mes.biz.work.service.IProdResultViewService;
import com.daehanins.mes.biz.work.service.IWorkOrderItemService;
import com.daehanins.mes.biz.work.vo.ProdLabelItem;
import com.daehanins.mes.common.utils.JasperUtil;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 생산실적ProdResult Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-25
 */
@RestController
@RequestMapping("/work/prod-result")
public class ProdResultController extends BaseController<ProdResult, ProdResultView, String> {

    @Autowired
    private IProdResultService prodResultService;

    @Autowired
    private IProdResultViewService prodResultViewService;

    @Autowired
    private IItemMasterService itemMasterService;

    @Autowired
    private IItemMasterViewService itemMasterViewService;

    @Autowired
    private IItemTestNoViewService itemTestNoViewService;

    @Autowired
    private IWorkOrderItemService workOrderItemService;

    @Autowired
    private IMatTranService matTranService;

    @Autowired
    private IMatTranItemService matTranItemService;


    @Override
    public IProdResultService getTableService() {
        return this.prodResultService;
    }

    @Override
    public IProdResultViewService getViewService() {
        return this.prodResultViewService;
    }

    @RequestMapping(value = "/getViewByWorkOrder/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<ProdResultView> getViewByWorkOrder(@PathVariable String id) {

        ProdResultView entity = getViewService().getByWorkOrderItemId(id);
        return new RestUtil<ProdResultView>().setData(entity);
    }

    @RequestMapping(value = "/saveData", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<ProdResultView> saveData(@RequestBody ProdResult entity){

        this.getTableService().saveData(entity);    // 제조실적 등록과 시험번호 생성 동시 처리

        // 제조실적 생산량이 0보다 큰 값
        if (entity.getProdQty().compareTo(BigDecimal.ZERO) > 0) {
            // work_order_item 테이블의 상태를 작업완료로 처리한다.
            String workOrderItemId = entity.getWorkOrderItemId();
            WorkOrderItem workOrderItem = this.workOrderItemService.getById(workOrderItemId);
            workOrderItem.setProdQty(entity.getProdQty()); //생산량 등록
            workOrderItem.setWorkOrderItemStatus(WorkItemState.제조완료);  // 제조완료
            this.workOrderItemService.saveOrUpdate(workOrderItem);
        }

        MatTran matTran = new MatTran();
        MatTranItem matTranItem = new MatTranItem();

        // TRAN에 아직 반영되지 않은 경우, 신규생성
        if (!entity.getProdTranYn().equals("Y")) {
            // 제조입고 B
            String tranCd = TranCd.B;
            // 외주생산인 경우 제조입고(외주):C 로 처리한다.
            if (entity.getAreaCd().equals("Z001")) {
                tranCd = TranCd.C;
            }
            matTran.setTranCd(tranCd);     // 제조입고:B  또는 제조입고(외주):C
            matTran.setTranDate(entity.getProdDate());
            matTran.setAreaCd(entity.getAreaCd());
            matTran.setSrcStorageCd(entity.getSrcStorageCd());
            matTran.setMemberCd(entity.getUpdId());
            matTran.setConfirmMemberCd(entity.getUpdId());
            matTran.setConfirmState(ConfirmState.OK);
            matTran.setEndYn("Y");
            matTran.setConfirmState(ConfirmState.OK);

            this.matTranService.saveOrUpdate(matTran);

            matTranItem.setMatTranId(matTran.getMatTranId());
            matTranItem.setItemCd(entity.getItemCd());

            ItemMaster itemMaster = this.itemMasterService.getById(entity.getItemCd());
            matTranItem.setItemName(itemMaster.getItemName());
            matTranItem.setItemTypeCd(itemMaster.getItemTypeCd());
            matTranItem.setSpec(itemMaster.getSpec());
            matTranItem.setPrice(itemMaster.getOutPrice());

            matTranItem.setLotNo(entity.getLotNo());
//            matTranItem.setQty(entity.getProdQty());
            matTranItem.setTestNo(entity.getTestNo());
            matTranItem.setExpiryDate(entity.getExpiryDate());
            matTranItem.setConfirmState(ConfirmState.OK);
            matTranItem.setMemo(entity.getMemo());

        } else {
            matTranItem = this.matTranItemService.getById(entity.getProdTranId());
//            matTranItem.setQty(entity.getProdQty());
        }

        // 임시데이터에서 tranItem 미매칭되어 오류나는 부분 회피하기 위하여 처리함
        if (matTranItem != null) {
            matTranItem.setQty(entity.getProdQty());

            this.matTranItemService.saveOrUpdate(matTranItem);

            entity.setProdTranYn("Y");
            entity.setProdTranId(matTranItem.getMatTranItemId());
            this.getTableService().saveOrUpdate(entity);
        }

        ProdResultView data;
        data = getViewService().getById(entity.getProdResultId());

        return new RestUtil<ProdResultView>().setData(data);

    }

    @RequestMapping(value = "/deleteData/{ids}",method = {RequestMethod.DELETE, RequestMethod.POST})
    @ResponseBody
    public RestResponse<Object> deleteData(@PathVariable String[] ids){
        List<String> idList = Arrays.asList(ids);

        String msg = getTableService().deleteData(idList);

        return new RestUtil<>().setMessage(msg);
    }

    @RequestMapping(value = "/printProdLabel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Resource> printProdLabel(@RequestBody ProdLabelItem[] labelItems) throws Exception {

        List<LabelPrint> labelPrintList = new ArrayList<>();

        for (ProdLabelItem labelItem : labelItems ) {

            ItemMasterView itemMaster = this.itemMasterViewService.getById(labelItem.getItemCd());

            if (itemMaster.getItemCondition() == null) {
                itemMaster.setItemConditionName("실온");
            }
            String itemTypeCd = itemMaster.getItemTypeCd();
            for (int i = 0; i < labelItem.getPrintCnt(); i++) {
                ItemTestNoView itemTestNo = this.itemTestNoViewService.getById(labelItem.getItemTestNo());
                LabelPrint labelPrint = new LabelPrint(
                        "(" + itemMaster.getItemCd() + ")",
                        itemMaster.getItemName(),
                        itemMaster.getItemConditionName(),
                        labelItem.getItemTestNo(),
                        labelItem.getProdNo(),
                        labelItem.getExpiryDate(),
                        new DecimalFormat("#,##0").format(labelItem.getProdQty()) + "kg",
                        itemTestNo.getPassStateName()
                );
                labelPrint.setLabelTitle(ItemTypeCd.getLableTitle(itemTypeCd));
                labelPrintList.add(labelPrint);
            }
        }

        try {
            InputStream reportStream = getClass().getResourceAsStream("/report/mat_prod_label_v3.jrxml");
            byte[] pdfContent = JasperUtil.getPdfBinary(reportStream, labelPrintList);

            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("prod_label_list", pdfContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("리포트에서 에러발생");
        }

    }

}
