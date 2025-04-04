package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.work.entity.*;
import com.daehanins.mes.biz.work.mapper.ProdProcessMapper;
import com.daehanins.mes.biz.work.service.*;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * ProdProcess 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2023-02-17
 */
@Service
public class ProdProcessServiceImpl extends ServiceImpl<ProdProcessMapper, ProdProcess> implements IProdProcessService {

    @Autowired
    private IWorkOrderItemViewService workOrderItemViewService;

    @Autowired
    private IMatUseViewService matUseViewService;

    public ResponseEntity<Resource> getProdWeighExcel (String workOrderItemId) throws Exception {

        WorkOrderItemView workOrderItemView = workOrderItemViewService.getById(workOrderItemId);

        List<MatUseView> matUseList = matUseViewService.list(
                new QueryWrapper<MatUseView>()
                        .eq("work_order_item_id", workOrderItemId)
                        .orderByAsc("prod_state", "ser_no"));

        try {
            int size = matUseList.size();
            String templateName = "/excel/prod_weigh_record_page";
            if(size > 56) {
                templateName += "3.xlsx";
            } else if (size > 25) {
                templateName += "2.xlsx";
            } else {
                templateName += "1.xlsx";
            }

            InputStream excelStream = getClass().getResourceAsStream(templateName);
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            Sheet sheet = workbook.getSheet("Sheet1");

            ExcelPoiUtil.getCellRef(sheet, "U1").setCellValue(workOrderItemView.getItemCd()); //품목코드
            ExcelPoiUtil.getCellRef(sheet, "F2").setCellValue(workOrderItemView.getItemName()); //품목명
            ExcelPoiUtil.getCellRef(sheet, "E5").setCellValue(workOrderItemView.getCustomerName()); //고객사
            if(workOrderItemView.getProdDate() != null) {
                String prodDateFormat = workOrderItemView.getProdDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                ExcelPoiUtil.getCellRef(sheet, "Q5").setCellValue(prodDateFormat); //제조일자
            }
            double orderQty = (workOrderItemView.getOrderQty() != null )? workOrderItemView.getOrderQty().doubleValue() : 0;
            ExcelPoiUtil.getCellRef(sheet, "E6").setCellValue(orderQty); //제조량
            ExcelPoiUtil.getCellRef(sheet, "Q6").setCellValue(workOrderItemView.getProdNo()); //제조번호

            int rowNo = 11;
            List<String> weighMembers = new ArrayList<>();
            List<String> weighConfirmMembers = new ArrayList<>();

            for (MatUseView item : matUseList) {
                //품목코드
                ExcelPoiUtil.getCellRef(sheet, "B"+rowNo).setCellValue(item.getItemCd());
                //품목명
                ExcelPoiUtil.getCellRef(sheet, "E"+rowNo).setCellValue(item.getItemName());
                //상 구분
                ExcelPoiUtil.getCellRef(sheet, "K"+rowNo).setCellValue(item.getProdState());
                //칭량지시량
                double reqQty = (item.getReqQty() != null )? item.getReqQty().doubleValue() : 0;
                ExcelPoiUtil.getCellRef(sheet, "N"+rowNo).setCellValue(reqQty);
                //기준량(함량%)
                double contentRatio = reqQty/orderQty * 100;
                ExcelPoiUtil.getCellRef(sheet, "L"+rowNo).setCellValue(contentRatio);
                //시험번호
                ExcelPoiUtil.getCellRef(sheet, "Q"+rowNo).setCellValue(item.getTestNoJoin());
                //실칭량량
                double weighQty = (item.getWeighQty() != null )? item.getWeighQty().doubleValue() : 0;
                ExcelPoiUtil.getCellRef(sheet, "T"+rowNo).setCellValue(weighQty);
                //작업자
                ExcelPoiUtil.getCellRef(sheet, "W"+rowNo).setCellValue(item.getWeighMemberCd());

                if(item.getWeighMemberCd() != null) weighMembers.add(item.getWeighMemberCd());
                if(item.getWeighConfirmMemberCd() != null) weighConfirmMembers.add(item.getWeighConfirmMemberCd());

                rowNo++;
            }

            ExcelPoiUtil.getCellRef(sheet, "E7").setCellValue(distinctAndJoining(weighMembers)); //작업자
            ExcelPoiUtil.getCellRef(sheet, "Q7").setCellValue(distinctAndJoining(weighConfirmMembers)); //확인자
            //L36, N36, T36
            if(templateName.equals("/excel/prod_weigh_record_page1.xlsx")){
                ExcelPoiUtil.getCellRef(sheet, "L36").setCellType(HSSFCell.CELL_TYPE_FORMULA);
                ExcelPoiUtil.getCellRef(sheet, "L36").setCellFormula("SUM(L11:M35)");
                ExcelPoiUtil.getCellRef(sheet, "N36").setCellType(HSSFCell.CELL_TYPE_FORMULA);
                ExcelPoiUtil.getCellRef(sheet, "N36").setCellFormula("SUM(N11:P35)");
                ExcelPoiUtil.getCellRef(sheet, "T36").setCellType(HSSFCell.CELL_TYPE_FORMULA);
                ExcelPoiUtil.getCellRef(sheet, "T36").setCellFormula("SUM(T11:V35)");
            } else if(templateName.equals("/excel/prod_weigh_record_page2.xlsx")){
                ExcelPoiUtil.getCellRef(sheet, "L67").setCellType(HSSFCell.CELL_TYPE_FORMULA);
                ExcelPoiUtil.getCellRef(sheet, "L67").setCellFormula("SUM(L11:M66)");
                ExcelPoiUtil.getCellRef(sheet, "N67").setCellType(HSSFCell.CELL_TYPE_FORMULA);
                ExcelPoiUtil.getCellRef(sheet, "N67").setCellFormula("SUM(N11:P66)");
                ExcelPoiUtil.getCellRef(sheet, "T67").setCellType(HSSFCell.CELL_TYPE_FORMULA);
                ExcelPoiUtil.getCellRef(sheet, "T67").setCellFormula("SUM(T11:V66)");
            } else {
                ExcelPoiUtil.getCellRef(sheet, "L98").setCellType(HSSFCell.CELL_TYPE_FORMULA);
                ExcelPoiUtil.getCellRef(sheet, "L98").setCellFormula("SUM(L11:M97)");
                ExcelPoiUtil.getCellRef(sheet, "N98").setCellType(HSSFCell.CELL_TYPE_FORMULA);
                ExcelPoiUtil.getCellRef(sheet, "N98").setCellFormula("SUM(N11:P97)");
                ExcelPoiUtil.getCellRef(sheet, "T98").setCellType(HSSFCell.CELL_TYPE_FORMULA);
                ExcelPoiUtil.getCellRef(sheet, "T98").setCellFormula("SUM(T11:V97)");
            }


            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("prod_proc_list", excelContent.length, "xlsx");

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    public ResponseEntity<Resource> getProdProcExcel (String workOrderItemId) throws Exception {

        WorkOrderItemView workOrderItemView = workOrderItemViewService.getById(workOrderItemId);
        List<ProdProcess> prodProcess = this.list(
                new QueryWrapper<ProdProcess>().eq("work_order_item_id", workOrderItemId).orderByAsc("prod_order")
        );

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/prod_proc_record.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            Sheet sheet = workbook.getSheet("Sheet1");


            ExcelPoiUtil.getCellRef(sheet, "U1").setCellValue(workOrderItemView.getItemCd()); //품목코드
            ExcelPoiUtil.getCellRef(sheet, "F2").setCellValue(workOrderItemView.getItemName()); //품목명
            ExcelPoiUtil.getCellRef(sheet, "E5").setCellValue(workOrderItemView.getCustomerName()); //고객사
            if(workOrderItemView.getProdDate() != null) {
                String prodDateFormat = workOrderItemView.getProdDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                ExcelPoiUtil.getCellRef(sheet, "Q5").setCellValue(prodDateFormat); //제조일자
            }
            double prodQty = (workOrderItemView.getProdQty() != null )? workOrderItemView.getProdQty().doubleValue() : 0;
            ExcelPoiUtil.getCellRef(sheet, "E6").setCellValue(prodQty); //제조량
            ExcelPoiUtil.getCellRef(sheet, "Q6").setCellValue(workOrderItemView.getProdNo()); //제조번호

            int rowNo = 11;     // 출력시작 Row,  POI row는 0부터 시작
            List<String> prodMembers = new ArrayList<>();
            List<String> prodConfirmMembers = new ArrayList<>();

            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 24);

            for (ProdProcess item : prodProcess ) {
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(item.getProdState());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3));
                sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 3));

                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(item.getDetail());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 10, cellStyleList.get(10));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 11, cellStyleList.get(11));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 12, cellStyleList.get(12));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 13, cellStyleList.get(13));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 14, cellStyleList.get(14));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 15, cellStyleList.get(15));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 16, cellStyleList.get(16));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 17, cellStyleList.get(17));
                sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 4, 17));

                ExcelPoiUtil.getStyleCell(sheet, rowNo, 18, cellStyleList.get(18)).setCellValue(item.getProdStartTime());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 19, cellStyleList.get(19));
                sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 18, 19));

                ExcelPoiUtil.getStyleCell(sheet, rowNo, 20, cellStyleList.get(20)).setCellValue(item.getProdEndTime());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 21, cellStyleList.get(21));
                sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 20, 21));

                ExcelPoiUtil.getStyleCell(sheet, rowNo, 22, cellStyleList.get(22)).setCellValue(item.getProdMember());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 23, cellStyleList.get(23));
                sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 22, 23));

                if(item.getProdMember() != null) prodMembers.add(item.getProdMember());
                if(item.getConfirmMember() != null) prodConfirmMembers.add(item.getConfirmMember());
                rowNo++;
            }
            ExcelPoiUtil.getCellRef(sheet, "E7").setCellValue(distinctAndJoining(prodMembers)); //작업자
            ExcelPoiUtil.getCellRef(sheet, "Q7").setCellValue(distinctAndJoining(prodConfirmMembers)); //확인자

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("prod_proc_list", excelContent.length, "xlsx");

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    public ResponseEntity<Resource> getProdInputExcel (String workOrderItemId) throws Exception {

        WorkOrderItemView workOrderItemView = workOrderItemViewService.getById(workOrderItemId);

        WorkOrderItemView weighWorkOrderItem = workOrderItemViewService.getOne(
                new QueryWrapper<WorkOrderItemView>().eq("work_order_batch_id", workOrderItemView.getWorkOrderBatchId())
                        .eq("process_cd", "PRC001")
        );
        List<MatUseView> matUseList = matUseViewService.list(
                new QueryWrapper<MatUseView>().eq("work_order_item_id", weighWorkOrderItem.getWorkOrderItemId())
                        .orderByAsc("prod_state", "ser_no")
        );

        try {
            int size = matUseList.size();
            String templateName = "/excel/prod_input_record_page";
            if(size > 53) {
                templateName += "3.xlsx";
            } else if (size > 24) {
                templateName += "2.xlsx";
            } else {
                templateName += "1.xlsx";
            }

            InputStream excelStream = getClass().getResourceAsStream(templateName);
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            Sheet sheet = workbook.getSheet("Sheet1");

            ExcelPoiUtil.getCellRef(sheet, "U1").setCellValue(workOrderItemView.getItemCd()); //품목코드
            ExcelPoiUtil.getCellRef(sheet, "F2").setCellValue(workOrderItemView.getItemName()); //품목명
            ExcelPoiUtil.getCellRef(sheet, "E5").setCellValue(workOrderItemView.getCustomerName()); //고객사
            if(workOrderItemView.getProdDate() != null) {
                String prodDateFormat = workOrderItemView.getProdDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                ExcelPoiUtil.getCellRef(sheet, "Q5").setCellValue(prodDateFormat); //제조일자
            }
            double prodQty = (workOrderItemView.getProdQty() != null )? workOrderItemView.getProdQty().doubleValue() : 0;
            ExcelPoiUtil.getCellRef(sheet, "E6").setCellValue(prodQty); //제조량
            ExcelPoiUtil.getCellRef(sheet, "Q6").setCellValue(workOrderItemView.getProdNo()); //제조번호

            int rowNo = 11;
            List<String> prodMembers = new ArrayList<>();
            List<String> prodConfirmMembers = new ArrayList<>();

            for (MatUseView item : matUseList) {
                ExcelPoiUtil.getCellRef(sheet, "C"+rowNo).setCellValue(item.getItemCd());
                ExcelPoiUtil.getCellRef(sheet, "F"+rowNo).setCellValue(item.getItemName());
                double weighQty = (item.getWeighQty() != null )? item.getWeighQty().doubleValue() : 0;
                ExcelPoiUtil.getCellRef(sheet, "P"+rowNo).setCellValue(weighQty);
                ExcelPoiUtil.getCellRef(sheet, "T"+rowNo).setCellValue(item.getProdState());
                ExcelPoiUtil.getCellRef(sheet, "V"+rowNo).setCellValue(item.getProdMemberCd());
                if(item.getProdMemberCd() != null) prodMembers.add(item.getProdMemberCd());
                if(item.getProdConfirmMemberCd() != null) prodConfirmMembers.add(item.getProdConfirmMemberCd());
                rowNo++;
            }
            ExcelPoiUtil.getCellRef(sheet, "E7").setCellValue(distinctAndJoining(prodMembers)); //작업자
            ExcelPoiUtil.getCellRef(sheet, "Q7").setCellValue(distinctAndJoining(prodConfirmMembers)); //확인자

            if(templateName.equals("/excel/prod_input_record_page1.xlsx")){
                ExcelPoiUtil.getCellRef(sheet, "P35").setCellType(HSSFCell.CELL_TYPE_FORMULA);
                ExcelPoiUtil.getCellRef(sheet, "P35").setCellFormula("SUM(P11:S34)");
            } else if(templateName.equals("/excel/prod_input_record_page2.xlsx")){
                ExcelPoiUtil.getCellRef(sheet, "P64").setCellType(HSSFCell.CELL_TYPE_FORMULA);
                ExcelPoiUtil.getCellRef(sheet, "P64").setCellFormula("SUM(P11:S63)");
            } else {
                ExcelPoiUtil.getCellRef(sheet, "P93").setCellType(HSSFCell.CELL_TYPE_FORMULA);
                ExcelPoiUtil.getCellRef(sheet, "P93").setCellFormula("SUM(P11:S92)");
            }

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("prod_proc_list", excelContent.length, "xlsx");

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    public String distinctAndJoining (List<String> stringList) {
        return stringList.stream().distinct().collect(Collectors.joining(", "));
    }

}
