package com.daehanins.mes.biz.work.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.common.code.WorkOrderItemStatus;
import com.daehanins.mes.biz.common.code.WorkState;
import com.daehanins.mes.biz.work.entity.WorkOrderBatch;
import com.daehanins.mes.biz.work.entity.WorkOrderBatchStatusView;
import com.daehanins.mes.biz.work.entity.WorkOrderBatchView;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;
import com.daehanins.mes.biz.work.service.IWorkOrderBatchService;
import com.daehanins.mes.biz.work.service.IWorkOrderBatchStatusViewService;
import com.daehanins.mes.biz.work.service.IWorkOrderBatchViewService;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.utils.SearchUtil;
import com.daehanins.mes.common.vo.PageVo;
import com.daehanins.mes.common.vo.RestResponse;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * WorkOrderBatch Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2021-02-22
 */
@RestController
@RequestMapping("/work/work-order-batch")
public class WorkOrderBatchController extends BaseController<WorkOrderBatch, WorkOrderBatchView, String> {

    @Autowired
    private IWorkOrderBatchService workOrderBatchService;

    @Autowired
    private IWorkOrderBatchViewService workOrderBatchViewService;

    @Autowired
    private IWorkOrderBatchStatusViewService workOrderBatchStatusViewService;

    @Override
    public IWorkOrderBatchService getTableService() {
        return this.workOrderBatchService;
    }

    @Override
    public IWorkOrderBatchViewService getViewService() {
    return this.workOrderBatchViewService;
    }

    /** 작업지시 조회 (정렬순서로 인해 개별로 생성함) **/
    @RequestMapping(value = "/getPageAdvanced",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<Page<WorkOrderBatchView>> getPageAdvanced(@ModelAttribute PageVo param, @RequestParam (name="condition", required=false ) String conditionJson){
        Page<WorkOrderBatchView> page = new Page<>(param.getCurrentPage(), param.getPageSize());

        QueryWrapper<WorkOrderBatchView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        queryWrapper.orderByDesc("order_no", "batch_ser_no");
        Page<WorkOrderBatchView> data = this.getViewService().page(page, queryWrapper );
        return new RestUtil<Page<WorkOrderBatchView>>().setData(data);
    }

    /** 작업지시 조회 (정렬순서로 인해 개별로 생성함) **/
    @RequestMapping(value = "/getPageStatusAdvanced",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<Page<WorkOrderBatchStatusView>> getPageStatusAdvanced(@ModelAttribute PageVo param, @RequestParam (name="condition", required=false ) String conditionJson){
        Page<WorkOrderBatchStatusView> page = new Page<>(param.getCurrentPage(), param.getPageSize());

        QueryWrapper<WorkOrderBatchStatusView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        queryWrapper.orderByDesc("order_no", "batch_ser_no");
        Page<WorkOrderBatchStatusView> data = workOrderBatchStatusViewService.page(page, queryWrapper );
        return new RestUtil<Page<WorkOrderBatchStatusView>>().setData(data);
    }

    /** 작업지시 조회 (정렬순서로 인해 개별로 생성함) **/
    @RequestMapping(value = "/getStatusAdvancedExcel",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> getStatusAdvancedExcel(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception{

        Map<String, Object> paramMap = SearchUtil.parseParam(conditionJson);
        QueryWrapper<WorkOrderBatchStatusView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        queryWrapper.orderByDesc("order_no", "batch_ser_no");
        List<WorkOrderBatchStatusView> data = workOrderBatchStatusViewService.list(queryWrapper);

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/work_order_batch_status_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            Sheet sheet = workbook.getSheet("Sheet1");
            // 기본 변수 선언
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            String excelTitle = "회사명: (주)진코스텍 / " + paramMap.get("startDate") + " ~ " + paramMap.get("endDate");
            ExcelPoiUtil.getCell(sheet, 0, 0).setCellValue(excelTitle);

            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 12);
            for (WorkOrderBatchStatusView item : data){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(lineNo++);   // 줄번호
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(item.getOrderNo());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(item.getAreaName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(item.getItemCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(item.getItemName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(item.getProdNo());
                String lotNo = (item.getLotNo() != null)? item.getLotNo().replaceAll("!!", " ") : "/" ;
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(lotNo);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(WorkOrderItemStatus.getStatusName(item.getBatchStatus()));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue(item.getProdQty());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue(item.getCoatingQty());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 10, cellStyleList.get(10)).setCellValue(item.getChargingQty());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 11, cellStyleList.get(11)).setCellValue(item.getPackingQty());
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("work_order_batch_status_list", excelContent.length, "xlsx");

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

}

