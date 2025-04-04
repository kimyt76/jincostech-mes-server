package com.daehanins.mes.biz.mat.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.adm.entity.CommonCode;
import com.daehanins.mes.biz.adm.service.ICommonCodeService;
import com.daehanins.mes.biz.common.code.OrderState;
import com.daehanins.mes.biz.common.vo.PoSheetMailParam;
import com.daehanins.mes.biz.common.vo.PoSheetMailVo;
import com.daehanins.mes.biz.mat.entity.*;
import com.daehanins.mes.biz.mat.service.IMatOrderItemService;
import com.daehanins.mes.biz.mat.service.IMatOrderItemViewService;
import com.daehanins.mes.biz.mat.service.IMatOrderService;
import com.daehanins.mes.biz.mat.service.IMatOrderViewService;
import com.daehanins.mes.biz.mat.vo.*;
import com.daehanins.mes.biz.pub.entity.Customer;
import com.daehanins.mes.biz.pub.entity.ItemMasterView;
import com.daehanins.mes.biz.pub.entity.Storage;
import com.daehanins.mes.biz.pub.service.ICustomerService;
import com.daehanins.mes.biz.pub.service.IStorageService;
import com.daehanins.mes.biz.work.entity.WorkOrderItem;
import com.daehanins.mes.biz.work.service.IWorkOrderItemService;
import com.daehanins.mes.common.exception.BizException;
import com.daehanins.mes.common.mail.MailManager;
import com.daehanins.mes.common.mail.MessageVariable;
import com.daehanins.mes.common.utils.*;
import com.daehanins.mes.common.vo.RestResponse;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 자재지시(요청)MatOrder Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-22
 */
@RestController
@RequestMapping("/mat/mat-order")
public class MatOrderController extends BaseController<MatOrder, MatOrderView, String> {

    @Autowired
    private IMatOrderService matOrderService;

    @Autowired
    private IMatOrderItemService matOrderItemService;

    @Autowired
    private IMatOrderViewService matOrderViewService;

    @Autowired
    private IMatOrderItemViewService matOrderItemViewService;

    @Autowired
    private IWorkOrderItemService workOrderItemService;

    @Autowired
    private IStorageService storageService;

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private ICommonCodeService commonCodeService;

    @Override
    public IMatOrderService getTableService() {
        return this.matOrderService;
    }

    @Override
    public IMatOrderViewService getViewService() {
    return this.matOrderViewService;
    }

    @Autowired
    MailManager mailManager;

    @RequestMapping(value = "/getItem/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatOrderItemInfoVo> getItem(@PathVariable String id){
        MatOrderItemInfoVo matOrderItem = new MatOrderItemInfoVo();

        List<MatOrderItemInfoVo> matOrderItemList = this.matOrderItemService.getMatOrderItemList(id);
        matOrderItem.setMatOrderItemList(matOrderItemList);

        return new RestUtil<MatOrderItemInfoVo>().setData(matOrderItem);
    }

    @RequestMapping(value = "/getWithItems/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatOrderReadWithItems> getWithItems(@PathVariable String id){
        MatOrderReadWithItems poItems = new MatOrderReadWithItems();
        MatOrderView matOrder = getViewService().getById(id);

        QueryWrapper<MatOrderItemView> queryWrapper = new QueryWrapper<MatOrderItemView>().eq(StringUtils.camelToUnderline("matOrderId"), id);
        List<MatOrderItemView> matOrderItems = this.matOrderItemViewService.list(queryWrapper);
        poItems.setMatOrder(matOrder);
        poItems.setMatOrderItems(matOrderItems);
        return new RestUtil<MatOrderReadWithItems>().setData(poItems);
    }

    @RequestMapping(value = "/getWithItemsTranSum/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatOrderReadWithItemsTranSum> getWithItemsTranSum(@PathVariable String id){
        MatOrderReadWithItemsTranSum poItems = new MatOrderReadWithItemsTranSum();
        MatOrderView matOrder = getViewService().getById(id);

        QueryWrapper<MatOrderItemView> queryWrapper = new QueryWrapper<MatOrderItemView>().eq(StringUtils.camelToUnderline("matOrderId"), id);
        List<MatOrderTranItemSum> matOrderItems = this.matOrderItemService.getMatOrderTranItemSum(id);
        poItems.setMatOrder(matOrder);
        poItems.setMatOrderItems(matOrderItems);
        return new RestUtil<MatOrderReadWithItemsTranSum>().setData(poItems);
    }

    @RequestMapping(value = "/saveWithItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<MatOrderSaveWithItems> saveWithItems(@RequestBody MatOrderSaveWithItems requestParam){

        MatOrder po = requestParam.getMatOrder();

        // 로케이션(창고)에서 구역(공장) 구함
        Storage storage = this.storageService.getOne(new QueryWrapper<Storage>().eq("storage_cd", po.getSrcStorageCd()));
        po.setAreaCd(storage.getAreaCd());

        List<MatOrderItem> poItems = requestParam.getMatOrderItems();
        List<MatOrderItem> poDeleteItems = requestParam.getMatOrderDeleteItems();

        MatOrderSaveWithItems data = getTableService().saveWithItems(po, poItems, poDeleteItems);

        return new RestUtil<MatOrderSaveWithItems>().setData(data);
    }

    @RequestMapping(value = "/deleteWithItems/{ids}",method = {RequestMethod.DELETE, RequestMethod.POST})
    @ResponseBody
    public RestResponse<Object> deleteWithItems(@PathVariable String[] ids){
        List<String> idList = Arrays.asList(ids);
        String msg = getTableService().deleteWithItems(idList);
        workOrderItemService.update(new UpdateWrapper<WorkOrderItem>().set("req_mat_order_id", null)
                        .in("req_mat_order_id", idList));
        return new RestUtil<>().setMessage(msg);
    }

    @RequestMapping(value = "/end", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatOrder> orderEnd(@RequestParam String id, @RequestParam String endYn){
        MatOrder entity = getTableService().getById(id);
        if (endYn.equals("Y")) {
            entity.setOrderState(MatOrderStatus.END);
        } else {
            entity.setOrderState(MatOrderStatus.ING);
        }
        entity.setEndYn(endYn);
        getTableService().updateById(entity);
        return new RestUtil<MatOrder>().setData(entity);
    }

    /**
     * endItem
     * @id
     * @itemEndYn
     * @return
     */
    @RequestMapping(value ="endItem", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatOrder> endItem (@RequestParam String id, @RequestParam String itemEndYn) throws Exception {
        MatOrder entity = getTableService().getById(id);
        /**
         *  품목별
         */
        if ( !matOrderItemService.updateOrderItemEndYn(id, itemEndYn) ) throw new Exception("종결 업데이트 중 오류가 발생했습니다!");

        HashMap<String, Object> endInfo =  matOrderItemService.getItemEndYnInfo(id);

        if ( endInfo.get("endYn").equals("Y") ) {
            itemEndYn = "Y";
        }else{
            itemEndYn = "N";
        }

        id = endInfo.get("matOrderId").toString();
        orderEnd(id, itemEndYn);

        return new RestUtil<MatOrder>().setData(entity);
    }

    @RequestMapping(value = "/printPo/{ids}", method = RequestMethod.GET)
    public ResponseEntity<Resource> printPo(@PathVariable String[] ids) throws  Exception {

        final int PO_ITEM_MAX_ROWS = 11;    // 발주품목 최대 12개 출력

        List<PurchaseOrderSheet> poSheetList = new ArrayList<>();

        List<CommonCode> commonCodes = commonCodeService.list(
                new QueryWrapper<CommonCode>().eq("code_type", "AUTH_LINE")
                        .orderByAsc("display_order")
        );

        String line1 = commonCodes.get(0).getCodeName();
        String line2 = commonCodes.get(1).getCodeName();
        String line3 = commonCodes.get(2).getCodeName();
        String line4 = commonCodes.get(3).getCodeName();

        for (int i = 0; i < ids.length; i++) {
            String matOrderId = ids[i];
            PurchaseOrderSheet poSheet = new PurchaseOrderSheet();
            MatOrderView matOrder = getViewService().getById(matOrderId);

            poSheet.setMemberName(matOrder.getMemberName());
            poSheet.setOrderDate(matOrder.getOrderDate().toString());
            poSheet.setDeliveryDate(matOrder.getDeliveryDate().toString());
            poSheet.setCustomerManager(matOrder.getCustomerManager());

            Customer customer = this.customerService.getById(matOrder.getCustomerCd());
            poSheet.setCustomerName(customer.getCustomerName());
            poSheet.setEmail(customer.getEmail());
            poSheet.setTel(customer.getTel());
            poSheet.setFax(customer.getFax());
            poSheet.setAddress(customer.getAddress());
            poSheet.setLine1(line1);
            poSheet.setLine2(line2);
            poSheet.setLine3(line3);
            poSheet.setLine4(line4);

            DecimalFormat df = new DecimalFormat("#,##0");
            BigDecimal amount = matOrder.getSumSupplyAmt().add(matOrder.getSumVat());
            poSheet.setHanAmount(BizUtil.getHangulNumber(amount.toString()) + "원 정");
            poSheet.setNumAmount("(\\ " + df.format(amount.doubleValue()) + " )");

            poSheet.setTotOrderQty(matOrder.getSumOrderQty());
            poSheet.setTotSupplyAmt(matOrder.getSumSupplyAmt());
            poSheet.setTotVat(matOrder.getSumVat());
            poSheet.setTotAmt(amount);

            // subItems 만들기
            List<PurchaseOrderItem> poItemList = new ArrayList<>();

            QueryWrapper<MatOrderItemView> queryWrapper = new QueryWrapper<MatOrderItemView>().eq(StringUtils.camelToUnderline("matOrderId"), matOrderId);
            List<MatOrderItemView> matOrderItems = this.matOrderItemViewService.list(queryWrapper);
            int rowCount = 0;
            for (MatOrderItemView moItem: matOrderItems) {
                rowCount++;

                PurchaseOrderItem poItem = new PurchaseOrderItem();

                poItem.setItemCd(moItem.getItemCd());
                poItem.setItemName(moItem.getItemName());
                poItem.setSpec(moItem.getSpec());
                poItem.setOrderQty(moItem.getOrderQty());
                poItem.setPrice(moItem.getPrice());
                poItem.setSupplyAmt(moItem.getSupplyAmt());
                poItem.setVat(moItem.getVat());
                poItem.setMemo(moItem.getMemo());
                poItemList.add(poItem);
            }
            while(rowCount < PO_ITEM_MAX_ROWS) {
                rowCount++;
                PurchaseOrderItem poItem = new PurchaseOrderItem();
                poItemList.add(poItem);
            }
            poSheet.setOrderItems(poItemList);
            poSheetList.add(poSheet);

            // 프린트 완료 표시
            MatOrder entity = getTableService().getById(matOrderId);
            entity.setPrintYn("Y");
            getTableService().updateById(entity);
        }

        try {
            Map<String, Object> parameters = new HashMap<>();
            // 로고이미지
            InputStream logoStream = getClass().getResourceAsStream("/static/images/logo1.png");
            parameters.put("logo", logoStream);

            // subReport 셋팅
            InputStream inputStream = getClass().getResourceAsStream("/report/purchase_order_sub.jrxml");
            JasperReport subReport = JasperCompileManager.compileReport(inputStream);
            parameters.put("subReport", subReport);

            InputStream reportStream = getClass().getResourceAsStream("/report/purchase_order_sheet_v2.jrxml");
            byte[] pdfContent = JasperUtil.getPdfBinary(reportStream, parameters, poSheetList);
            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("poSheet_list", pdfContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("리포트 생성중 에러발생!");
        }
    }

    @RequestMapping(value = "/getExcel",method = RequestMethod.GET)
    public ResponseEntity<Resource> getExcel(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<MatOrderView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        Map<String, Object> paramMap = SearchUtil.parseParam(conditionJson);
        queryWrapper.orderByDesc("order_no");
        List<MatOrderView> matOrderList = getViewService().list(queryWrapper);
        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/purchase_order_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("발주서조회");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            String excelTitle = "회사명: (주)진코스텍 / " + paramMap.get("startDate") + " ~ " + paramMap.get("endDate");
            ExcelPoiUtil.getCell(sheet, 0, 0).setCellValue(excelTitle);
            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 10);
            for (MatOrderView matOrder : matOrderList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
//                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(lineNo++);   // 줄번호
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(matOrder.getOrderNo());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(matOrder.getDeliveryDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(matOrder.getCustomerName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(matOrder.getItemName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(matOrder.getItemTypeName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(matOrder.getSumOrderQty().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(matOrder.getMemberName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(matOrder.getEndYn().equals("Y") ? "종결" : "취소");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue("조회");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue(matOrder.getMailYn().equals("Y") ? "발송": "미발송");
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("purchase_order_list", excelContent.length, "xlsx");

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    @RequestMapping(value = "/orderMail", method = RequestMethod.POST)
    @ResponseBody

    public RestResponse<MatOrder> orderMail(@RequestBody PoSheetMailParam sheetMailParam) {

        final int PO_ITEM_MAX_ROWS = 11;    // 발주품목 최대 12개 출력

        List<PurchaseOrderSheet> poSheetList = new ArrayList<>();

        List<CommonCode> commonCodes = commonCodeService.list(
                new QueryWrapper<CommonCode>().eq("code_type", "AUTH_LINE")
                        .orderByAsc("display_order")
        );

        String line1 = commonCodes.get(0).getCodeName();
        String line2 = commonCodes.get(1).getCodeName();
        String line3 = commonCodes.get(2).getCodeName();
        String line4 = commonCodes.get(3).getCodeName();

        String matOrderId = sheetMailParam.getMatOrderId();

        PurchaseOrderSheet poSheet = new PurchaseOrderSheet();
        MatOrderView matOrder = getViewService().getById(matOrderId);

        poSheet.setMemberName(matOrder.getMemberName());
        poSheet.setOrderDate(matOrder.getOrderDate().toString());
        poSheet.setDeliveryDate(matOrder.getDeliveryDate().toString());
//        poSheet.setCustomerManager(matOrder.getCustomerManager());
        poSheet.setCustomerManager(sheetMailParam.getReceiver());

        Customer customer = this.customerService.getById(matOrder.getCustomerCd());

        poSheet.setCustomerName(customer.getCustomerName());
        poSheet.setEmail(sheetMailParam.getReceiverEmail());
//        poSheet.setEmail(customer.getEmail());
        poSheet.setTel(customer.getTel());
        poSheet.setFax(customer.getFax());
        poSheet.setAddress(customer.getAddress());
        poSheet.setLine1(line1);
        poSheet.setLine2(line2);
        poSheet.setLine3(line3);
        poSheet.setLine4(line4);

        DecimalFormat df = new DecimalFormat("#,##0");
        BigDecimal amount = matOrder.getSumSupplyAmt().add(matOrder.getSumVat());
        poSheet.setHanAmount(BizUtil.getHangulNumber(amount.toString()) + "원 정");
        poSheet.setNumAmount("(\\ " + df.format(amount.doubleValue()) + " )");

        poSheet.setTotOrderQty(matOrder.getSumOrderQty());
        poSheet.setTotSupplyAmt(matOrder.getSumSupplyAmt());
        poSheet.setTotVat(matOrder.getSumVat());
        poSheet.setTotAmt(amount);

        // subItems 만들기
        List<PurchaseOrderItem> poItemList = new ArrayList<>();

        QueryWrapper<MatOrderItemView> queryWrapper = new QueryWrapper<MatOrderItemView>().eq(StringUtils.camelToUnderline("matOrderId"), matOrderId);
        List<MatOrderItemView> matOrderItems = this.matOrderItemViewService.list(queryWrapper);
        int rowCount = 0;
        for (MatOrderItemView moItem: matOrderItems) {
            rowCount++;

            PurchaseOrderItem poItem = new PurchaseOrderItem();

            poItem.setItemCd(moItem.getItemCd());
            poItem.setItemName(moItem.getItemName());
            poItem.setSpec(moItem.getSpec());
            poItem.setOrderQty(moItem.getOrderQty());
            poItem.setPrice(moItem.getPrice());
            poItem.setSupplyAmt(moItem.getSupplyAmt());
            poItem.setVat(moItem.getVat());
            poItem.setMemo(moItem.getMemo());
            poItemList.add(poItem);
        }
        while(rowCount < PO_ITEM_MAX_ROWS) {
            rowCount++;
            PurchaseOrderItem poItem = new PurchaseOrderItem();
            poItemList.add(poItem);
        }

        poSheet.setOrderItems(poItemList);
        poSheetList.add(poSheet);

        // 프린트 완료 표시
        MatOrder entity = getTableService().getById(matOrderId);
        entity.setMailYn("Y");
        getTableService().updateById(entity);

        try {
            Map<String, Object> parameters = new HashMap<>();
            // 로고이미지
            InputStream logoStream = getClass().getResourceAsStream("/static/images/logo1.png");
            parameters.put("logo", logoStream);

            // subReport 셋팅
            InputStream subStream = getClass().getResourceAsStream("/report/purchase_order_sub.jrxml");
            JasperReport subReport = JasperCompileManager.compileReport(subStream);
            parameters.put("subReport", subReport);

            InputStream reportStream = getClass().getResourceAsStream("/report/purchase_order_sheet_v2.jrxml");
            byte[] pdfContent = JasperUtil.getPdfBinary(reportStream, parameters, poSheetList);
            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("poSheet_list", pdfContent.length);

            ByteArrayDataSource bds = new ByteArrayDataSource(pdfContent, "application/pdf");

            Map<String, DataSource> files = new HashMap<>();
            files.put("발주서("+ matOrder.getOrderNo() + ").pdf", bds);

            PoSheetMailVo mailVo = new PoSheetMailVo();
            mailVo.setCustomerName(matOrder.getCustomerName());
            mailVo.setOrderNo(matOrder.getOrderNo());
            mailVo.setOrderDate(matOrder.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            mailVo.setSenderEmail(sheetMailParam.getSenderEmail());
            mailVo.setMemo(sheetMailParam.getMemo());

            String mailSubject = "(주)진코스텍으로부터 발주서(" + matOrder.getOrderNo() + ")가 도착했습니다.";

           mailManager.send(sheetMailParam.getReceiverEmail(), sheetMailParam.getReceiverCc(), mailSubject, "poSheetMail.ftl",  files, MessageVariable.from("vo", mailVo));

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BizException("발주서 메일 발송중 에러발생!" + ex.getMessage());
        }

        return new RestUtil<MatOrder>().setData(entity);
    }


    @RequestMapping(value = "/getExcelReq",method = RequestMethod.GET)
    public ResponseEntity<Resource> getExcelReq(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<MatOrderView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        Map<String, Object> paramMap = SearchUtil.parseParam(conditionJson);
        queryWrapper.orderByDesc("order_no");
        List<MatOrderView> matOrderList = getViewService().list(queryWrapper);
        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/move_req_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("Sheet1");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            String excelTitle = "회사명: (주)진코스텍 / " + paramMap.get("startDate") + " ~ " + paramMap.get("endDate");
            ExcelPoiUtil.getCell(sheet, 0, 0).setCellValue(excelTitle);
            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 8);
            for (MatOrderView matOrder : matOrderList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
//                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(lineNo++);   // 줄번호
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(matOrder.getOrderNo());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(matOrder.getSrcStorageName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(matOrder.getDestStorageName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(matOrder.getItemName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(matOrder.getMemo());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(matOrder.getSumOrderQty().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(matOrder.getMemberName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(OrderState.getOrderStateName(matOrder.getOrderState()));
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("move_req_list", excelContent.length, "xlsx");

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }
}

