package com.daehanins.mes.biz.qt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.qt.entity.*;
import com.daehanins.mes.biz.qt.service.*;
import com.daehanins.mes.biz.qt.vo.QualityTestReadWithItems;
import com.daehanins.mes.biz.qt.vo.QualityTestReportItem;
import com.daehanins.mes.biz.qt.vo.QualityTestSaveWithItems;
import com.daehanins.mes.biz.qt.vo.QualityTestReport;
import com.daehanins.mes.common.exception.BizException;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import com.daehanins.mes.common.utils.JasperUtil;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.utils.SearchUtil;
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
 * 품질검사QualityTest Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-03
 */
@RestController
@RequestMapping("/qt/quality-test")
public class QualityTestController extends BaseController<QualityTest, QualityTestView, String> {

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

    @Override
    public IQualityTestService getTableService() {
        return this.qualityTestService;
    }

    @Override
    public IQualityTestViewService getViewService() {
    return this.qualityTestViewService;
    }


    @RequestMapping(value = "/getWithItems/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<QualityTestReadWithItems> getWithItems(@PathVariable String id){
        QualityTestReadWithItems qualityTestItems = new QualityTestReadWithItems();
        QualityTestView qualityTest = getViewService().getById(id);

        QueryWrapper<QualityTestMethod> queryWrapper = new QueryWrapper<QualityTestMethod>().eq(StringUtils.camelToUnderline("qualityTestId"), id);
        queryWrapper.orderByAsc("display_order", "quality_test_method_id");
        List<QualityTestMethod> qualityTestMethods = this.qualityTestMethodService.list(queryWrapper);
        qualityTestItems.setQualityTest(qualityTest);
        qualityTestItems.setQualityTestMethods(qualityTestMethods);
        return new RestUtil<QualityTestReadWithItems>().setData(qualityTestItems);
    }

    @RequestMapping(value = "/saveWithItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<QualityTestSaveWithItems> saveWithItems(@RequestBody QualityTestSaveWithItems requestParam){

        QualityTest qualityTest = requestParam.getQualityTest();

        List<QualityTestMethod> qualityTestMethods = requestParam.getQualityTestMethods();
        List<QualityTestMethod> deletequalityTestMethods = requestParam.getDeleteQualityTestMethods();

        QualityTestSaveWithItems data = getTableService().saveWithItems(qualityTest, qualityTestMethods, deletequalityTestMethods);

        return new RestUtil<QualityTestSaveWithItems>().setData(data);
    }

    @RequestMapping(value = "/deleteWithItems/{ids}",method = {RequestMethod.DELETE, RequestMethod.POST})
    @ResponseBody
    public RestResponse<Object> deleteWithItems(@PathVariable String[] ids){
        List<String> idList = Arrays.asList(ids);
        String msg = getTableService().deleteWithItems(idList);
        return new RestUtil<>().setMessage(msg);
    }


    /**
     * 검체채취량 일단위 tran생성
     */
    @RequestMapping(value = "/makeSampleUse/{paramDate}", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<String> makeSampleUse(@PathVariable String paramDate){

        // pdate : yyyy/mm/dd
        LocalDate calcDate = LocalDate.parse(paramDate);

        boolean result = this.getTableService().makeSampleUse(calcDate);

        String message = result ? "OK":"Error";

        return new RestUtil<String>().setData(message);
    }

    @RequestMapping(value = "/printQt/{ids}", method = RequestMethod.GET)
    public ResponseEntity<Resource> printQualityTestReports(@PathVariable String[] ids) throws  Exception {
        try {
            byte[] pdfContent = qualityTestService.printQualityTestReports(ids[0]);
            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("qualityTestSheet_list", pdfContent.length);
            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BizException("성적서 생성 중 에러 발생");
        }
    }

    @RequestMapping(value = "/printQts/{ids}", method = RequestMethod.GET)
    public ResponseEntity<Resource> printQt(@PathVariable String[] ids) throws  Exception {

        final int QT_ITEM_MAX_ROWS = 16;    // 검사항목 최대 16개 출력
        final int QT_ITEM_PAGE_ROWS = 10;   // 시험일지 1페이지 표시갯수

        List<QualityTestReport> qtSheetList = new ArrayList<>();

        String reportType = "_type1a";   // 원료 _type1a, 반제품 _type2a
        String titleLotNo = "Lot NO.";   // Lot NO. , 제조번호
        String printLotNo = "";

        // id 1건만 처리하는 것으로 변경함
        String qualityTestId = ids[0];
        QualityTestReport qtSheet = new QualityTestReport();
        QualityTestView qualityTest = getViewService().getById(qualityTestId);

        String testGbName = "";
        switch(qualityTest.getItemGb()) {
            case "1":
                testGbName = "원재료";
                qtSheet.setReqDept("구매부");
                reportType = "_type1a";
                titleLotNo = "Lot NO.";
                printLotNo = qualityTest.getLotNo();
                break;
            case "2":
                testGbName = "부재료";
                qtSheet.setReqDept("구매부");
                reportType = "_type1a";
                titleLotNo = "Lot NO.";
                printLotNo = qualityTest.getLotNo();
                break;
            case "3":
                testGbName = "반제품";
                qtSheet.setReqDept("생산부");
                reportType = "_type2a";
                titleLotNo = "제조번호";
                printLotNo = qualityTest.getProdNo();
                break;
            case "6":
                testGbName = "완제품";
                qtSheet.setReqDept("생산부");
                reportType = "_type2a";
                titleLotNo = "Lot No.";
                printLotNo = qualityTest.getProdNo();
                break;
            default:
                testGbName = "기타검사";
        }


        // 재검사 처리 : 시험번호 + serNo,  부서명
        String printTestNo = qualityTest.getTestNo();
        if (StringUtils.isNotBlank(qualityTest.getRetestYn()) && qualityTest.getRetestYn().equals("Y")) {
            testGbName += "(재검사)";
            qtSheet.setReqDept("생산부");
            printTestNo = qualityTest.getTestNo() + " - " + qualityTest.getRetestSerNo();
        }

        qtSheet.setTestNo(printTestNo);
        qtSheet.setTestGbName(testGbName);
        qtSheet.setItemCd(qualityTest.getItemCd());
        qtSheet.setItemName(qualityTest.getItemName());
        qtSheet.setLotNo(printLotNo);     // 품목유형에 따라 각각 lotNo, prodNo로 셋팅한 값으로 처리한다.

        // 날짜 관련 처리
        if (qualityTest.getShelfLife() != null ) {
            qtSheet.setShelfLife(qualityTest.getShelfLife().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        }
        if (qualityTest.getExpiryDate() != null) {
            qtSheet.setExpiryDate(qualityTest.getExpiryDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        }

        if (qualityTest.getReqDate() != null) {
            qtSheet.setReqDate(qualityTest.getReqDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        }
        if (qualityTest.getTestDate() != null) {
            qtSheet.setTestDate(qualityTest.getTestDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        }
        if (qualityTest.getConfirmDate() != null) {
            qtSheet.setConfirmDate(qualityTest.getConfirmDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
        }

        qtSheet.setStorageName(qualityTest.getStorageName());
        qtSheet.setCustomerName(qualityTest.getCustomerName());

        // 사원명 처리 :: 모든 memberCd에 직접 이름이 들어가 있음
        qtSheet.setReqMember(qualityTest.getReqMemberCd());
        qtSheet.setTestMember(qualityTest.getTestMemberCd());
        qtSheet.setOrderMember(qualityTest.getOrderMemberCd());
        qtSheet.setSampleMember(qualityTest.getSampleMemberCd());
        qtSheet.setConfirmMember(qualityTest.getConfirmMemberCd());

        DecimalFormat df1 = new DecimalFormat("#,##0.000");
        DecimalFormat df2 = new DecimalFormat("#,##0");
        qtSheet.setReqQty(df1.format(qualityTest.getReqQty()) + " kg");
        qtSheet.setSampleQty(df2.format(qualityTest.getSampleQty()) + " g(ml)");
        qtSheet.setTestResult(qualityTest.getPassStateName());

        // subItems 만들기
        List<QualityTestReportItem> qtItemList = new ArrayList<>();

        QueryWrapper<QualityTestMethodView> queryWrapper = new QueryWrapper<QualityTestMethodView>().eq(StringUtils.camelToUnderline("qualityTestId"), qualityTestId);
        List<QualityTestMethodView> qtMethods = this.qualityTestMethodViewService.list(queryWrapper);
        int rowCount = 0;
        for (QualityTestMethodView qtMethod: qtMethods) {
            rowCount++;

            QualityTestReportItem qtItem = new QualityTestReportItem();

            qtItem.setRowNo(rowCount);
            qtItem.setTestItem(qtMethod.getTestItem());
            qtItem.setTestMethod(qtMethod.getTestMethod());
            qtItem.setTestSpec(qtMethod.getTestSpec());
            qtItem.setTestResult(qtMethod.getTestResult());
            qtItem.setTestMember(qtMethod.getTestMemberName());
            qtItem.setTestDateString(qtMethod.getTestDateString());
            qtItem.setPassStateName(qtMethod.getPassState());
            qtItem.setConfirmMember(qualityTest.getConfirmMemberCd());

            qtItemList.add(qtItem);
        }

        // 시험일지  페이지당 10칸씩 표시하기 위함
        List<QualityTestReportItem> qtItemList2 = new ArrayList<>(qtItemList);

        int appendRowCount = (QT_ITEM_PAGE_ROWS - (rowCount % QT_ITEM_PAGE_ROWS)) % QT_ITEM_PAGE_ROWS ;
        while(appendRowCount > 0) {
            appendRowCount--;
            QualityTestReportItem qtItem = new QualityTestReportItem();
            qtItemList2.add(qtItem);
        }

        // 성적서 최대 16칸 표시하기 위함
        while(rowCount < QT_ITEM_MAX_ROWS) {
            rowCount++;
            QualityTestReportItem qtItem = new QualityTestReportItem();
            qtItemList.add(qtItem);
        }
        qtSheet.setTestReportItems(qtItemList);
        qtSheetList.add(qtSheet);

        try {
            // 시험일지 : 2번째 순서 리포트
            Map<String, Object> parameters2 = new HashMap<>();
            InputStream logoStream2 = getClass().getResourceAsStream("/static/images/logo1.png");
            parameters2.put("logo", logoStream2);
            parameters2.put("itemName", qualityTest.getItemName() );
            parameters2.put("titleLotNo", titleLotNo);
            parameters2.put("lotNo", printLotNo);
            parameters2.put("testNo", printTestNo);

            InputStream reportStream2 = getClass().getResourceAsStream("/report/quality_test_sheet_type1b.jrxml");
            JasperReport jasperReport2 = JasperCompileManager.compileReport(reportStream2);
            JRBeanCollectionDataSource dataSource2 = new JRBeanCollectionDataSource(qtItemList2);
            JasperPrint jasperPrint2= JasperFillManager.fillReport(jasperReport2, parameters2, dataSource2);


            // 성적서 : 1번째 순서 리포트
            Map<String, Object> parameters1 = new HashMap<>();
            InputStream logoStream1 = getClass().getResourceAsStream("/static/images/logo1.png");
            parameters1.put("logo", logoStream1);

            InputStream inputStream = getClass().getResourceAsStream("/report/quality_test_sub1a.jrxml");
            JasperReport subReport = JasperCompileManager.compileReport(inputStream);
            parameters1.put("subReport", subReport);

            InputStream reportStream1 = getClass().getResourceAsStream("/report/quality_test_sheet" + reportType + ".jrxml");
            JasperReport jasperReport1 = JasperCompileManager.compileReport(reportStream1);
            JRBeanCollectionDataSource dataSource1 = new JRBeanCollectionDataSource(qtSheetList);
            JasperPrint jasperPrint1 = JasperFillManager.fillReport(jasperReport1, parameters1, dataSource1);


            // 통합하여 리포트 생성
            List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
            jasperPrintList.add(jasperPrint1);
            jasperPrintList.add(jasperPrint2);

            JRPdfExporter exporter = new JRPdfExporter();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));    // 다수의 jasperPrint 입렦
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));         // ByteArrayStream 출력
            exporter.exportReport();

            byte[] pdfContent = baos.toByteArray();
            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("qualityTestSheet_list", pdfContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("리포트 생성중 에러발생!");
        }
    }

    @RequestMapping(value = "/getPdf", method = RequestMethod.GET)
    public ResponseEntity<Resource> getPdf(@RequestParam(name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<QualityTestView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<QualityTestView> QualityTestViewList = getViewService().list(queryWrapper);

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

        QueryWrapper<QualityTestView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        Map<String, Object> paramMap = SearchUtil.parseParam(conditionJson);
        List<QualityTestView> QualityTestViewList = getViewService().list(queryWrapper);

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/quality_test_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("품질검사리스트");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            String excelTitle = "회사명: (주)진코스텍 / " + paramMap.get("testStartDate") + " ~ " + paramMap.get("testEndDate");
            ExcelPoiUtil.getCell(sheet, 0, 0).setCellValue(excelTitle);
            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 13);
            for (QualityTestView qualityTestView : QualityTestViewList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(qualityTestView.getTestNo());
                String testDate = (qualityTestView.getTestDate() != null)? qualityTestView.getTestDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) : "";
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(testDate);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(qualityTestView.getReqDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(qualityTestView.getReqMemberName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(qualityTestView.getItemCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(qualityTestView.getItemName());
                String lotNo = (qualityTestView.getLotNo() != null) ? qualityTestView.getLotNo().replaceAll("!!", " ") : "";
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(lotNo);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(qualityTestView.getReqQty().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue(qualityTestView.getStorageName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue(qualityTestView.getRetestYn().equals("Y") ? "재검사" : "입고검사");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 10, cellStyleList.get(10)).setCellValue(qualityTestView.getTestStateName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 11, cellStyleList.get(11)).setCellValue(qualityTestView.getConfirmDate() != null ? qualityTestView.getConfirmDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) : "");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 12, cellStyleList.get(12)).setCellValue(qualityTestView.getPassStateName());
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("quality_test_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    @RequestMapping(value = "/getExcelReq",method = RequestMethod.GET)
    public ResponseEntity<Resource> getExcelReq(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<QualityTestView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        Map<String, Object> paramMap = SearchUtil.parseParam(conditionJson);
        List<QualityTestView> QualityTestViewList = getViewService().list(queryWrapper);

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/quality_test_req_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("Sheet1");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            String excelTitle = "회사명: (주)진코스텍 / " + paramMap.get("testStartDate") + " ~ " + paramMap.get("testEndDate");
            ExcelPoiUtil.getCell(sheet, 0, 0).setCellValue(excelTitle);
            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 10);
            for (QualityTestView qualityTestView : QualityTestViewList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(qualityTestView.getTestNo());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(qualityTestView.getReqDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(qualityTestView.getReqMemberName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(qualityTestView.getItemCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(qualityTestView.getItemName());
                String lotNo = (qualityTestView.getLotNo() != null) ? qualityTestView.getLotNo().replaceAll("!!", " ") : "";
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(lotNo);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(qualityTestView.getReqQty().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(qualityTestView.getStorageName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue(qualityTestView.getTestStateName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue(qualityTestView.getPassStateName());
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("quality_test_req_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    /** 성적서 Excel **/
    @RequestMapping(value = "/getQtReport/{id}",method = RequestMethod.GET)
    public ResponseEntity<Resource> getQtReport(@PathVariable String id) throws Exception {
        return qualityTestService.getQtReport(id);
    }

    /** 시험일지 Excel **/
    @RequestMapping(value = "/getQtJournal/{id}",method = RequestMethod.GET)
    public ResponseEntity<Resource> getQtJournal(@PathVariable String id) throws Exception {
        return qualityTestService.getQtJournal(id);
    }

}

