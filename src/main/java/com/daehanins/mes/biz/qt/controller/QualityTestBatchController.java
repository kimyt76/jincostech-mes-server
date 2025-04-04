package com.daehanins.mes.biz.qt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.mat.entity.MatTranItemView;
import com.daehanins.mes.biz.pub.entity.ItemMaster;
import com.daehanins.mes.biz.pub.entity.ItemMasterView;
import com.daehanins.mes.biz.pub.service.IItemMasterService;
import com.daehanins.mes.biz.pub.service.IItemMasterViewService;
import com.daehanins.mes.biz.qt.entity.*;
import com.daehanins.mes.biz.qt.service.*;
import com.daehanins.mes.biz.qt.vo.QualityTestReadWithItems;
import com.daehanins.mes.biz.qt.vo.QualityTestReportItem;
import com.daehanins.mes.biz.qt.vo.QualityTestSaveWithItems;
import com.daehanins.mes.biz.qt.vo.QualityTestReport;
import com.daehanins.mes.biz.security.entity.User;
import com.daehanins.mes.common.utils.*;
import com.daehanins.mes.common.vo.RestResponse;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.apache.poi.ss.usermodel.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 품질검사QualityTestBatch Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-03
 */
@RestController
@RequestMapping("/qt/quality-test-batch")
public class QualityTestBatchController extends BaseController<QualityTest, QualityTestBatchView, String> {

    @Autowired
    private IQualityTestService qualityTestService;

    @Autowired
    private IQualityTestViewService qualityTestViewService;

    @Autowired
    private IQualityTestMethodService qualityTestMethodService;

    @Autowired
    private IQualityTestMethodViewService qualityTestMethodViewService;

    @Autowired
    private IQualityTestBatchViewService qualityTestBatchViewService;

    @Autowired
    private IItemMasterViewService itemMasterViewService;

    @Autowired
    private ITestTypeMethodService testTypeMethodService;

    @Override
    public IQualityTestService getTableService() {
        return this.qualityTestService;
    }

    @Override
    public IQualityTestBatchViewService getViewService() {
        return this.qualityTestBatchViewService;
    }


    @RequestMapping(value = "/updateItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<List<QualityTestBatchView>> updateItems(@RequestBody List<QualityTestBatchView> requestParam){

        for (QualityTestBatchView row : requestParam ) {

            String[] ids = row.getQualityTestMethodIdJoin().split("\\^");
            String[] values = row.getTestResultJoin().split("\\^");

            for(int i = 0; i < row.getColCount(); i++) {
                if (values[i] == null || ids[i] == null)  continue;
                this.qualityTestMethodService.update(new UpdateWrapper<QualityTestMethod>()
                        .eq("quality_test_method_id", ids[i])
                        .set("test_result", values[i])
                );
            }
            this.qualityTestService.update(new UpdateWrapper<QualityTest>()
                    .eq("quality_test_id", row.getQualityTestId())
                    .set("test_state", row.getTestState())
                    .set("pass_state", row.getPassState())
            );
        }

        List<QualityTestBatchView> data = requestParam;

        return new RestUtil<List<QualityTestBatchView>>().setData(data);
    }


    @RequestMapping(value = "/getPdf", method = RequestMethod.GET)
    public ResponseEntity<Resource> getPdf(@RequestParam(name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<QualityTestView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<QualityTestView> QualityTestViewList = this.qualityTestViewService.list(queryWrapper);

        try {
            InputStream reportStream = getClass().getResourceAsStream("/report/quality_test_list.jrxml");
            byte[] pdfContent = JasperUtil.getPdfBinary(reportStream, QualityTestViewList);
            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("quality_test_list", pdfContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("리포트 생성중 에러발생!");
        }
    }

    @RequestMapping(value = "/getExcel",method = RequestMethod.GET)
    public ResponseEntity<Resource> getExcel(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<QualityTestBatchView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        Map<String, Object> paramMap = SearchUtil.parseParam(conditionJson);
        // itemCd , testStartDate, testEndDate

        String itemCd = paramMap.get("itemCd").toString();
        ItemMasterView itemMasterView = this.itemMasterViewService.getById(itemCd);
        List<TestTypeMethod> testTypeMethods = this.testTypeMethodService.list(new QueryWrapper<TestTypeMethod>().eq("item_cd", itemCd));
        List<QualityTestBatchView> QualityTestBatchViewList = this.qualityTestBatchViewService.list(queryWrapper);

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/quality_test_batch_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("품질검사리스트");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 7;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            // 조회조건 : 기간
            String dateCond = "기간 : " + paramMap.get("testStartDate") + " ~ " + paramMap.get("testEndDate");
            ExcelPoiUtil.getCell(sheet, 1, 0).setCellValue(dateCond);
            // 구분, 시험유형
            if (itemMasterView != null) {
                ExcelPoiUtil.getCell(sheet, 3, 0).setCellValue(itemMasterView.getItemTypeName());
                ExcelPoiUtil.getCell(sheet, 3, 1).setCellValue(itemMasterView.getItemName());
            }

            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 4);

            int typeNameRowNo = 6;
            int typeCount = testTypeMethods.size();
            for (int mi = 0; mi < typeCount; mi++) {
                // 시험항목 merge 컬럼 생성
                if (mi > 0) {
                    ExcelPoiUtil.getStyleCell(sheet, typeNameRowNo - 1, mi + 1, cellStyleList.get(1)).setCellValue("");
                }
                // 시험항목 세부명칭
                ExcelPoiUtil.getStyleCell(sheet, typeNameRowNo, mi + 1, cellStyleList.get(1)).setCellValue(testTypeMethods.get(mi).getTestItem());
            }
            // 시험항목 merge 컬럼 생성
            ExcelPoiUtil.getStyleCell(sheet, typeNameRowNo - 1, typeCount + 1, cellStyleList.get(1)).setCellValue("");
            ExcelPoiUtil.getStyleCell(sheet, typeNameRowNo, typeCount + 1, cellStyleList.get(1)).setCellValue("판정");

            // 시험항목 merge 처리
            sheet.addMergedRegion(new CellRangeAddress(typeNameRowNo - 1,typeNameRowNo - 1,1,typeCount + 1));

            for (QualityTestBatchView qualityTestBatchView : QualityTestBatchViewList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(qualityTestBatchView.getTestNo());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, typeCount + 1, cellStyleList.get(1)).setCellValue(qualityTestBatchView.getPassStateName());

                String testValues[] = qualityTestBatchView.getTestResultJoin().split("\\^");
                for (int ti = 0; ti < testTypeMethods.size(); ti++) {
                    String testValue = " ";
                    if (testValues != null && testValues.length > ti && testValues[ti] != null) {
                        testValue = testValues[ti];
                    }
                    testValue = testValue.replaceAll("&lt;", "<");
                    int cellNum = ti + 1;
                    ExcelPoiUtil.getStyleCell(sheet, rowNo, cellNum, cellStyleList.get(1)).setCellValue(testValue);
                }
                rowNo++;
            }
            rowNo++; // 공백1줄 추가

            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("quality_test_batch_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }
}
