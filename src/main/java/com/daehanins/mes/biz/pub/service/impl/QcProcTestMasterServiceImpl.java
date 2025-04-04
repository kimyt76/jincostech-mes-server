package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.common.code.ProcessCd;
import com.daehanins.mes.biz.pub.entity.*;
import com.daehanins.mes.biz.pub.mapper.QcProcTestMasterMapper;
import com.daehanins.mes.biz.pub.service.*;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;
import com.daehanins.mes.biz.work.service.IWorkOrderItemViewService;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * <p>
 * QcProcTestMaster 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2023-04-18
 */
@Service
public class QcProcTestMasterServiceImpl extends ServiceImpl<QcProcTestMasterMapper, QcProcTestMaster> implements IQcProcTestMasterService {

    @Autowired
    IQcProcTestMasterViewService qcProcTestMasterViewService;

    @Autowired
    IQcProcTestMethodService qcProcTestMethodService;

    @Autowired
    IQcProcTestSampleService qcProcTestSampleService;

    @Autowired
    IQcProcTestLineViewService qcProcTestLineViewService;

    @Autowired
    IQcProcTestDetailService qcProcTestDetailService;

    @Autowired
    IWorkOrderItemViewService workOrderItemViewService;

//    private static final DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public ResponseEntity<Resource> getQcProcTestExcelByType(String workOrderBatchId) throws Exception {
        QcProcTestMasterView master = qcProcTestMasterViewService.getById(workOrderBatchId);
        ResponseEntity<Resource> result;

        System.out.println("============================================= : " + master.getQcProcTestType());

        switch (master.getQcProcTestType()) {
            case "A":
                result = this.getExcelTypeA(master);
                break;
            case "B":
                result = this.getExcelTypeB(master);
                break;
            case "C":
                result = this.getExcelTypeC(master);
                break;
            case "D":
                result = this.getExcelTypeD(master);
                break;
            default:
                result = this.getExcelTypeA(master);
                break;
        }
        return result;
    }

    /**기초, 시트마스크 **/
    public ResponseEntity<Resource> getExcelTypeA(QcProcTestMasterView master) throws Exception {
        String masterId = master.getQcProcTestMasterId();
        //충전 공정
        List<QcProcTestMethod> chargingMethods = qcProcTestMethodService.getQcMethod(masterId, "QRC004");
        //포장 공정
        List<QcProcTestMethod> packingMethod = qcProcTestMethodService.getQcMethod(masterId, "QRC005");

        //충전작업지시
        WorkOrderItemView chargingItem = workOrderItemViewService.getById(master.getChargingId());
        //포장작업지시
        WorkOrderItemView packingItem = workOrderItemViewService.getById(master.getPackingId());

        //WE616 line setting
        List<QcProcTestLineView> WE616_line = qcProcTestLineViewService.getQcDetailLines(masterId, "WE616");
        //WE616 data
        List<QcProcTestDetail> WE616_details = qcProcTestDetailService.getQcDetail(masterId, "WE616");
        //sample data
        QcProcTestSample sampleData = qcProcTestSampleService.getOne(
                new QueryWrapper<QcProcTestSample>().eq("qc_proc_test_master_id", masterId));

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/qc_proc_test_type_a.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);

            //품목코드, 문서번호(JQP12-01), 문서버전(Rev.01), 제품명, 고객사, 제조일자, 제조번호, 표시량, 로트인쇄, 검사자
            String[] infoData = {
                    (master.getItemCd() != null) ? master.getItemCd() : ""
                    , "JQP12-01"
                    , "(Rev.02)"
                    , (master.getItemName() != null) ? master.getItemName() : ""
                    , (master.getCustomerName() != null) ? master.getCustomerName() : ""
                    , (master.getProdDate() != null) ? sdf.format(master.getProdDate()) : ""
                    , (master.getProdNo() != null) ? master.getProdNo() : ""
                    , (master.getDisplayCapacity() != null) ? master.getDisplayCapacity() : ""
                    , (master.getLotNo2() != null && !master.getLotNo2().equals("/")) ? master.getLotNo2() : (master.getLotNo() != null)? master.getLotNo() : ""
                    , (master.getTestMember() != null) ? master.getTestMember() : ""
            };

            /** Sheet1 : 충전작업, 중량검사, 포장작업 **/
            Sheet sheet1 = workbook.getSheet("Sheet1");

            /** 상단 정보영역 **/
            //품목코드, 문서번호(JQP12-01), 문서버전(Rev.01), 제품명, 고객사, 제조일자, 제조번호, 표시량, 로트인쇄, 검사자
            String[] sheet1Info = { "AH1", "AH3", "AH4", "F6", "AH6", "F7", "T7", "AH7", "F8", "AH8" };

            for (int i = 0; i < 10; i++) {
                ExcelPoiUtil.getCellRef(sheet1, sheet1Info[i]).setCellValue(infoData[i]);
            }

            /** 충전작업 **/
            //작업일자 (시작일시)Q11
            String charging_startTime = (chargingItem.getWorkStartTime() != null)? sdf.format(chargingItem.getWorkStartTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "Q11").setCellValue(charging_startTime);
            //작업일자 (종료일시)X11
            String charging_endTime = (chargingItem.getWorkEndTime() != null)? sdf.format(chargingItem.getWorkEndTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "X11").setCellValue(charging_endTime);

            //충전작업 컨텐츠
            int idx1 = 13;
            for (QcProcTestMethod item : chargingMethods){
                ExcelPoiUtil.getCellRef(sheet1, "C" + idx1).setCellValue(item.getTestMethod());
                ExcelPoiUtil.getCellRef(sheet1, "O" + idx1).setCellValue(item.getTestItem());
                String testResult = (item.getTestResult() != null) ?
                        (item.getTestResult().equals("Y"))? "■ 양호   □ 양호하지않음" : "□ 양호   ■ 양호하지않음"
                        : "□ 양호   □ 양호하지않음";
                ExcelPoiUtil.getCellRef(sheet1, "AD" + idx1).setCellValue(testResult);
                idx1++;
            }

            /** 중량검사 **/
            //충전지시량 AH21
            ExcelPoiUtil.getCellRef(sheet1, "AH21").setCellValue(master.getChargingQtys());

            String[] lineNameList = { "I23", "M23", "Q23", "U23", "Y23", "AC23" };

            // 라인명 처리
            for (int i = 0; i < lineNameList.length; i++) {
                String lineName = (WE616_line.get(i).getLineName() !=null && !WE616_line.get(i).getLineName().equals("")) ?
                        WE616_line.get(i).getLineName() : "(     )라인";
                ExcelPoiUtil.getCellRef(sheet1, lineNameList[i]).setCellValue(lineName);
            }

            //중량검사 내용 처리
            int idx2 = 24;
            for (QcProcTestDetail items : WE616_details) {
                //채취시간
                String testTime = (items.getTestTime() != null && !items.getTestTime().equals(""))? items.getTestTime() : ":";
                ExcelPoiUtil.getCellRef(sheet1, "E" + idx2).setCellValue(testTime);
                //라인1
                String line1 = (items.getLine1() != null) ? items.getLine1().toString() + " g" : "g";
                ExcelPoiUtil.getCellRef(sheet1, "I" + idx2).setCellValue(line1);
                //라인2
                String line2 = (items.getLine2() != null) ? items.getLine2().toString() + " g" : "g";
                ExcelPoiUtil.getCellRef(sheet1, "M" + idx2).setCellValue(line2);
                //라인3
                String line3 = (items.getLine3() != null) ? items.getLine3().toString() + " g" : "g";
                ExcelPoiUtil.getCellRef(sheet1, "Q" + idx2).setCellValue(line3);
                //라인4
                String line4 = (items.getLine4() != null) ? items.getLine4().toString() + " g" : "g";
                ExcelPoiUtil.getCellRef(sheet1, "U" + idx2).setCellValue(line4);
                //라인5
                String line5 = (items.getLine5() != null) ? items.getLine5().toString() + " g" : "g";
                ExcelPoiUtil.getCellRef(sheet1, "Y" + idx2).setCellValue(line5);
                //라인6
                String line6 = (items.getLine6() != null) ? items.getLine6().toString() + " g" : "g";
                ExcelPoiUtil.getCellRef(sheet1, "AC" + idx2).setCellValue(line6);
                //검사겳과
                String passYn = (items.getPassYn() != null) ?
                        (items.getPassYn().equals("Y")) ? "■ 적합 □ 부적합(           )" : "□ 적합 ■ 부적합(           )"
                        : "□ 적합 □ 부적합(           )";
                ExcelPoiUtil.getCellRef(sheet1, "AG" + idx2).setCellValue(passYn);
                idx2++;
            }

            /** 포장작업 **/
            //작업일자 (시작일시)Q42
            String packing_startTime = (packingItem.getWorkStartTime() != null)? sdf.format(packingItem.getWorkStartTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "Q42").setCellValue(packing_startTime);
            //작업일자 (종료일시)X42
            String packing_endTime = (packingItem.getWorkEndTime() != null)? sdf.format(packingItem.getWorkEndTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "X42").setCellValue(packing_endTime);

            //포장작업 컨텐츠
            int idx3 = 44;
            for (QcProcTestMethod item : packingMethod){
                ExcelPoiUtil.getCellRef(sheet1, "C" + idx3).setCellValue(item.getTestMethod());
                ExcelPoiUtil.getCellRef(sheet1, "O" + idx3).setCellValue(item.getTestItem());
                String testResult = (item.getTestResult() != null) ?
                        (item.getTestResult().equals("Y"))? "■ 양호   □ 양호하지않음" : "□ 양호   ■ 양호하지않음"
                        : "□ 양호   □ 양호하지않음";
                ExcelPoiUtil.getCellRef(sheet1, "AD" + idx3).setCellValue(testResult);
                idx3++;
            }

            //비고 A52
            ExcelPoiUtil.getCellRef(sheet1, "A52").setCellValue(master.getMemo());

            /** Sheet2 : 검체채취기준 **/
            Sheet sheet2 = workbook.getSheet("Sheet2");

            /*상단 정보영역*/
            //품목코드, 문서번호(JQP12-01), 문서버전(Rev.01), 제품명, 고객사, 제조일자, 제조번호, 표시량, 로트인쇄, 검사자
            String[] sheet2Info = { "AD1", "AD3", "AD4", "F6", "AD6", "F7", "R7", "AD7", "F8", "AD8" };

            for (int i = 0; i < sheet1Info.length; i++) {
                ExcelPoiUtil.getCellRef(sheet2, sheet2Info[i]).setCellValue(infoData[i]);
            }

            Boolean isSample = sampleData != null;

            //채취일자 Q29
            ExcelPoiUtil.getCellRef(sheet2, "Q29").setCellValue((isSample && sampleData.getSampleDate() != null) ? sampleData.getSampleDate() : "");

            int[] qtys = {
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty1() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty2() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty3() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty4() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty5() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty6() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty7() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty8() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty9() : 0
            };

            String[] sampleAddr = { "E37", "G37", "I37", "K37", "M37", "O37", "Q37", "W37", "AC37" };

            int sum = 0;
            for (int i = 0; i < sampleAddr.length; i++) {
                ExcelPoiUtil.getCellRef(sheet2, sampleAddr[i]).setCellValue(qtys[i]);
                sum += qtys[i];
            }
            //총 채취량 AD30
            ExcelPoiUtil.getCellRef(sheet2, "AD30").setCellValue(sum);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("공정검사서", excelContent.length, "xlsx");

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    /**
     * testType
     * QRC003 : 코팅공정
     * QRC004 : 충전공정
     * QRC005 : 포장공정
     **/

    /**
     * testType
     * WE115 : 중량 검사 (1X15)
     * WE613 : 중량 검사 (6X13) use Line
     * WE616 : 중량 검사 (6X16) use Line
     * GA115 : 겔 수량 검사 (1X15)
     * CA515 : 캡핑세기(완제품) (5X15) use Line
     * ES515 : 중량검사(에센스) (5X15) use Line
     **/

    public ResponseEntity<Resource> getExcelTypeB(QcProcTestMasterView master) throws Exception {

        String masterId = master.getQcProcTestMasterId();

        //코팅 공정
        List<QcProcTestMethod> coatingMethods = qcProcTestMethodService.getQcMethod(masterId, "QRC003");
        //충전 공정
        List<QcProcTestMethod> chargingMethods = qcProcTestMethodService.getQcMethod(masterId, "QRC004");
        //포장 공정
        List<QcProcTestMethod> packingMethod = qcProcTestMethodService.getQcMethod(masterId, "QRC005");
        //충전작업지시
        WorkOrderItemView coatingItem = workOrderItemViewService.getById(master.getCoatingId());
        //충전작업지시
        WorkOrderItemView chargingItem = workOrderItemViewService.getById(master.getChargingId());
        //포장작업지시
        WorkOrderItemView packingItem = workOrderItemViewService.getById(master.getPackingId());
        //WE115 data
        List<QcProcTestDetail> WE115_details = qcProcTestDetailService.getQcDetail(masterId, "WE115");
        //GA115 data
        List<QcProcTestDetail> GA115_details = qcProcTestDetailService.getQcDetail(masterId, "GA115");
        //sample data
        QcProcTestSample sampleData = qcProcTestSampleService.getOne(
                new QueryWrapper<QcProcTestSample>().eq("qc_proc_test_master_id", masterId));

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/qc_proc_test_type_b.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);

            //품목코드, 문서번호(JQP12-01), 문서버전(Rev.01), 제품명, 고객사, 제조일자, 제조번호, 표시량, 로트인쇄, 검사자
            String[] infoData = {
                    (master.getItemCd() != null) ? master.getItemCd() : ""
                    , "JQP12-01"
                    , "(Rev.02)"
                    , (master.getItemName() != null) ? master.getItemName() : ""
                    , (master.getCustomerName() != null) ? master.getCustomerName() : ""
                    , (master.getProdDate() != null) ? sdf.format(master.getProdDate()) : ""
                    , (master.getProdNo() != null) ? master.getProdNo() : ""
                    , (master.getDisplayCapacity() != null) ? master.getDisplayCapacity() : ""
                    , (master.getLotNo2() != null && !master.getLotNo2().equals("/")) ? master.getLotNo2() : (master.getLotNo() != null)? master.getLotNo() : ""
                    , (master.getTestMember() != null) ? master.getTestMember() : ""
            };
            /** Sheet1 : 코팅작업, 충전작업, 중량검사, 겔수량검사, 포장작업 **/
            Sheet sheet1 = workbook.getSheet("Sheet1");
            /*상단 정보영역*/
            //품목코드, 문서번호(JQP12-01), 문서버전(Rev.01), 제품명, 고객사, 제조일자, 제조번호, 표시량, 로트인쇄, 검사자
            String[] sheet1Info = { "AD1", "AD3", "AD4", "F6", "AD6", "F7", "R7", "AD7", "F8", "AD8" };
            for (int i = 0; i < 10; i++) {
                ExcelPoiUtil.getCellRef(sheet1, sheet1Info[i]).setCellValue(infoData[i]);
            }
            /** 코팅작업  **/
            //작업일자 (시작일시)Q11
            String coating_startTime = (coatingItem.getWorkStartTime() != null)? sdf.format(coatingItem.getWorkStartTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "Q10").setCellValue(coating_startTime);
            //작업일자 (종료일시)X11
            String coating_endTime = (coatingItem.getWorkEndTime() != null)? sdf.format(coatingItem.getWorkEndTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "X10").setCellValue(coating_endTime);

            int idx1 = 12;
            for (QcProcTestMethod item : coatingMethods){
                ExcelPoiUtil.getCellRef(sheet1, "C" + idx1).setCellValue(item.getTestMethod());
                ExcelPoiUtil.getCellRef(sheet1, "O" + idx1).setCellValue(item.getTestItem());
                String testResult = (item.getTestResult() != null) ?
                        (item.getTestResult().equals("Y"))? "■ 양호   □ 양호하지않음" : "□ 양호   ■ 양호하지않음"
                        : "□ 양호   □ 양호하지않음";
                ExcelPoiUtil.getCellRef(sheet1, "Z" + idx1).setCellValue(testResult);
                idx1++;
            }

            /** 충전작업 **/
            //작업일자 (시작일시)Q17
            String charging_startTime = (chargingItem.getWorkStartTime() != null)? sdf.format(chargingItem.getWorkStartTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "Q17").setCellValue(charging_startTime);
            //작업일자 (종료일시)X17
            String charging_endTime = (chargingItem.getWorkEndTime() != null)? sdf.format(chargingItem.getWorkEndTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "X17").setCellValue(charging_endTime);

            int idx2 = 19;
            for (QcProcTestMethod item : chargingMethods){
                ExcelPoiUtil.getCellRef(sheet1, "C" + idx2).setCellValue(item.getTestMethod());
                ExcelPoiUtil.getCellRef(sheet1, "O" + idx2).setCellValue(item.getTestItem());
                String testResult = (item.getTestResult() != null) ?
                        (item.getTestResult().equals("Y"))? "■ 양호   □ 양호하지않음" : "□ 양호   ■ 양호하지않음"
                        : "□ 양호   □ 양호하지않음";
                ExcelPoiUtil.getCellRef(sheet1, "Z" + idx2).setCellValue(testResult);
                idx2++;
            }

            /* 중량검사 */

            //충전지시량 AD27
            ExcelPoiUtil.getCellRef(sheet1, "AD27").setCellValue(master.getChargingQtys());
            //todo 중량검사 15개
            // 검사시간, 수량, 검사결과 "□ 적합 □ 부적합", "■ 적합 □ 부적합", "□ 적합 ■ 부적합"
            String[] cell1 = { "B30", "B31", "B32", "B33", "B34", "M30", "M31", "M32", "M33", "M34", "Y30", "Y31", "Y32", "Y33", "Y34" };
            String[] cell2 = { "E30", "E31", "E32", "E33", "E34", "Q30", "Q31", "Q32", "Q33", "Q34", "AB30", "AB31", "AB32", "AB33", "AB34",};
            String[] cell3 = { "I30", "I31", "I32", "I33", "I34", "U30", "U31", "U32", "U33", "U34", "AF30", "AF31", "AF32", "AF33", "AF34",};

            for (int i = 0; i < cell1.length; i++) {
                QcProcTestDetail items = WE115_details.get(i);
                //검사시간
                String testTime = (items.getTestTime() != null && !items.getTestTime().equals(""))? items.getTestTime() : ":";
                ExcelPoiUtil.getCellRef(sheet1, cell1[i]).setCellValue(testTime);
                //수량
                String line1 = (items.getLine1() != null) ? items.getLine1().toString() + " g" : "g";
                ExcelPoiUtil.getCellRef(sheet1, cell2[i]).setCellValue(line1);
                //검사결과
                String passYn = (items.getPassYn() != null) ?
                        (items.getPassYn().equals("Y")) ? "■ 적합 □ 부적합" : "□ 적합 ■ 부적합"
                        : "□ 적합 □ 부적합";
                ExcelPoiUtil.getCellRef(sheet1, cell3[i]).setCellValue(passYn);
            }

            /** 겔수량검사 **/

            //충전 매 수 AD38
            ExcelPoiUtil.getCellRef(sheet1, "AD38").setCellValue(master.getChargingCnt());
            //todo 겔수량검사 15개
            // 검사시간, 수량, 검사결과 "□ 적합 □ 부적합", "■ 적합 □ 부적합", "□ 적합 ■ 부적합"
            String[] cell4 = { "B41", "B42", "B43", "B44", "B45", "M41", "M42", "M43", "M44", "M45", "Y41", "Y42", "Y43", "Y44", "Y45" };
            String[] cell5 = { "E41", "E42", "E43", "E44", "E45", "Q41", "Q42", "Q43", "Q44", "Q45", "AB41", "AB42", "AB43", "AB44", "AB45",};
            String[] cell6 = { "I41", "I42", "I43", "I44", "I45", "U41", "U42", "U43", "U44", "U45", "AF41", "AF42", "AF43", "AF44", "AF45",};

            for (int i = 0; i < cell1.length; i++) {
                QcProcTestDetail items = GA115_details.get(i);
                //검사시간
                String testTime = (items.getTestTime() != null && !items.getTestTime().equals(""))? items.getTestTime() : ":";
                ExcelPoiUtil.getCellRef(sheet1, cell4[i]).setCellValue(testTime);
                //수량
                String line1 = (items.getLine1() != null) ? items.getLine1().setScale(0).toString() + " 매" : "매";
                ExcelPoiUtil.getCellRef(sheet1, cell5[i]).setCellValue(line1);
                //검사결과
                String passYn = (items.getPassYn() != null) ?
                        (items.getPassYn().equals("Y")) ? "■ 적합 □ 부적합" : "□ 적합 ■ 부적합"
                        : "□ 적합 □ 부적합";
                ExcelPoiUtil.getCellRef(sheet1, cell6[i]).setCellValue(passYn);
            }

            /** 포장작업 **/
            //작업일자 (시작일시)Q48
            String packing_startTime = (packingItem.getWorkStartTime() != null)? sdf.format(packingItem.getWorkStartTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "Q48").setCellValue(packing_startTime);
            //작업일자 (종료일시)X48
            String packing_endTime = (packingItem.getWorkEndTime() != null)? sdf.format(packingItem.getWorkEndTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "X48").setCellValue(packing_endTime);

            int idx3 = 50;
            for (QcProcTestMethod item : packingMethod){
                ExcelPoiUtil.getCellRef(sheet1, "C" + idx3).setCellValue(item.getTestMethod());
                ExcelPoiUtil.getCellRef(sheet1, "O" + idx3).setCellValue(item.getTestItem());
                String testResult = (item.getTestResult() != null) ?
                        (item.getTestResult().equals("Y"))? "■ 양호   □ 양호하지않음" : "□ 양호   ■ 양호하지않음"
                        : "□ 양호   □ 양호하지않음";
                ExcelPoiUtil.getCellRef(sheet1, "Z" + idx3).setCellValue(testResult);
                idx3++;
            }

            //비고 A58
            ExcelPoiUtil.getCellRef(sheet1, "A58").setCellValue(master.getMemo());

            /** Sheet2 : 검체채취기준 **/
            Sheet sheet2 = workbook.getSheet("Sheet2");

            /*상단 정보영역*/
            //품목코드, 문서번호(JQP12-01), 문서버전(Rev.01), 제품명, 고객사, 제조일자, 제조번호, 표시량, 로트인쇄, 검사자
            String[] sheet2Info = { "AD1", "AD3", "AD4", "F6", "AD6", "F7", "R7", "AD7", "F8", "AD8" };

            for (int i = 0; i < sheet1Info.length; i++) {
                ExcelPoiUtil.getCellRef(sheet2, sheet2Info[i]).setCellValue(infoData[i]);
            }

            Boolean isSample = sampleData != null;

            //채취일자 Q29
            ExcelPoiUtil.getCellRef(sheet2, "Q29").setCellValue((isSample && sampleData.getSampleDate() != null) ? sampleData.getSampleDate() : "");

            int[] qtys = {
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty1() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty2() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty3() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty4() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty5() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty6() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty7() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty8() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty9() : 0
            };

            String[] sampleAddr = { "E37", "G37", "I37", "K37", "M37", "O37", "Q37", "W37", "AC37" };

            int sum = 0;
            for (int i = 0; i < sampleAddr.length; i++) {
                ExcelPoiUtil.getCellRef(sheet2, sampleAddr[i]).setCellValue(qtys[i]);
                sum += qtys[i];
            }
            //총 채취량 AD30
            ExcelPoiUtil.getCellRef(sheet2, "AD30").setCellValue(sum);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("공정검사서", excelContent.length, "xlsx");

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    public ResponseEntity<Resource> getExcelTypeC(QcProcTestMasterView master) throws Exception {

        String masterId = master.getQcProcTestMasterId();

        //코팅 공정
        List<QcProcTestMethod> coatingMethods = qcProcTestMethodService.getQcMethod(masterId, "QRC003");
        //충전 공정
        List<QcProcTestMethod> chargingMethods = qcProcTestMethodService.getQcMethod(masterId, "QRC004");
        //포장 공정
        List<QcProcTestMethod> packingMethod = qcProcTestMethodService.getQcMethod(masterId, "QRC005");

        //충전작업지시
        WorkOrderItemView coatingItem = workOrderItemViewService.getById(master.getCoatingId());
        //충전작업지시
        WorkOrderItemView chargingItem = workOrderItemViewService.getById(master.getChargingId());
        //포장작업지시
        WorkOrderItemView packingItem = workOrderItemViewService.getById(master.getPackingId());
        //WE115 data
        List<QcProcTestDetail> WE115_details = qcProcTestDetailService.getQcDetail(masterId, "WE115");
        //GA115 data
        List<QcProcTestDetail> GA115_details = qcProcTestDetailService.getQcDetail(masterId, "GA115");

        //CA515 line setting
        List<QcProcTestLineView> CA515_line = qcProcTestLineViewService.getQcDetailLines(masterId, "CA515");
        //CA515 data
        List<QcProcTestDetail> CA515_details = qcProcTestDetailService.getQcDetail(masterId, "CA515");

        //ES515 line setting
        List<QcProcTestLineView> ES515_line = qcProcTestLineViewService.getQcDetailLines(masterId, "ES515");
        //ES515 data
        List<QcProcTestDetail> ES515_details = qcProcTestDetailService.getQcDetail(masterId, "ES515");
        //sample data
        QcProcTestSample sampleData = qcProcTestSampleService.getOne(
                new QueryWrapper<QcProcTestSample>().eq("qc_proc_test_master_id", masterId));

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/qc_proc_test_type_c.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);

            //품목코드, 문서번호(JQP12-01), 문서버전(Rev.01), 제품명, 고객사, 제조일자, 제조번호, 표시량, 로트인쇄, 검사자
            String[] infoData = {
                    (master.getItemCd() != null) ? master.getItemCd() : "" // 품목코드
                    , "JQP12-01" // 문서번호(JQP12-01)
                    , "(Rev.02)" // 문서버전(Rev.01)
                    , (master.getItemName() != null) ? master.getItemName() : "" // 제품명
                    , (master.getCustomerName() != null) ? master.getCustomerName() : "" // 고객사
                    , (master.getProdDate() != null) ? sdf.format(master.getProdDate()) : "" // 제조일자
                    , (master.getProdNo() != null) ? master.getProdNo() : "" // 제조번호
                    , (master.getDisplayCapacity() != null) ? master.getDisplayCapacity() : "" // 표시량
                    , (master.getLotNo2() != null && !master.getLotNo2().equals("/")) ? master.getLotNo2() : (master.getLotNo() != null)? master.getLotNo() : "" //로트인쇄
                    , (master.getTestMember() != null) ? master.getTestMember() : "" //검사자
                    , (master.getWorkFlow() != null) ? master.getWorkFlow() : "" //포장공정도
            };

            /** Sheet1 : 코팅작업, 충전작업, 중량검사, 겔수량검사, 포장작업 **/
            Sheet sheet1 = workbook.getSheet("Sheet1");

            //비고 A58
            ExcelPoiUtil.getCellRef(sheet1, "A58").setCellValue(master.getMemo());

            /*상단 정보영역*/
            //품목코드, 문서번호(JQP12-01), 문서버전(Rev.01), 제품명, 고객사, 제조일자, 제조번호, 표시량, 로트인쇄, 검사자, 포장공정도
            String[] sheet1Info = {
                    "AD1" // 품목코드
                    , "AD3" // 문서번호(JQP12-01)
                    , "AD4" // 문서버전(Rev.01)
                    , "F6" // 제품명
                    , "AD6" // 고객사
                    , "F7" // 제조일자
                    , "R7" // 제조번호
                    , "AD7" // 표시량
                    , "F8" // 로트인쇄
                    , "AD8" // 검사자
                    , "A58" // 포장공정도
            };

            for (int i = 0; i < sheet1Info.length; i++) {
                ExcelPoiUtil.getCellRef(sheet1, sheet1Info[i]).setCellValue(infoData[i]);
            }

            /** 코팅작업  **/
            //작업일자 (시작일시)Q11
            String coating_startTime = (coatingItem.getWorkStartTime() != null)? sdf.format(coatingItem.getWorkStartTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "Q10").setCellValue(coating_startTime);
            //작업일자 (종료일시)X11
            String coating_endTime = (coatingItem.getWorkEndTime() != null)? sdf.format(coatingItem.getWorkEndTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "X10").setCellValue(coating_endTime);

            int idx1 = 12;
            for (QcProcTestMethod item : coatingMethods){
                ExcelPoiUtil.getCellRef(sheet1, "C" + idx1).setCellValue(item.getTestMethod());
                ExcelPoiUtil.getCellRef(sheet1, "O" + idx1).setCellValue(item.getTestItem());
                String testResult = (item.getTestResult() != null) ?
                        (item.getTestResult().equals("Y"))? "■ 양호   □ 양호하지않음" : "□ 양호   ■ 양호하지않음"
                        : "□ 양호   □ 양호하지않음";
                ExcelPoiUtil.getCellRef(sheet1, "Z" + idx1).setCellValue(testResult);
                idx1++;
            }

            /** 충전작업 **/
            //작업일자 (시작일시)Q17
            String charging_startTime = (chargingItem.getWorkStartTime() != null)? sdf.format(chargingItem.getWorkStartTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "Q17").setCellValue(charging_startTime);
            //작업일자 (종료일시)X17
            String charging_endTime = (chargingItem.getWorkEndTime() != null)? sdf.format(chargingItem.getWorkEndTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "X17").setCellValue(charging_endTime);

            int idx2 = 19;
            for (QcProcTestMethod item : chargingMethods){
                ExcelPoiUtil.getCellRef(sheet1, "C" + idx2).setCellValue(item.getTestMethod());
                ExcelPoiUtil.getCellRef(sheet1, "O" + idx2).setCellValue(item.getTestItem());
                String testResult = (item.getTestResult() != null) ?
                        (item.getTestResult().equals("Y"))? "■ 양호   □ 양호하지않음" : "□ 양호   ■ 양호하지않음"
                        : "□ 양호   □ 양호하지않음";
                ExcelPoiUtil.getCellRef(sheet1, "Z" + idx2).setCellValue(testResult);
                idx2++;
            }

            /* 중량검사 */
            //충전지시량 AD27

            ExcelPoiUtil.getCellRef(sheet1, "AD27").setCellValue(master.getChargingQtys());

            //todo 중량검사 15개
            // 검사시간, 수량, 검사결과 "□ 적합 □ 부적합", "■ 적합 □ 부적합", "□ 적합 ■ 부적합"
            String[] cell1 = { "B30", "B31", "B32", "B33", "B34", "M30", "M31", "M32", "M33", "M34", "Y30", "Y31", "Y32", "Y33", "Y34" };
            String[] cell2 = { "E30", "E31", "E32", "E33", "E34", "Q30", "Q31", "Q32", "Q33", "Q34", "AB30", "AB31", "AB32", "AB33", "AB34",};
            String[] cell3 = { "I30", "I31", "I32", "I33", "I34", "U30", "U31", "U32", "U33", "U34", "AF30", "AF31", "AF32", "AF33", "AF34",};

            for (int i = 0; i < cell1.length; i++) {
                QcProcTestDetail items = WE115_details.get(i);
                //검사시간
                String testTime = (items.getTestTime() != null && !items.getTestTime().equals(""))? items.getTestTime() : ":";
                ExcelPoiUtil.getCellRef(sheet1, cell1[i]).setCellValue(testTime);
                //수량
                String line1 = (items.getLine1() != null) ? items.getLine1().toString() + " g" : "g";
                ExcelPoiUtil.getCellRef(sheet1, cell2[i]).setCellValue(line1);
                //검사결과
                String passYn = (items.getPassYn() != null) ?
                        (items.getPassYn().equals("Y")) ? "■ 적합 □ 부적합" : "□ 적합 ■ 부적합"
                        : "□ 적합 □ 부적합";
                ExcelPoiUtil.getCellRef(sheet1, cell3[i]).setCellValue(passYn);
            }

            /** 겔수량검사 **/
            //충전 매 수 AD38
            ExcelPoiUtil.getCellRef(sheet1, "AD38").setCellValue(master.getChargingCnt());
            //todo 겔수량검사 15개
            // 검사시간, 수량, 검사결과 "□ 적합 □ 부적합", "■ 적합 □ 부적합", "□ 적합 ■ 부적합"
            String[] cell4 = { "B41", "B42", "B43", "B44", "B45", "M41", "M42", "M43", "M44", "M45", "Y41", "Y42", "Y43", "Y44", "Y45" };
            String[] cell5 = { "E41", "E42", "E43", "E44", "E45", "Q41", "Q42", "Q43", "Q44", "Q45", "AB41", "AB42", "AB43", "AB44", "AB45",};
            String[] cell6 = { "I41", "I42", "I43", "I44", "I45", "U41", "U42", "U43", "U44", "U45", "AF41", "AF42", "AF43", "AF44", "AF45",};

            for (int i = 0; i < cell1.length; i++) {
                QcProcTestDetail items = GA115_details.get(i);
                //검사시간
                String testTime = (items.getTestTime() != null && !items.getTestTime().equals(""))? items.getTestTime() : ":";
                ExcelPoiUtil.getCellRef(sheet1, cell4[i]).setCellValue(testTime);
                //수량
                String line1 = (items.getLine1() != null) ? items.getLine1().setScale(0).toString() + " 매" : "매";
                ExcelPoiUtil.getCellRef(sheet1, cell5[i]).setCellValue(line1);
                //검사결과
                String passYn = (items.getPassYn() != null) ?
                        (items.getPassYn().equals("Y")) ? "■ 적합 □ 부적합" : "□ 적합 ■ 부적합"
                        : "□ 적합 □ 부적합";
                ExcelPoiUtil.getCellRef(sheet1, cell6[i]).setCellValue(passYn);
            }

            /** 포장작업 **/
            //작업일자 (시작일시)Q48
            String packing_startTime = (packingItem.getWorkStartTime() != null)? sdf.format(packingItem.getWorkStartTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "Q48").setCellValue(packing_startTime);
            //작업일자 (종료일시)X48
            String packing_endTime = (packingItem.getWorkEndTime() != null)? sdf.format(packingItem.getWorkEndTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "X48").setCellValue(packing_endTime);

            int idx3 = 50;
            for (QcProcTestMethod item : packingMethod){
                ExcelPoiUtil.getCellRef(sheet1, "C" + idx3).setCellValue(item.getTestMethod());
                ExcelPoiUtil.getCellRef(sheet1, "O" + idx3).setCellValue(item.getTestItem());
                String testResult = (item.getTestResult() != null) ?
                        (item.getTestResult().equals("Y"))? "■ 양호   □ 양호하지않음" : "□ 양호   ■ 양호하지않음"
                        : "□ 양호   □ 양호하지않음";
                ExcelPoiUtil.getCellRef(sheet1, "Z" + idx3).setCellValue(testResult);
                idx3++;
            }

            /** Sheet2 : 검체채취기준 **/
            Sheet sheet2 = workbook.getSheet("Sheet2");

            /*상단 정보영역*/
            //품목코드, 문서번호(JQP12-01), 문서버전(Rev.01), 제품명, 고객사, 제조일자, 제조번호, 표시량, 로트인쇄, 검사자
            String[] sheet2Info = {
                    "AD1" //품목코드
                    , "AD3" //문서번호(JQP12-01)
                    , "AD4" //문서버전(Rev.01)
                    , "F6"  //제품명
                    , "AD6" // 고객사
                    , "F7"  //제조일자
                    , "R7"  //제조번호
                    , "AD7" // 표시량
                    , "F8"  // 로트인쇄
                    , "AD8" // 검사자
            };

            for (int i = 0; i < sheet2Info.length; i++) {
                ExcelPoiUtil.getCellRef(sheet2, sheet2Info[i]).setCellValue(infoData[i]);
            }

            Boolean isSample = sampleData != null;

            //채취일자 Q29
            ExcelPoiUtil.getCellRef(sheet2, "Q29").setCellValue((isSample && sampleData.getSampleDate() != null) ? sampleData.getSampleDate() : "");

            int[] qtys = {
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty1() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty2() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty3() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty4() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty5() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty6() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty7() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty8() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty9() : 0
            };

            String[] sampleAddr = { "E37", "G37", "I37", "K37", "M37", "O37", "Q37", "W37", "AC37" };

            int sum = 0;
            for (int i = 0; i < sampleAddr.length; i++) {
                ExcelPoiUtil.getCellRef(sheet2, sampleAddr[i]).setCellValue(qtys[i]);
                sum += qtys[i];
            }
            //총 채취량 AD30
            ExcelPoiUtil.getCellRef(sheet2, "AD30").setCellValue(sum);

            /** Sheet3 : 캡핑세기, 중량검사(에센스) **/
            Sheet sheet3 = workbook.getSheet("Sheet3");

            /** 상단 정보영역 **/
            //품목코드, 문서번호(JQP12-01), 문서버전(Rev.01), 제품명, 고객사, 제조일자, 제조번호, 표시량, 로트인쇄, 검사자
            String[] sheet3Info = { "AD1", "AD3", "AD4", "F6", "AD6", "F7", "R7", "AD7", "F8", "AD8" };
            for (int i = 0; i < sheet3Info.length; i++) {
                ExcelPoiUtil.getCellRef(sheet3, sheet3Info[i]).setCellValue(infoData[i]);
            }

            ////CA515_details ES515_details
            //CA515_line //ES515_line

            /** 캡핑세기(완제품) **/
            //측정범위
            ExcelPoiUtil.getCellRef(sheet3, "AD12").setCellValue(master.getCappingRange());

            String[] lineNameList = { "I14", "M14", "Q14", "U14", "Y14" };
            for (int i = 0; i < lineNameList.length; i++) {
                String lineName = (CA515_line.get(i).getLineName() !=null && !CA515_line.get(i).getLineName().equals("")) ?
                        CA515_line.get(i).getLineName() + " 라인" : "(     )라인";
                ExcelPoiUtil.getCellRef(sheet3, lineNameList[i]).setCellValue(lineName);
            }

            int idx4 = 15;
            for (QcProcTestDetail items : CA515_details) {
                //채취시간
                String testTime = (items.getTestTime() != null && !items.getTestTime().equals(""))? items.getTestTime() : ":";
                ExcelPoiUtil.getCellRef(sheet3, "E" + idx4).setCellValue(testTime);
                //라인1
                String line1 = (items.getLine1() != null) ? items.getLine1().toString() + " N.M" : "N.M";
                ExcelPoiUtil.getCellRef(sheet3, "I" + idx4).setCellValue(line1);
                //라인2
                String line2 = (items.getLine2() != null) ? items.getLine2().toString() + " N.M" : "N.M";
                ExcelPoiUtil.getCellRef(sheet3, "M" + idx4).setCellValue(line2);
                //라인3
                String line3 = (items.getLine3() != null) ? items.getLine3().toString() + " N.M" : "N.M";
                ExcelPoiUtil.getCellRef(sheet3, "Q" + idx4).setCellValue(line3);
                //라인4
                String line4 = (items.getLine4() != null) ? items.getLine4().toString() + " N.M" : "N.M";
                ExcelPoiUtil.getCellRef(sheet3, "U" + idx4).setCellValue(line4);
                //라인5
                String line5 = (items.getLine5() != null) ? items.getLine5().toString() + " N.M" : "N.M";
                ExcelPoiUtil.getCellRef(sheet3, "Y" + idx4).setCellValue(line5);
                //검사겳과
                String passYn = (items.getPassYn() != null) ?
                        (items.getPassYn().equals("Y")) ? "■ 적합   □ 부적합" : "□ 적합   ■ 부적합"
                        : "□ 적합   □ 부적합";
                ExcelPoiUtil.getCellRef(sheet3, "AC" + idx4).setCellValue(passYn);
                idx4++;
            }

            /** 중량검사(에센스) **/
            //충전량기준
            ExcelPoiUtil.getCellRef(sheet3, "AD33").setCellValue(master.getEssenceStd());

            String[] lineNameList2 = { "I35", "M35", "Q35", "U35", "Y35" };
            for (int i = 0; i < lineNameList2.length; i++) {
                String lineName = (ES515_line.get(i).getLineName() !=null && !ES515_line.get(i).getLineName().equals("")) ?
                        ES515_line.get(i).getLineName() + " 라인": "(     )라인";
                ExcelPoiUtil.getCellRef(sheet3, lineNameList2[i]).setCellValue(lineName);
            }

            int idx5 = 36;
            for (QcProcTestDetail items : ES515_details) {
                //채취시간
                String testTime = (items.getTestTime() != null && !items.getTestTime().equals(""))? items.getTestTime() : ":";
                ExcelPoiUtil.getCellRef(sheet3, "E" + idx5).setCellValue(testTime);
                //라인1
                String line1 = (items.getLine1() != null) ? items.getLine1().toString() + " g" : "g";
                ExcelPoiUtil.getCellRef(sheet3, "I" + idx5).setCellValue(line1);
                //라인2
                String line2 = (items.getLine2() != null) ? items.getLine2().toString() + " g" : "g";
                ExcelPoiUtil.getCellRef(sheet3, "M" + idx5).setCellValue(line2);
                //라인3
                String line3 = (items.getLine3() != null) ? items.getLine3().toString() + " g" : "g";
                ExcelPoiUtil.getCellRef(sheet3, "Q" + idx5).setCellValue(line3);
                //라인4
                String line4 = (items.getLine4() != null) ? items.getLine4().toString() + " g" : "g";
                ExcelPoiUtil.getCellRef(sheet3, "U" + idx5).setCellValue(line4);
                //라인5
                String line5 = (items.getLine5() != null) ? items.getLine5().toString() + " g" : "g";
                ExcelPoiUtil.getCellRef(sheet3, "Y" + idx5).setCellValue(line5);
                //검사겳과
                String passYn = (items.getPassYn() != null) ?
                        (items.getPassYn().equals("Y")) ? "■ 적합   □ 부적합" : "□ 적합   ■ 부적합"
                        : "□ 적합   □ 부적합";
                ExcelPoiUtil.getCellRef(sheet3, "AC" + idx5).setCellValue(passYn);
                idx5++;
            }

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("공정검사서", excelContent.length, "xlsx");

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }

    }

    public ResponseEntity<Resource> getExcelTypeD(QcProcTestMasterView master) throws Exception {

        String masterId = master.getQcProcTestMasterId();

        //코팅 공정
        List<QcProcTestMethod> coatingMethods = qcProcTestMethodService.getQcMethod(masterId, "QRC003");
        //충전 공정
        List<QcProcTestMethod> chargingMethods = qcProcTestMethodService.getQcMethod(masterId, "QRC004");
        //포장 공정
        List<QcProcTestMethod> packingMethod = qcProcTestMethodService.getQcMethod(masterId, "QRC005");
        //충전작업지시
        WorkOrderItemView coatingItem = workOrderItemViewService.getById(master.getCoatingId());
        //충전작업지시
        WorkOrderItemView chargingItem = workOrderItemViewService.getById(master.getChargingId());
        //포장작업지시
        WorkOrderItemView packingItem = workOrderItemViewService.getById(master.getPackingId());
        //WE613 line setting
        List<QcProcTestLineView> WE613_line = qcProcTestLineViewService.getQcDetailLines(masterId, "WE613");
        //WE613 data
        List<QcProcTestDetail> WE613_details = qcProcTestDetailService.getQcDetail(masterId, "WE613");
        //sample data
        QcProcTestSample sampleData = qcProcTestSampleService.getOne(
                new QueryWrapper<QcProcTestSample>().eq("qc_proc_test_master_id", masterId));

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/qc_proc_test_type_d.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);

            //품목코드, 문서번호(JQP12-01), 문서버전(Rev.01), 제품명, 고객사, 제조일자, 제조번호, 표시량, 로트인쇄, 검사자
            String[] infoData = {
                    (master.getItemCd() != null) ? master.getItemCd() : ""
                    , "JQP12-01"
                    , "(Rev.02)"
                    , (master.getItemName() != null) ? master.getItemName() : ""
                    , (master.getCustomerName() != null) ? master.getCustomerName() : ""
                    , (master.getProdDate() != null) ? sdf.format(master.getProdDate()) : ""
                    , (master.getProdNo() != null) ? master.getProdNo() : ""
                    , (master.getDisplayCapacity() != null) ? master.getDisplayCapacity() : ""
                    , (master.getLotNo2() != null && !master.getLotNo2().equals("/")) ? master.getLotNo2() : (master.getLotNo() != null)? master.getLotNo() : ""
                    , (master.getTestMember() != null) ? master.getTestMember() : ""
            };

            /** Sheet1 : 코팅작업, 충전작업, 중량검사, 겔수량검사, 포장작업 **/
            Sheet sheet1 = workbook.getSheet("Sheet1");

            /*상단 정보영역*/
            //품목코드, 문서번호(JQP12-01), 문서버전(Rev.01), 제품명, 고객사, 제조일자, 제조번호, 표시량, 로트인쇄, 검사자
            String[] sheet1Info = { "AK1", "AK3", "AK4", "F6", "AK6", "F7", "V7", "AK7", "F8", "AK8" };
            for (int i = 0; i < sheet1Info.length; i++) {
                ExcelPoiUtil.getCellRef(sheet1, sheet1Info[i]).setCellValue(infoData[i]);
            }
            /** 코팅작업  **/
            //작업일자 (시작일시)Q11
            String coating_startTime = (coatingItem.getWorkStartTime() != null)? sdf.format(coatingItem.getWorkStartTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "Q10").setCellValue(coating_startTime);
            //작업일자 (종료일시)X11
            String coating_endTime = (coatingItem.getWorkEndTime() != null)? sdf.format(coatingItem.getWorkEndTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "X10").setCellValue(coating_endTime);

            int idx1 = 12;
            for (QcProcTestMethod item : coatingMethods){
                ExcelPoiUtil.getCellRef(sheet1, "C" + idx1).setCellValue(item.getTestMethod());
                ExcelPoiUtil.getCellRef(sheet1, "O" + idx1).setCellValue(item.getTestItem());
                String testResult = (item.getTestResult() != null) ?
                        (item.getTestResult().equals("Y"))? "■ 양호   □ 양호하지않음" : "□ 양호   ■ 양호하지않음"
                        : "□ 양호   □ 양호하지않음";
                ExcelPoiUtil.getCellRef(sheet1, "AD" + idx1).setCellValue(testResult);
                idx1++;
            }

            /** 충전작업 **/
            //작업일자 (시작일시)Q17
            String charging_startTime = (chargingItem.getWorkStartTime() != null)? sdf.format(chargingItem.getWorkStartTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "Q17").setCellValue(charging_startTime);
            //작업일자 (종료일시)X17
            String charging_endTime = (chargingItem.getWorkEndTime() != null)? sdf.format(chargingItem.getWorkEndTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "X17").setCellValue(charging_endTime);

            int idx2 = 19;
            for (QcProcTestMethod item : chargingMethods){
                ExcelPoiUtil.getCellRef(sheet1, "C" + idx2).setCellValue(item.getTestMethod());
                ExcelPoiUtil.getCellRef(sheet1, "O" + idx2).setCellValue(item.getTestItem());
                String testResult = (item.getTestResult() != null) ?
                        (item.getTestResult().equals("Y"))? "■ 양호   □ 양호하지않음" : "□ 양호   ■ 양호하지않음"
                        : "□ 양호   □ 양호하지않음";
                ExcelPoiUtil.getCellRef(sheet1, "AD" + idx2).setCellValue(testResult);
                idx2++;
            }

            /** 중량검사 **/
            //충전지시량 AH27
            ExcelPoiUtil.getCellRef(sheet1, "AH27").setCellValue(master.getChargingQtys());

            String[] lineNameList = { "I29", "M29", "Q29", "U29", "Y29", "AC29" };

            for (int i = 0; i < lineNameList.length; i++) {
                String lineName = (WE613_line.get(i).getLineName() !=null && !WE613_line.get(i).getLineName().equals("")) ?
                        WE613_line.get(i).getLineName() + " 라인" : "(     )라인";
                ExcelPoiUtil.getCellRef(sheet1, lineNameList[i]).setCellValue(lineName);
            }

            int idx3 = 30;
            for (QcProcTestDetail items : WE613_details) {
                //채취시간
                String testTime = (items.getTestTime() != null && !items.getTestTime().equals(""))? items.getTestTime() : ":";
                ExcelPoiUtil.getCellRef(sheet1, "E" + idx3).setCellValue(testTime);
                //라인1
                String line1 = (items.getLine1() != null) ? items.getLine1().toString() + " g" : "g";
                ExcelPoiUtil.getCellRef(sheet1, "I" + idx3).setCellValue(line1);
                //라인2
                String line2 = (items.getLine2() != null) ? items.getLine2().toString() + " g" : "g";
                ExcelPoiUtil.getCellRef(sheet1, "M" + idx3).setCellValue(line2);
                //라인3
                String line3 = (items.getLine3() != null) ? items.getLine3().toString() + " g" : "g";
                ExcelPoiUtil.getCellRef(sheet1, "Q" + idx3).setCellValue(line3);
                //라인4
                String line4 = (items.getLine4() != null) ? items.getLine4().toString() + " g" : "g";
                ExcelPoiUtil.getCellRef(sheet1, "U" + idx3).setCellValue(line4);
                //라인5
                String line5 = (items.getLine5() != null) ? items.getLine5().toString() + " g" : "g";
                ExcelPoiUtil.getCellRef(sheet1, "Y" + idx3).setCellValue(line5);
                //라인5
                String line6 = (items.getLine6() != null) ? items.getLine6().toString() + " g" : "g";
                ExcelPoiUtil.getCellRef(sheet1, "AC" + idx3).setCellValue(line6);
                //검사겳과
                String passYn = (items.getPassYn() != null) ?
                        (items.getPassYn().equals("Y")) ? "■ 적합   □ 부적합(                 )" : "□ 적합   ■ 부적합(                 )"
                        : "□ 적합   □ 부적합(                 )";
                ExcelPoiUtil.getCellRef(sheet1, "AG" + idx3).setCellValue(passYn);
                idx3++;
            }

            /* 포장작업 */
            //작업일자 (시작일시)Q45
            String packing_startTime = (packingItem.getWorkStartTime() != null)? sdf.format(packingItem.getWorkStartTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "Q45").setCellValue(packing_startTime);
            //작업일자 (종료일시)X45
            String packing_endTime = (packingItem.getWorkEndTime() != null)? sdf.format(packingItem.getWorkEndTime()) : "";
            ExcelPoiUtil.getCellRef(sheet1, "X45").setCellValue(packing_endTime);

            int idx4 = 47;
            for (QcProcTestMethod item : packingMethod){
                ExcelPoiUtil.getCellRef(sheet1, "C" + idx4).setCellValue(item.getTestMethod());
                ExcelPoiUtil.getCellRef(sheet1, "O" + idx4).setCellValue(item.getTestItem());
                String testResult = (item.getTestResult() != null) ?
                        (item.getTestResult().equals("Y"))? "■ 양호   □ 양호하지않음" : "□ 양호   ■ 양호하지않음"
                        : "□ 양호   □ 양호하지않음";
                ExcelPoiUtil.getCellRef(sheet1, "AD" + idx4).setCellValue(testResult);
                idx4++;
            }
            //비고 A58
            ExcelPoiUtil.getCellRef(sheet1, "A55").setCellValue(master.getMemo());

            /** Sheet2 : 검체채취기준 **/
            Sheet sheet2 = workbook.getSheet("Sheet2");

            /*상단 정보영역*/
            //품목코드, 문서번호(JQP12-01), 문서버전(Rev.01), 제품명, 고객사, 제조일자, 제조번호, 표시량, 로트인쇄, 검사자
            String[] sheet2Info = { "AD1", "AD3", "AD4", "F6", "AD6", "F7", "R7", "AD7", "F8", "AD8" };

            for (int i = 0; i < sheet1Info.length; i++) {
                ExcelPoiUtil.getCellRef(sheet2, sheet2Info[i]).setCellValue(infoData[i]);
            }

            Boolean isSample = sampleData != null;

            //채취일자 Q29
            ExcelPoiUtil.getCellRef(sheet2, "Q29").setCellValue((isSample && sampleData.getSampleDate() != null) ? sampleData.getSampleDate() : "");

            int[] qtys = {
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty1() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty2() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty3() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty4() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty5() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty6() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty7() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty8() : 0,
                    (isSample && sampleData.getQty1() != null)? sampleData.getQty9() : 0
            };

            String[] sampleAddr = { "E37", "G37", "I37", "K37", "M37", "O37", "Q37", "W37", "AC37" };

            int sum = 0;
            for (int i = 0; i < sampleAddr.length; i++) {
                ExcelPoiUtil.getCellRef(sheet2, sampleAddr[i]).setCellValue(qtys[i]);
                sum += qtys[i];
            }
            //총 채취량 AD30
            ExcelPoiUtil.getCellRef(sheet2, "AD30").setCellValue(sum);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("공정검사서", excelContent.length, "xlsx");

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }
}
