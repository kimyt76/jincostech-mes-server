package com.daehanins.mes.biz.work.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.common.code.*;
import com.daehanins.mes.biz.common.vo.LabelPrint;
import com.daehanins.mes.biz.mat.entity.MatTran;
import com.daehanins.mes.biz.mat.entity.MatTranItem;
import com.daehanins.mes.biz.mat.entity.MatTranView;
import com.daehanins.mes.biz.pub.entity.ItemMaster;
import com.daehanins.mes.biz.pub.entity.ItemMasterView;
import com.daehanins.mes.biz.pub.service.IItemMasterService;
import com.daehanins.mes.biz.qt.entity.ItemTestNoView;
import com.daehanins.mes.biz.qt.service.IItemTestNoViewService;
import com.daehanins.mes.biz.work.entity.*;
import com.daehanins.mes.biz.work.service.IWorkOrderItemService;
import com.daehanins.mes.biz.work.service.IWorkOrderItemViewService;
import com.daehanins.mes.biz.work.vo.*;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import com.daehanins.mes.common.utils.JasperUtil;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.utils.SearchUtil;
import com.daehanins.mes.common.vo.PageVo;
import com.daehanins.mes.common.vo.RestResponse;
import org.apache.poi.ss.usermodel.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
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
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 작업지시상세WorkOrderItem Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-28
 */
@RestController
@RequestMapping("/work/work-order-item")
public class WorkOrderItemController extends BaseController<WorkOrderItem, WorkOrderItemView, String> {

    @Autowired
    private IWorkOrderItemService workOrderItemService;

    @Autowired
    private IWorkOrderItemViewService workOrderItemViewService;

    @Override
    public IWorkOrderItemService getTableService() {
        return this.workOrderItemService;
    }

    @Override
    public IWorkOrderItemViewService getViewService() {
    return this.workOrderItemViewService;
    }

    private static final DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /** 작업지시 페이징 조회 (칭량, 제조, 코팅, 충전, 포장) **/
    @RequestMapping(value = "/getItemViewPage",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<Page<WorkOrderItemView>> getItemViewPage(@ModelAttribute PageVo param, @RequestParam (name="condition", required=false ) String conditionJson){
        Page<WorkOrderItemView> page = new Page<>(param.getCurrentPage(), param.getPageSize());
        QueryWrapper<WorkOrderItemView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);

        queryWrapper.orderByDesc(StringUtils.camelToUnderline("orderDate"));
        queryWrapper.orderByAsc(StringUtils.camelToUnderline("customerName"));
        queryWrapper.orderByAsc(StringUtils.camelToUnderline("itemCd"));
        queryWrapper.orderByDesc(StringUtils.camelToUnderline("prodNo"));

        Page<WorkOrderItemView> data = getViewService().page(page, queryWrapper );
        return new RestUtil<Page<WorkOrderItemView>>().setData(data);

    }


    @RequestMapping(value = "/getExcel",method = RequestMethod.GET)
    public ResponseEntity<Resource> getExcel(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<WorkOrderItemView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        queryWrapper.orderByDesc(StringUtils.camelToUnderline("orderDate"));
        queryWrapper.orderByAsc(StringUtils.camelToUnderline("customerName"));
        queryWrapper.orderByAsc(StringUtils.camelToUnderline("itemCd"));
        queryWrapper.orderByDesc(StringUtils.camelToUnderline("prodNo"));
        Map<String, Object> paramMap = SearchUtil.parseParam(conditionJson);

        List<WorkOrderItemView> workOrderItemViewList = getViewService().list(queryWrapper);
        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/work_order_item_view_list.xlsx");
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
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 11);
            for (WorkOrderItemView item : workOrderItemViewList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(lineNo);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(item.getAreaName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(sdf.format(item.getOrderDate()));
                String prodNo = item.getProdNo().replaceAll("!!", " ");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(prodNo);
                String lotNo1 = item.getLotNo().replaceAll("!!", " ");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(lotNo1);
                String lotNo2 = item.getLotNo2().replaceAll("!!", " ");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(lotNo2);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(item.getItemCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(item.getItemName());
                Double orderQty = (item.getOrderQty() != null)? item.getOrderQty().doubleValue() : 0;
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue(orderQty);
                String status = WorkOrderItemStatus.getStatusName(item.getWorkOrderItemStatus());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue(status);
                String batchStatus = WorkOrderItemStatus.getStatusName(item.getBatchStatus());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 10, cellStyleList.get(10)).setCellValue(batchStatus);
                rowNo++;
                lineNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("work_order_item_view_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }




    /** 코팅, 충전, 포장 작업지시 상세 조회 **/
    @RequestMapping(value = "/getWorkOrderItemWithRecord/{workOrderItemId}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<WorkOrderItemWithRecord> getWorkOrderItemWithRecord (@PathVariable String workOrderItemId) {

        WorkOrderItemWithRecord resultData = this.getTableService().getWorkOrderItemWithRecord(workOrderItemId);
        return new RestUtil<WorkOrderItemWithRecord>().setData(resultData);
    }

    /** 작업지시 시작처리 **/
    @RequestMapping(value = "/startWorkOrderItem",method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<Object> startWorkOrderItem(@RequestBody WorkOrderItem entity) throws Exception{
        boolean result = this.getTableService().startWorkOrderItem(entity);
        return new RestUtil<>().setMessage((result)? "ok" : "fail" );
    }

    /** 작업지시 종료처리 **/
    @RequestMapping(value = "/finishWorkOrderItem", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<Object> finishWorkOrderItem(@RequestBody WorkOrderItem entity) throws Exception {

        boolean result = this.getTableService().finishWorkOrderItem(entity);
        return new RestUtil<>().setMessage((result)? "ok" : "fail" );
    }

    /** 작업지시 종료 후 수정처리 **/
    @RequestMapping(value = "/editWorkOrderItem", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<Object> editWorkOrderItem(@RequestBody WorkOrderItem entity) throws Exception {

        boolean result = this.getTableService().editWorkOrderItem(entity);
        return new RestUtil<>().setMessage((result)? "ok" : "fail" );
    }

    /** 작업지시 특이사항 업데이트 **/
    @RequestMapping(value = "/updateWorkOrderItemNote",method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<Object> updateWorkOrderItemNote(@RequestBody WorkOrderItem entity){
        //id, time, "작업지시 특이사항 업데이트"
        this.getTableService().update(new UpdateWrapper<WorkOrderItem>()
                .eq("work_order_item_id", entity.getWorkOrderItemId())
                .set("note", entity.getNote()));
        return new RestUtil<>().setMessage("ok");
    }

    /** 작업지시 메모 업데이트 **/
    @RequestMapping(value = "/updateWorkOrderItemMemo",method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<Object> updateWorkOrderItemMemo(@RequestBody WorkOrderItem entity){
        this.getTableService().update(new UpdateWrapper<WorkOrderItem>()
                .eq("work_order_item_id", entity.getWorkOrderItemId())
                .set("memo", entity.getMemo()));
        return new RestUtil<>().setMessage("ok");
    }

    /** 지시 및 기록서 엑셀다운로드. **/
    @RequestMapping(value = "/getOrderExcel/{workOrderItemId}", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Resource> getOrderExcel (@PathVariable String workOrderItemId) throws Exception {
        return this.getTableService().getOrderExcel(workOrderItemId);
    }

    /** 생산결과물 바코드 PDF 다운로드. **/
    @RequestMapping(value = "/getProdLabel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Resource> getProdLabel(@RequestBody ProdLabelItem[] labelItems) throws Exception {
        return this.getTableService().getProdLabel(labelItems);
    }

    @RequestMapping(value = "/getItemQtyList/{ids}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<ReqItemQty>> getItemQtyList (@PathVariable String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<ReqItemQty> resultData = this.getTableService().getItemQtyList(idList);
        return new RestUtil<List<ReqItemQty>>().setData(resultData);
    }

    /** 작업지시 메모 업데이트 **/
    @RequestMapping(value = "/updateReqMatOrderId",method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<Object> updateReqMatOrderId(@RequestBody ReqMatOrderVo reqMatOrderVo){
        this.getTableService().update(new UpdateWrapper<WorkOrderItem>()
                .set("req_mat_order_id", reqMatOrderVo.getMatOrderId())
                .in("work_order_item_id", reqMatOrderVo.getWorkOrderItemIds()));
        return new RestUtil<>().setMessage("ok");
    }

    /** 생산실적, 생산수율 **/
    @RequestMapping(value = "/getProdPerformance/{targetYear}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<Map<String, Object>>> getProdPerformance(@PathVariable String targetYear){
        PeriodVo periodVo = new PeriodVo();
        periodVo.setStartDate(targetYear + "-01-01");
        periodVo.setEndDate(targetYear + "-12-31");
        List<Map<String, Object>> result = this.workOrderItemService.getProdPerformance(periodVo);
        return new RestUtil<List<Map<String, Object>>>().setData(result);
    }

    /** 업체별 생산실적 **/
    @RequestMapping(value = "/getProdPerformanceByCustomer/{targetYear}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<Map<String, Object>>> getProdPerformanceByCustomer(@PathVariable String targetYear){
        PeriodVo periodVo = new PeriodVo();
        periodVo.setStartDate(targetYear + "-01-01");
        periodVo.setEndDate(targetYear + "-12-31");
        List<Map<String, Object>> result = this.workOrderItemService.getProdPerformanceByCustomer(periodVo);
        return new RestUtil<List<Map<String, Object>>>().setData(result);
    }

}

