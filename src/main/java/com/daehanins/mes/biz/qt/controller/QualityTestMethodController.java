package com.daehanins.mes.biz.qt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.qt.entity.QualityTestMethod;
import com.daehanins.mes.biz.qt.entity.QualityTestMethodView;
import com.daehanins.mes.biz.qt.service.IQualityTestMethodService;
import com.daehanins.mes.biz.qt.service.IQualityTestMethodViewService;
import com.daehanins.mes.biz.tag.entity.TagInfo;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.utils.SearchUtil;
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
import java.util.List;

/**
 * <p>
 * 품질검사상세QualityTestMethod Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-03
 */
@RestController
@RequestMapping("/qt/quality-test-method")
public class QualityTestMethodController extends BaseController<QualityTestMethod, QualityTestMethodView, String> {

    @Autowired
    private IQualityTestMethodService qualityTestMethodService;

    @Autowired
    private IQualityTestMethodViewService qualityTestMethodViewService;

    @Override
    public IQualityTestMethodService getTableService() {
        return this.qualityTestMethodService;
    }

    @Override
    public IQualityTestMethodViewService getViewService() {
    return this.qualityTestMethodViewService;
    }

    @RequestMapping(value = "/getViewAdvanced",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<QualityTestMethodView>> getViewAdvanced(@RequestParam(name="condition", required=false ) String conditionJson){
        QueryWrapper<QualityTestMethodView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        queryWrapper.orderByAsc("display_order", "test_no");
        List<QualityTestMethodView> list = getViewService().list(queryWrapper);
        return new RestUtil<List<QualityTestMethodView>>().setData(list);
    }

    @RequestMapping(value = "/getExcel",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> getExcel(@RequestParam(name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<QualityTestMethodView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        queryWrapper.orderByAsc("display_order", "test_no");
        List<QualityTestMethodView> data = getViewService().list(queryWrapper);

        try {
            // 엑셀양식 로드
            InputStream excelStream = getClass().getResourceAsStream("/excel/quality_test_method_view_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("Sheet1");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 9);
            for (QualityTestMethodView item : data){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(lineNo++);   // 줄번호
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(item.getTestNo());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(item.getTestItem());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(item.getTestMethod());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(item.getTestSpec());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(item.getTestResult());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(item.getTestDateString());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(item.getTestMemberName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue(item.getPassState());
                rowNo++;
            }

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("quality_test_method_view_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }
}

