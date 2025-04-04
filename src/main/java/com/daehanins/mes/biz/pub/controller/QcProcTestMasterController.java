package com.daehanins.mes.biz.pub.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.common.code.WorkOrderItemStatus;
import com.daehanins.mes.biz.pub.entity.*;
import com.daehanins.mes.biz.pub.service.*;
import com.daehanins.mes.biz.pub.vo.QcProcTestDetailsWithLine;
import com.daehanins.mes.biz.pub.vo.QcProcTestDetailsWithLineView;
import com.daehanins.mes.biz.qt.entity.QualityTestView;
import com.daehanins.mes.biz.work.entity.WorkOrderBatchView;
import com.daehanins.mes.biz.work.entity.WorkOrderItem;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.utils.SearchUtil;
import com.daehanins.mes.common.vo.PageVo;
import com.daehanins.mes.common.vo.RestResponse;
import org.apache.poi.ss.usermodel.*;
import org.checkerframework.checker.units.qual.A;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * QcProcTestMaster Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2023-04-18
 */
@RestController
@RequestMapping("/pub/qc-proc-test-master")
public class QcProcTestMasterController extends BaseController<QcProcTestMaster, QcProcTestMasterView, String> {

    @Autowired
    private IQcProcTestMasterService qcProcTestMasterService;

    @Autowired
    private IQcProcTestMasterViewService qcProcTestMasterViewService;

    @Autowired
    private IQcProcTestSampleService qcProcTestSampleService;

    @Autowired
    private IProcTestTypeMethodService procTestTypeMethodService;

    @Autowired
    private IQcProcTestMethodService qcProcTestMethodService;

    @Autowired
    private IQcProcTestDetailService qcProcTestDetailService;

    @Autowired
    private IQcProcTestLineService qcProcTestLineService;

    @Autowired
    private IQcProcTestLineViewService qcProcTestLineViewService;

    @Override
    public IQcProcTestMasterService getTableService() {
        return this.qcProcTestMasterService;
    }

    @Override
    public IQcProcTestMasterViewService getViewService() {
    return this.qcProcTestMasterViewService;
    }

    /** 작업지시 조회 (정렬순서로 인해 개별로 생성함) **/
    @RequestMapping(value = "/getPageAdvanced",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<Page<QcProcTestMasterView>> getPageAdvanced(@ModelAttribute PageVo param, @RequestParam (name="condition", required=false ) String conditionJson){
        Page<QcProcTestMasterView> page = new Page<>(param.getCurrentPage(), param.getPageSize());
        QueryWrapper<QcProcTestMasterView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        queryWrapper.orderByDesc("order_no", "batch_ser_no");
        Page<QcProcTestMasterView> data = this.getViewService().page(page, queryWrapper );
        return new RestUtil<Page<QcProcTestMasterView>>().setData(data);
    }

    @RequestMapping(value = "/initQcProcMaster", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<QcProcTestMasterView> initQcProcMaster(@RequestBody QcProcTestMaster qcProcTestMaster) {
        this.getTableService().save(qcProcTestMaster);
        QcProcTestMasterView result = this.getViewService().getById(qcProcTestMaster.getWorkOrderBatchId());
        return new RestUtil<QcProcTestMasterView>().setData(result);
    }

    @RequestMapping(value = "/getQcProcTestMaster/{workOrderBatchId}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<QcProcTestMasterView> getQcProcTestMaster(@PathVariable String workOrderBatchId){
        QcProcTestMasterView result = this.getViewService().getById(workOrderBatchId);
        return new RestUtil<QcProcTestMasterView>().setData(result);
    }

    @RequestMapping(value = "/getQcProcTestSample/{qcProcTestMasterId}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<QcProcTestSample> getQcProcTestSample(@PathVariable String qcProcTestMasterId){
        QcProcTestSample result = new QcProcTestSample();
        QueryWrapper<QcProcTestSample> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("qc_proc_test_master_id",qcProcTestMasterId);
        int isExist = qcProcTestSampleService.count(queryWrapper);
        if(isExist > 0){
            result = qcProcTestSampleService.getOne(queryWrapper);
        } else {
            result.setQcProcTestMasterId(qcProcTestMasterId);
        }
        return new RestUtil<QcProcTestSample>().setData(result);
    }

    /**
     * testType
     * QRC003 : 코팅공정
     * QRC004 : 충전공정
     * QRC005 : 포장공정
     **/
    @RequestMapping(value = "/getQcMethod/{qcProcTestMasterId}/{testType}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<QcProcTestMethod>> getQcMethod(@PathVariable String qcProcTestMasterId, @PathVariable String testType){
        List<QcProcTestMethod> result = qcProcTestMethodService.getQcMethod(qcProcTestMasterId, testType);
        return new RestUtil<List<QcProcTestMethod>>().setData(result);
    }

    /**
     * testType
     * WE115 : 중량 검사 (1X15)
     * WE613 : 중량 검사 (6X13) use Line
     * WE616 : 중량 검사 (6X16) use Line
     * GA115 : 겔 수량 검사 (1X15)
     * CA515 : 캡핑세기(완제품) (5X15) use Line
     * ES515 : 중량검사(에센스) (5X15) use Line
     **/
    @RequestMapping(value = "/getQcDetail/{qcProcTestMasterId}/{testType}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<QcProcTestDetailsWithLineView> getQcDetail(@PathVariable String qcProcTestMasterId, @PathVariable String testType){
        QcProcTestDetailsWithLineView result = new QcProcTestDetailsWithLineView();
        result.setQcProcTestLineViews(qcProcTestLineViewService.getQcDetailLines(qcProcTestMasterId, testType));
        result.setQcProcTestDetails(qcProcTestDetailService.getQcDetail(qcProcTestMasterId, testType));
        return new RestUtil<QcProcTestDetailsWithLineView>().setData(result);
    }

    @RequestMapping(value = "/getQcLine/{qcProcTestMasterId}/{testType}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<QcProcTestLineView>> getQcLine(@PathVariable String qcProcTestMasterId, @PathVariable String testType){
        List<QcProcTestLineView> result = qcProcTestLineViewService.getQcDetailLines(qcProcTestMasterId, testType);
        return new RestUtil<List<QcProcTestLineView>>().setData(result);
    }

    @RequestMapping(value = "/saveQcProcTestMaster", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<QcProcTestMasterView> saveQcProcTestMaster(@RequestBody QcProcTestMaster qcProcTestMaster) {
        this.getTableService().saveOrUpdate(qcProcTestMaster);
        QcProcTestMasterView result = this.getViewService().getById(qcProcTestMaster.getWorkOrderBatchId());
        return new RestUtil<QcProcTestMasterView>().setData(result);
    }

    @RequestMapping(value = "/saveQcProcTestSample", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<QcProcTestSample> saveQcProcTestSample(@RequestBody QcProcTestSample qcProcTestSample) {
        qcProcTestSampleService.saveOrUpdate(qcProcTestSample);
        return new RestUtil<QcProcTestSample>().setData(qcProcTestSample);
    }

    @RequestMapping(value = "/saveQcProcTestMethod", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<List<QcProcTestMethod>> saveQcProcTestMethod(@RequestBody List<QcProcTestMethod> qcProcTestMethods) {
        qcProcTestMethodService.saveOrUpdateBatch(qcProcTestMethods);
        return new RestUtil<List<QcProcTestMethod>>().setData(qcProcTestMethods);
    }

    @RequestMapping(value = "/saveQcProcTestDetail", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<QcProcTestDetailsWithLineView> saveQcProcTestDetail(@RequestBody QcProcTestDetailsWithLine qcProcTestDetailsWithLine) {
        if (qcProcTestDetailsWithLine.getQcProcTestLines().size() > 0) {
            qcProcTestLineService.saveOrUpdateBatch(qcProcTestDetailsWithLine.getQcProcTestLines());
        }
        qcProcTestDetailService.saveOrUpdateBatch(qcProcTestDetailsWithLine.getQcProcTestDetails());
        String id = qcProcTestDetailsWithLine.getQcProcTestDetails().get(0).getQcProcTestMasterId();
        String testType = qcProcTestDetailsWithLine.getQcProcTestDetails().get(0).getTestType();

        QcProcTestDetailsWithLineView result = new QcProcTestDetailsWithLineView();
        result.setQcProcTestLineViews(qcProcTestLineViewService.getQcDetailLines(id, testType));
        result.setQcProcTestDetails(qcProcTestDetailService.getQcDetail(id, testType));

        return new RestUtil<QcProcTestDetailsWithLineView>().setData(result);
    }

    @RequestMapping(value = "/saveQcProcTestLine", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<List<QcProcTestLine>> saveQcProcTestLine(@RequestBody List<QcProcTestLine> qcProcTestLines) {
        qcProcTestLineService.saveOrUpdateBatch(qcProcTestLines);
        return new RestUtil<List<QcProcTestLine>>().setData(qcProcTestLines);
    }


    @RequestMapping(value = "/getQcProcTestExcel/{workOrderBatchId}", method = RequestMethod.GET)
    public ResponseEntity<Resource> getQcProcTestExcel (@PathVariable String workOrderBatchId) throws Exception {
        return this.getTableService().getQcProcTestExcelByType(workOrderBatchId);
    }


    @RequestMapping(value = "/getExcel",method = RequestMethod.GET)
    public ResponseEntity<Resource> getExcel(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception {

        Map<String, Object> paramMap = SearchUtil.parseParam(conditionJson);
        QueryWrapper<QcProcTestMasterView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        queryWrapper.orderByDesc("order_no", "batch_ser_no");
        List<QcProcTestMasterView> data = getViewService().list(queryWrapper);

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/qc_proc_test_list.xlsx");
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
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 10);
            for (QcProcTestMasterView item : data){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(lineNo);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(item.getAreaName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(item.getItemName());
                String prodNo = (item.getProdNo() != null) ? item.getProdNo().replaceAll("!!", " ") : "";
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(prodNo);
                String lotNo = (item.getLotNo() != null) ? item.getLotNo().replaceAll("!!", " ") : "";
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(lotNo);
                String lotNo2 = (item.getLotNo2() != null) ? item.getLotNo2().replaceAll("!!", " ") : "";
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(lotNo2);
                String batchStatus = WorkOrderItemStatus.getStatusName(item.getBatchStatus());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(batchStatus);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(this.getTestStateName(item.getTestState()));
                String chargingDate = (item.getChargingDate() != null)? item.getChargingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue(chargingDate);
                String packingDate = (item.getPackingDate() != null)? item.getPackingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue(packingDate);
                rowNo++;
                lineNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("qc_proc_test_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    public String getTestStateName (String testState) {
        String result = "";
        if (testState.equals("NON")) result = "미작성";
        if (testState.equals("ING")) result = "작성중";
        if (testState.equals("END")) result = "작성완료";
        return result;
    }
}

