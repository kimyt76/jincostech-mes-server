package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.common.code.ProcessCd;
import com.daehanins.mes.biz.pub.entity.*;
import com.daehanins.mes.biz.pub.mapper.ProcTestMasterMapper;
import com.daehanins.mes.biz.pub.service.IProcTestEquipService;
import com.daehanins.mes.biz.pub.service.IProcTestMasterService;
import com.daehanins.mes.biz.pub.service.IProcTestMethodService;
import com.daehanins.mes.biz.pub.service.IProcTestWorkerService;
import com.daehanins.mes.biz.pub.vo.ProcTestWithItems;
import com.daehanins.mes.biz.work.entity.MatUseEtcView;
import com.daehanins.mes.biz.work.entity.ProdRecord;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;
import com.daehanins.mes.biz.work.service.IWorkOrderItemViewService;
import com.daehanins.mes.biz.work.vo.WorkOrderItemWithRecord;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * ProcTestMaster 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2023-03-14
 */
@Service
public class ProcTestMasterServiceImpl extends ServiceImpl<ProcTestMasterMapper, ProcTestMaster> implements IProcTestMasterService {

    @Autowired
    private IWorkOrderItemViewService workOrderItemViewService;

    @Autowired
    private IProcTestMethodService procTestMethodService;

    @Autowired
    private IProcTestEquipService procTestEquipService;

    @Autowired
    private IProcTestWorkerService procTestWorkerService;

    public ProcTestMaster getByWorkOrderItemId (String id) {
        return this.getOne(new QueryWrapper<ProcTestMaster>().eq("work_order_item_id", id));
    }

    /** 공정검사서 엑셀 다운로드 **/
    public ResponseEntity<Resource> getProcTestExcel(String workOrderItemId) throws Exception {

        //작업지시, 공정검사, 검사항목, 사용설비, 작업자 조회
        WorkOrderItemView workOrderItemView = workOrderItemViewService.getById(workOrderItemId);
        ProcTestMaster procTestMaster = this.getByWorkOrderItemId(workOrderItemId);
        List<ProcTestMethod> methodList = procTestMethodService.getByMasterId(procTestMaster.getProcTestMasterId());
        List<ProcTestEquip> equipList = procTestEquipService.getByMasterId(procTestMaster.getProcTestMasterId());
        List<ProcTestWorker> workerList = procTestWorkerService.getByMasterId(procTestMaster.getProcTestMasterId());

        String processCd = workOrderItemView.getProcessCd();
        String tempType = (processCd.equals(ProcessCd.충전) || processCd.equals(ProcessCd.포장)) ? "B" : "A";

        try {
            // 공정 별 양식을 로드
            InputStream excelStream = getClass().getResourceAsStream(getTemplatePathByProcessCd(processCd));
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            Sheet sheet = workbook.getSheet("Sheet1");

            /** 상단 part **/
            ExcelPoiUtil.getCellRef(sheet, "AF1").setCellValue(workOrderItemView.getItemCd()); //품목코드
            ExcelPoiUtil.getCellRef(sheet, "F6").setCellValue(workOrderItemView.getItemName());  //품목명
            ExcelPoiUtil.getCellRef(sheet, "AF6").setCellValue(workOrderItemView.getCustomerName());  //고객사

            String prodDateFormat = (workOrderItemView.getProdDate() != null)
                    ? workOrderItemView.getProdDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "-";
            ExcelPoiUtil.getCellRef(sheet, "F7").setCellValue(prodDateFormat);  //제조일자
            ExcelPoiUtil.getCellRef(sheet, "S7").setCellValue(workOrderItemView.getProdNo());  //제조번호

            double orderQty = (workOrderItemView.getOrderQty() != null )? workOrderItemView.getOrderQty().doubleValue() : 0;
            ExcelPoiUtil.getCellRef(sheet, "AF7").setCellValue(orderQty);  //제조량

            if(tempType.equals("B")){
                ExcelPoiUtil.getCellRef(sheet, "F8").setCellValue(workOrderItemView.getLotNo());  //로트번호
                ExcelPoiUtil.getCellRef(sheet, "S8").setCellValue("");  //제품유형
                ExcelPoiUtil.getCellRef(sheet, "AF8").setCellValue("");  //용량
            }

            int commonIdx = (tempType.equals("A"))? 9 : 10;
            String startDateFormat = (workOrderItemView.getWorkStartTime() != null)
                    ? workOrderItemView.getWorkStartTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")) : "";
            String endDateFormat = (workOrderItemView.getWorkEndTime() != null)
                    ? workOrderItemView.getWorkEndTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")) : "";
            ExcelPoiUtil.getCellRef(sheet, "P" + commonIdx).setCellValue(startDateFormat);  //시작일시
            ExcelPoiUtil.getCellRef(sheet, "W" + commonIdx).setCellValue(endDateFormat);  //종료일시
            ExcelPoiUtil.getCellRef(sheet, "AI" + commonIdx).setCellValue(procTestMaster.getChargeMember());  //점검자

            /** 공정검사 그리드 part **/
            int methodIdx = (tempType.equals("A"))? 11 : 12;
            for (ProcTestMethod item : methodList) {
                ExcelPoiUtil.getCellRef(sheet, "C" + methodIdx).setCellValue(item.getTestItem());  //검사항목
                ExcelPoiUtil.getCellRef(sheet, "P" + methodIdx).setCellValue(item.getTestMethod());  //검사방법
                ExcelPoiUtil.getCellRef(sheet, "T" + methodIdx).setCellValue(item.getTestTiming());  //점검시기
                ExcelPoiUtil.getCellRef(sheet, "Y" + methodIdx).setCellValue(item.getTestTime());  //점검시간

                String testResult = "□ 적합   □ 부적합";
                if(item.getTestResult() != null && item.getTestResult().equals("Y")) {
                    testResult = "■ 적합   □ 부적합";
                } else if (item.getTestResult() != null && item.getTestResult().equals("N")) {
                    testResult = "□ 적합   ■ 부적합";
                }

                ExcelPoiUtil.getCellRef(sheet, "AC" + methodIdx).setCellValue(testResult);  //점검결과
                methodIdx++;
            }

            /** 사용설비 그리드 part **/
            int equipIdx = (tempType.equals("A"))? 25 : 26;
            for (ProcTestEquip item : equipList) {
                ExcelPoiUtil.getCellRef(sheet, "C" + equipIdx).setCellValue(item.getEquipmentName());  //설비명
                ExcelPoiUtil.getCellRef(sheet, ((processCd.equals(ProcessCd.칭량))? "G" : "J") + equipIdx).setCellValue(item.getEquipmentCd());  //설비코드
                equipIdx++;
            }

            /** 작업인원 그리드 part **/
            if(processCd.equals(ProcessCd.칭량)) {
                int workerIdxA = 25;
                int workerIdxB = 29;

                for (ProcTestWorker item : workerList) {
                    if(item.getGb().equals("분말")) {
                        ExcelPoiUtil.getCellRef(sheet, "AF" + workerIdxA).setCellValue(item.getMemberCd());  //작업자명
                        workerIdxA++;
                    } else {
                        ExcelPoiUtil.getCellRef(sheet, "AF" + workerIdxB).setCellValue(item.getMemberCd());  //작업자명
                        workerIdxB++;
                    }
                }
            } else {
                int workerIdx = (tempType.equals("A"))? 25 : 26;
                for (ProcTestWorker item : workerList) {
                    ExcelPoiUtil.getCellRef(sheet, "AF" + workerIdx).setCellValue(item.getMemberCd());  //작업자명
                    workerIdx++;
                }
            }

            /** 작업환경 part **/
            double temperature1 = (procTestMaster.getTemperature1() != null )? procTestMaster.getTemperature1().doubleValue() : 0;
            double humidity1 = (procTestMaster.getHumidity1() != null )? procTestMaster.getHumidity1().doubleValue() : 0;
            ExcelPoiUtil.getCellRef(sheet, (tempType.equals("A"))? "X25" : "X26").setCellValue(temperature1); //온도
            ExcelPoiUtil.getCellRef(sheet, (tempType.equals("A"))? "X26" : "X27").setCellValue(humidity1); //습도

            if(processCd.equals(ProcessCd.칭량)) {
                double temperature2 = (procTestMaster.getTemperature2() != null )? procTestMaster.getTemperature2().doubleValue() : 0;
                double humidity2 = (procTestMaster.getHumidity2() != null )? procTestMaster.getHumidity2().doubleValue() : 0;
                ExcelPoiUtil.getCellRef(sheet, "X30").setCellValue(temperature2); //온도2
                ExcelPoiUtil.getCellRef(sheet, "X31").setCellValue(humidity2); //습도2
            }

            if(processCd.equals(ProcessCd.포장)) {
                ExcelPoiUtil.getCellRef(sheet, "S30").setCellValue(procTestMaster.getLotMark()); //로트번호
                ExcelPoiUtil.getCellRef(sheet, "V31").setCellValue(procTestMaster.getLotLocation1()); //착인위치 용기
                ExcelPoiUtil.getCellRef(sheet, "V32").setCellValue(procTestMaster.getLotLocation2()); //착인위치 단상자
            }

            /** 특이사항 part **/
            String noteIdx = (tempType.equals("A"))? "A34" : "A35";
            ExcelPoiUtil.getCellRef(sheet, noteIdx).setCellValue(procTestMaster.getNote());

            //Footer footer = sheet1.getFooter()
//            Footer footer =  sheet.getFooter();
//            footer.setLeft("회사명or 프로젝트명");

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("공정검사서", excelContent.length, "xlsx");

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    public String getTemplatePathByProcessCd (String processCd) {
        String templatePath = "";

        //각 공정에 해당하는 양식 설정
        switch (processCd) {
            case ProcessCd.칭량:
                templatePath = "/excel/proc_test_temp_PRC001.xlsx";
                break;
            case ProcessCd.제조:
                templatePath = "/excel/proc_test_temp_PRC002.xlsx";
                break;
            case ProcessCd.코팅:
                templatePath = "/excel/proc_test_temp_PRC003.xlsx";
                break;
            case ProcessCd.충전:
                templatePath = "/excel/proc_test_temp_PRC004.xlsx";
                break;
            case ProcessCd.포장:
                templatePath = "/excel/proc_test_temp_PRC005.xlsx";
                break;
            default:
                break;
        }
        return templatePath;
    }

}
