package com.daehanins.mes.biz.mat.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.mat.entity.MatOrderItem;
import com.daehanins.mes.biz.mat.entity.MatOrderItemView;
import com.daehanins.mes.biz.mat.entity.MatOrderStateView;
import com.daehanins.mes.biz.mat.service.IMatOrderItemService;
import com.daehanins.mes.biz.mat.service.IMatOrderItemViewService;
import com.daehanins.mes.biz.mat.service.IMatOrderStateViewService;
import com.daehanins.mes.biz.qt.entity.QualityTestView;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import com.daehanins.mes.common.utils.JasperUtil;
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 자재지시(요청)품목MatOrderItem Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-22
 */
@RestController
@RequestMapping("/mat/mat-order-item")
public class MatOrderItemController extends BaseController<MatOrderItem, MatOrderItemView, String> {

    @Autowired
    private IMatOrderItemService matOrderItemService;

    @Autowired
    private IMatOrderItemViewService matOrderItemViewService;

    @Autowired
    private IMatOrderStateViewService matOrderStateViewService;

    @Override
    public IMatOrderItemService getTableService() {
        return this.matOrderItemService;
    }

    @Override
    public IMatOrderItemViewService getViewService() {
    return this.matOrderItemViewService;
    }

    @RequestMapping(value = "/getStatePage",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<Page<MatOrderStateView>> getStatePage(@ModelAttribute PageVo param, @RequestParam (name="condition", required=false ) String conditionJson){
        Page<MatOrderStateView> page = new Page<>(param.getCurrentPage(), param.getPageSize());

        QueryWrapper<MatOrderStateView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        if (!StringUtils.isBlank(param.getSortColumn())) {
            queryWrapper.orderBy(true, param.isOrderAsc(), StringUtils.camelToUnderline(param.getSortColumn()));
        }
        Page<MatOrderStateView> data = this.matOrderStateViewService.page(page, queryWrapper );
        return new RestUtil<Page<MatOrderStateView>>().setData(data);
    }

    @RequestMapping(value = "/getPdf", method = RequestMethod.GET)
    public ResponseEntity<Resource> getPdf(@RequestParam(name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<MatOrderStateView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<MatOrderStateView> matOrderStateViewList = this.matOrderStateViewService.list(queryWrapper);

        try {
            InputStream reportStream = getClass().getResourceAsStream("/report/mat_order_state_list.jrxml");
            byte[] pdfContent = JasperUtil.getPdfBinary(reportStream, matOrderStateViewList);
            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("mat_order_state_list", pdfContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("리포트 생성중 에러발생!");
        }
    }

    @RequestMapping(value = "/getExcel",method = RequestMethod.GET)
    public ResponseEntity<Resource> getExcel(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<MatOrderStateView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        Map<String, Object> paramMap = SearchUtil.parseParam(conditionJson);
        queryWrapper.orderByDesc("order_no");
        List<MatOrderStateView> matOrderStateList = this.matOrderStateViewService.list(queryWrapper);
        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/purchase_order_list2.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("발주현황조회");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            String excelTitle = "회사명: (주)진코스텍 / " + paramMap.get("startDate") + " ~ " + paramMap.get("endDate");
            ExcelPoiUtil.getCell(sheet, 0, 0).setCellValue(excelTitle);
            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 10);
            for (MatOrderStateView matOrderState : matOrderStateList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(matOrderState.getOrderNo());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(matOrderState.getDeliveryDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(matOrderState.getCustomerName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(matOrderState.getItemName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(matOrderState.getItemTypeName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(matOrderState.getOrderQty().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(matOrderState.getSumQty().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(matOrderState.getRegYn().equals("Y") ? "입고완료" : "미입고");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue(matOrderState.getEndYn().equals("Y") ? "종결": "진행중");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue(matOrderState.getMemberName());
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("purchase_order_list2", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }


}

