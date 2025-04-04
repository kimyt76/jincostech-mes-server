package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.common.code.*;
import com.daehanins.mes.biz.common.util.BizDateUtil;
import com.daehanins.mes.biz.common.vo.LabelPrint;
import com.daehanins.mes.biz.mat.entity.MatTran;
import com.daehanins.mes.biz.mat.entity.MatTranItem;
import com.daehanins.mes.biz.mat.service.IMatTranItemService;
import com.daehanins.mes.biz.mat.service.IMatTranItemViewService;
import com.daehanins.mes.biz.mat.service.IMatTranService;
import com.daehanins.mes.biz.mat.service.IMatTranViewService;
import com.daehanins.mes.biz.mat.vo.MatTranSaveWithItems;
import com.daehanins.mes.biz.pub.entity.ItemMaster;
import com.daehanins.mes.biz.pub.entity.ItemMasterView;
import com.daehanins.mes.biz.pub.entity.Storage;
import com.daehanins.mes.biz.pub.service.IItemMasterService;
import com.daehanins.mes.biz.pub.service.IItemMasterViewService;
import com.daehanins.mes.biz.pub.service.IStorageService;
import com.daehanins.mes.biz.qt.entity.ItemTestNo;
import com.daehanins.mes.biz.qt.entity.ItemTestNoView;
import com.daehanins.mes.biz.qt.entity.QualityTest;
import com.daehanins.mes.biz.qt.service.IItemTestNoService;
import com.daehanins.mes.biz.qt.service.IItemTestNoViewService;
import com.daehanins.mes.biz.qt.service.IQualityTestService;
import com.daehanins.mes.biz.tag.vo.EquipRunTagValue;
import com.daehanins.mes.biz.tag.vo.EquipRunVo;
import com.daehanins.mes.biz.work.entity.*;
import com.daehanins.mes.biz.work.mapper.WorkOrderItemMapper;
import com.daehanins.mes.biz.work.service.*;
import com.daehanins.mes.biz.work.vo.*;
import com.daehanins.mes.common.utils.AuthUtil;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import com.daehanins.mes.common.utils.JasperUtil;
import com.daehanins.mes.common.utils.SearchUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 작업지시상세WorkOrderItem 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-28
 */
@Service
public class WorkOrderItemServiceImpl extends ServiceImpl<WorkOrderItemMapper, WorkOrderItem> implements IWorkOrderItemService {

    @Autowired
    private IMatTranService matTranService;

    @Autowired
    private IMatTranItemService matTranItemService;

    @Autowired
    private IMatWeighViewService matWeighViewService;

    @Autowired
    private IMatUseService matUseService;

    @Autowired
    private IMatUseEtcService matUseEtcService;

    @Autowired
    private IMatUseEtcViewService matUseEtcViewService;

    @Autowired
    private IProdIngService prodIngService;

    @Autowired
    private IProdRecordService prodRecordService;

    @Autowired
    private IWorkOrderItemViewService workOrderItemViewService;

    @Autowired
    private IItemMasterViewService itemMasterViewService;

    @Autowired
    private IItemTestNoService itemTestNoService;

    @Autowired
    private IQualityTestService qualityTestService;

    @Autowired
    private IItemTestNoViewService itemTestNoViewService;


    /** 배치ID 별 작업지시 조회 **/
    public List<WorkOrderItem> getByBatchId (String workOrderBatchId) {
        QueryWrapper<WorkOrderItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.camelToUnderline("workOrderBatchId"), workOrderBatchId);
        List<WorkOrderItem> workOrderItems = this.list(queryWrapper);
        return workOrderItems;
    }

    /** 코팅, 충전, 포장 작업지시 상세 조회 **/
    public WorkOrderItemWithRecord getWorkOrderItemWithRecord (String workOrderItemId) {

        WorkOrderItemView workOrderItemView = workOrderItemViewService.getById(workOrderItemId);
        ItemMasterView itemMasterView = itemMasterViewService.getById(workOrderItemView.getItemCd());

        List<MatUseEtcView> matUseEtcList = matUseEtcViewService.list(
                new QueryWrapper<MatUseEtcView>().eq("work_order_item_id", workOrderItemId)
                                                 .orderByAsc("mat_use_etc_id")
        );

        List<ProdRecord> prodRecordList = prodRecordService.list(
                new QueryWrapper<ProdRecord>().eq("work_order_item_id", workOrderItemId)
                                              .orderByAsc("prod_date")
                                              .orderByAsc("prod_start_time")
        );

        WorkOrderItemWithRecord result = new WorkOrderItemWithRecord();
        result.setWorkOrderItemInfo(workOrderItemView);
        result.setItemInfo(itemMasterView);
        result.setMatUseEtcList(matUseEtcList);
        result.setProdRecordList(prodRecordList);

        return result;
    }

    /** 신규 작업지시 생성 및 공정별 연계 **/
    @Transactional
    public boolean saveWorkOrderItem (WorkOrderItem workOrderItem) throws Exception {

        String workOrderItemId = workOrderItem.getWorkOrderItemId();
        workOrderItem.setWorkOrderItemStatus(WorkOrderItemStatus.작업지시);

        boolean result = this.save(workOrderItem);
        //작업지시 저장
        if ( result ) {
            switch ( workOrderItem.getProcessCd() ) {
                case ProcessCd.칭량 :
                    //matUse 생성
                    matUseService.saveByWorkOrderItem(workOrderItem);
                    break;
                case ProcessCd.제조 :
                    //prodIng 생성
                    prodIngService.saveByWorkOrderItemId(workOrderItemId, workOrderItem.getWorkOrderBatchId());
                    break;
                case ProcessCd.코팅:
                case ProcessCd.충전:
                case ProcessCd.포장:
                    //matUseEtc 생성
                    matUseEtcService.saveByWorkOrderItem(workOrderItem);
                    break;
                default: break;
            }
        }
        return result;
    }
    
    /** 작업지시 수정 (작업지시 상태에서) */
    @Transactional
    public boolean updateWorkOrderItem (WorkOrderItem workOrderItem) throws Exception {

        String workOrderItemId = workOrderItem.getWorkOrderItemId();
        boolean result = this.saveOrUpdate(workOrderItem);
        //작업지시 수정
        if ( result ) {
            switch ( workOrderItem.getProcessCd() ) {
                case ProcessCd.칭량 :
                    //기존 matUse 삭제, 변경된 내용으로 새로 생성.
                    matUseService.deleteByWorkOrderItemId(workOrderItemId);
                    matUseService.saveByWorkOrderItem(workOrderItem);
                    break;
                case ProcessCd.제조 :
                    //기존 prodIng 삭제, 변경된 내용으로 새로 생성.
                    prodIngService.deleteByWorkOrderItemId(workOrderItemId);
                    prodIngService.saveByWorkOrderItemId(workOrderItemId, workOrderItem.getWorkOrderBatchId());
                    break;
                case ProcessCd.코팅:
                case ProcessCd.충전:
                case ProcessCd.포장:
                    //기존 matUse 삭제, 변경된 내용으로 새로 생성.
                    matUseEtcService.deleteByWorkOrderItemId(workOrderItemId);
                    matUseEtcService.saveByWorkOrderItem(workOrderItem);
                    break;
                default: break;
            }
        }
        return result;
    }

    /** 작업지시 취소처리 */
    @Transactional
    public boolean cancelWorkOrderItem (WorkOrderItem workOrderItem) throws Exception {

        String workOrderItemId = workOrderItem.getWorkOrderItemId();

        workOrderItem.setWorkOrderItemStatus(WorkOrderItemStatus.작업취소);
//        matUseService.deleteByWorkOrderItemId(workOrderItemId); //기존 matUse 삭제
//        prodIngService.deleteByWorkOrderItemId(workOrderItemId); //기존 prodIng 삭제
//        matUseEtcService.deleteByWorkOrderItemId(workOrderItemId); //기존 matUseEtc 삭제

        return this.saveOrUpdate(workOrderItem);
    }

    /** 작업지시 삭제 */
    @Transactional
    public boolean deleteWorkOrderItem (WorkOrderItem workOrderItem) {

        String workOrderItemId = workOrderItem.getWorkOrderItemId();

        matUseService.deleteByWorkOrderItemId(workOrderItemId); //matUse 삭제.
        prodIngService.deleteByWorkOrderItemId(workOrderItemId); //prodIng 삭제.
        matUseEtcService.deleteByWorkOrderItemId(workOrderItemId); //matUseEtc 삭제.

        return this.removeById(workOrderItemId); //작업지시 삭제
    }


    @Transactional
    public String finishWeigh(String workOrderItemId) {

        WorkOrderItemView workOrderItemView = workOrderItemViewService.getById(workOrderItemId);
        WorkOrderItem workOrderItem = this.getById(workOrderItemId);

        MatTran matTran = new MatTran();
        // E: 제조출고  G: 외주출고

        String targetAreaCd =  workOrderItemView.getAreaCd();
        String srcStorageCd = workOrderItemView.getStorageCd();
        matTran.setTranCd(TranCd.E);
        matTran.setAreaCd(targetAreaCd);
        matTran.setDestStorageCd(ProdStorageCd.getFieldStorageCd(targetAreaCd)); //구역별 현장창고
        matTran.setSrcStorageCd(srcStorageCd);
        matTran.setMemberCd(AuthUtil.getMemberCd());
        matTran.setTranDate(LocalDate.now());
        matTran.setEndYn("N");
        matTran.setConfirmState(ConfirmState.OK);

        List<MatWeighView>  matWeighViews = this.matWeighViewService.list(new QueryWrapper<MatWeighView>().eq("work_order_item_id", workOrderItemId));

        List<MatTranItem> matTranItemList = new ArrayList<>();
        matWeighViews.forEach( item -> {
            MatTranItem matTranItem = new MatTranItem();
//            matTranItem.setMatTranId(matTran.getMatTranId()); // matTran등록시 연결되므로 생략, 이 값을 연결시 update로 처리하게 된다.
            matTranItem.setMatTranItemId(item.getMatWeighId());
            matTranItem.setItemTypeCd("M1");    // 원재료,  칭량은 모두 원재료
            matTranItem.setItemCd(item.getItemCd());
            matTranItem.setItemName(item.getItemName());
            matTranItem.setLotNo(item.getLotNo());
            matTranItem.setTestNo(item.getTestNo());
            matTranItem.setQty(item.getWeighQty());
            matTranItem.setConfirmState(ConfirmState.OK);
            matTranItemList.add(matTranItem);
        });

        this.matTranService.saveWithItems(matTran, matTranItemList, new ArrayList<MatTranItem>());

        // 칭량완료 상태 반영
        workOrderItem.setWorkOrderItemStatus(WorkOrderItemStatus.칭량완료);
        workOrderItem.setTranYn("Y");
        workOrderItem.setTranId(matTran.getMatTranId());
        workOrderItem.setEndYn("Y");
        workOrderItem.setWorkEndTime(LocalDateTime.now());
        this.saveOrUpdate(workOrderItem);

        String msg = "success";

        return msg;
    }

    @Transactional
    public boolean startWorkOrderItem (WorkOrderItem workOrderItem) throws Exception {

        /** 작업지시의 상태변경 **/
        String processCd = workOrderItem.getProcessCd();
        workOrderItem.setWorkOrderItemStatus(WorkOrderItemStatus.getStartStatus(processCd));

        /** 작업일, 작업시작시간 등록 **/
        workOrderItem.setProdDate(LocalDate.now());
        workOrderItem.setWorkStartTime(LocalDateTime.now());

        /** 칭량을 제외한 공정에서는 시험번호 생성 **/
        if(!processCd.equals(ProcessCd.칭량)) {
            WorkOrderItemView workOrderItemView = workOrderItemViewService.getById(workOrderItem.getWorkOrderItemId());
            ItemTestNo itemTestNo = itemTestNoService.saveTestNoWithWorkStart(workOrderItemView);
            workOrderItem.setItemTestNo(itemTestNo.getTestNo());

            /** 제조 & 충전의 경우 시험검사도 생성 **/
            if(ProcessCd.getTestYn(processCd)){
//                qualityTestService.saveQualityTestWithWorkStart(itemTestNo.getTestNo(), workOrderItemView.getAreaCd());
                qualityTestService.saveQualityTestWithWorkStart2(itemTestNo, workOrderItemView);
            }
        }
        return this.saveOrUpdate(workOrderItem);
    }

    @Transactional
    public boolean finishWorkOrderItem (WorkOrderItem workOrderItem) throws Exception {

        /** 작업지시의 상태변경 **/
        String processCd = workOrderItem.getProcessCd();
        workOrderItem.setWorkOrderItemStatus(WorkOrderItemStatus.getEndStatus(processCd));

        /** 작업지시 조회 및 오류 방지 데이터 삽입. **/
        WorkOrderItemView workOrderItemView = workOrderItemViewService.getById(workOrderItem.getWorkOrderItemId());
        workOrderItem.setProdDate(workOrderItemView.getProdDate());
        workOrderItem.setWorkEndTime(LocalDateTime.now());

        /** 칭량 공정일 경우 */
        if(workOrderItemView.getProcessCd().equals(ProcessCd.칭량)) {
            String weighMatTranId = matTranService.saveMatTranByWeighEnd(workOrderItemView);
            workOrderItem.setTranYn("Y");
            workOrderItem.setTranId(weighMatTranId);
        }
        /** 제조, 코팅, 충전, 포장 공정일 경우 */
        else {
            /** 오류 방지 위한 시험번호 삽입. **/
            workOrderItem.setItemTestNo(workOrderItemView.getItemTestNo());
            /** ProdRecord에서 생산량 합산. **/
            BigDecimal totalProdQty = prodRecordService.getTotalProdQty(workOrderItem.getWorkOrderItemId());
            workOrderItem.setProdQty(totalProdQty);
            workOrderItemView.setProdQty(totalProdQty);

            /** 제조입고 tran 생성 및 tranItem 생성 및 TranId, TranYn 처리 **/
            MatTran matTran = matTranService.saveMatTranByWorkEnd(workOrderItemView);
            MatTranItem matTranItem = matTranItemService.saveMatTranItemByWorkEnd(matTran, workOrderItemView);
            workOrderItem.setProdTranYn("Y");
            workOrderItem.setProdTranId(matTran.getMatTranId());

            /** 시험번호 입고량 수정. **/
            boolean itemCheck = itemTestNoService.updateQtyByWorkEnd(workOrderItemView);

            /** 제조, 충전일 경우에만 시험검사의 요청량 수정 **/
            if(ProcessCd.getTestYn(processCd)) {
                boolean testCheck = qualityTestService.updateReqQtyByWorkEnd(workOrderItemView);
            }

            /** 코팅,충전,포장 matTranEtc, matTranResult 제조출고 처리 **/
            if(!processCd.equals(ProcessCd.제조) && matTranService.isExistMatUseEtc(workOrderItemView)) {
                String matTranId = matTranService.saveMatUseEtcTranByWorkEnd(workOrderItemView);
                workOrderItem.setTranYn("Y");
                workOrderItem.setTranId(matTranId);
            }
        }
        workOrderItem.setEndYn("Y");

        return this.saveOrUpdate(workOrderItem);
    }


    @Transactional
    public boolean editWorkOrderItem (WorkOrderItem workOrderItem) throws Exception {

        ///상태변경할 필요없음
        /** 작업지시의 상태변경 **/
        String processCd = workOrderItem.getProcessCd();

        /** 작업지시 조회 및 오류 방지 데이터 삽입. **/
        WorkOrderItemView workOrderItemView = workOrderItemViewService.getById(workOrderItem.getWorkOrderItemId());

        /** ProdRecord에서 생산량 합산. **/
        BigDecimal totalProdQty = prodRecordService.getTotalProdQty(workOrderItem.getWorkOrderItemId());
        workOrderItem.setProdQty(totalProdQty);
        workOrderItemView.setProdQty(totalProdQty);

        /** 생산품에 대한 제조입고 tran 조회, tranItem 삭제 및 재생성 **/
        String prodTranId = workOrderItem.getProdTranId();
        MatTran matTran = matTranService.getById(prodTranId);
        matTranItemService.remove(new QueryWrapper<MatTranItem>().eq("mat_tran_id", prodTranId));
        MatTranItem matTranItem = matTranItemService.saveMatTranItemByWorkEnd(matTran, workOrderItemView);

        /** 시험번호 입고량 수정. **/
        boolean itemCheck = itemTestNoService.updateQtyByWorkEnd(workOrderItemView);

        /** 제조, 충전일 경우에만 시험검사의 요청량 수정 **/
        if(ProcessCd.getTestYn(processCd)) {
            boolean testCheck = qualityTestService.updateReqQtyByWorkEnd(workOrderItemView);
        }

        /** 코팅,충전,포장 matTranEtc, matTranResult 제조출고 삭제 및 재생성 처리 **/
        if( processCd.equals(ProcessCd.코팅) || processCd.equals(ProcessCd.충전) || processCd.equals(ProcessCd.포장)) {
            String matTranId = matTranService.updateMatUseEtcTran(workOrderItemView);
        }

        return this.saveOrUpdate(workOrderItem);
    }

    public String getTemplatePathByProcessCd (String processCd) {

        String templatePath = "";

        //각 공정에 해당하는 양식 설정
        switch (processCd) {
            case ProcessCd.코팅:
                templatePath = "/excel/coating_template.xlsx"; break;
            case ProcessCd.충전:
                templatePath = "/excel/charging_template.xlsx"; break;
            case ProcessCd.포장:
                templatePath = "/excel/packing_template.xlsx"; break;
            default:
                break;
        }
        return templatePath;
    }

    /** 지시 및 기록서(코팅, 충전, 포장) 엑셀 다운로드 **/
    public ResponseEntity<Resource> getOrderExcel(String workOrderItemId) throws Exception {
        WorkOrderItemWithRecord resultData = getWorkOrderItemWithRecord(workOrderItemId);

        WorkOrderItemView workOrderItemInfo = resultData.getWorkOrderItemInfo();
        ItemMasterView itemInfo = resultData.getItemInfo();
        List<MatUseEtcView> matUseEtcList = resultData.getMatUseEtcList();
        List<ProdRecord> prodRecordList = resultData.getProdRecordList();

        String processCd = workOrderItemInfo.getProcessCd();
        Boolean isCoating = processCd.equals(ProcessCd.코팅);

        try {
            // 공정 별 양식을 로드
            InputStream excelStream = getClass().getResourceAsStream(getTemplatePathByProcessCd(processCd));
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            Sheet sheet = workbook.getSheet("Sheet1");

            /** 타이틀 part **/
            ExcelPoiUtil.getCellRef(sheet, "H3").setCellValue(workOrderItemInfo.getItemName()); //품목명
            ExcelPoiUtil.getCellRef(sheet, "AU1").setCellValue(workOrderItemInfo.getItemCd());  //품목코드

            /** 작업정보 그리드 part **/
            //고객사 : F6
            ExcelPoiUtil.getCellRef(sheet, "F6").setCellValue(workOrderItemInfo.getCustomerName());
            //제품타입 : S6
            ExcelPoiUtil.getCellRef(sheet, "S6").setCellValue(itemInfo.getProdType());
            //작업일자 : AF6
            String orderDateFormat = workOrderItemInfo.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            ExcelPoiUtil.getCellRef(sheet, "AF6").setCellValue(orderDateFormat);
            //작업지시량 : AS6
            double orderQty = (workOrderItemInfo.getOrderQty() != null )? workOrderItemInfo.getOrderQty().doubleValue() : 0;
            ExcelPoiUtil.getCellRef(sheet, "AS6").setCellValue(orderQty);
            //제조번호 : F7
            ExcelPoiUtil.getCellRef(sheet, "F7").setCellValue(workOrderItemInfo.getProdNo());
            //제조량 : S7
            double producedQty = (workOrderItemInfo.getProducedQty() != null )? workOrderItemInfo.getProducedQty().doubleValue() : 0;
            ExcelPoiUtil.getCellRef(sheet, "S7").setCellValue(producedQty);
            // 작업처 : F8
            ExcelPoiUtil.getCellRef(sheet, "F8").setCellValue(workOrderItemInfo.getStorageName());
            // 작업공정도 : F9
            ExcelPoiUtil.getCellRef(sheet, "F9").setCellValue(itemInfo.getWorkFlow());
            // 특이사항 : 코팅일경우 AF9, 충전 포장일 경우 AP7
            String noteCell = (isCoating)?"AF9" : "AP7";
            ExcelPoiUtil.getCellRef(sheet, noteCell).setCellValue(workOrderItemInfo.getNote());

            //코팅공정 공유 표시값.
            if (isCoating){
                //폭너비(mm) 및 겔 Sheet 적층 수 BB7, BC7, BB8
                double sheetLength = (itemInfo.getSheetLength() != null)? itemInfo.getSheetLength().doubleValue() : 0;
                double sheetWidth = (itemInfo.getSheetWidth() != null)? itemInfo.getSheetWidth().doubleValue() : 0;
                double sheetStacking = (itemInfo.getSheetStacking() != null)? itemInfo.getSheetStacking().doubleValue() : 0;
                ExcelPoiUtil.getCellRef(sheet, "BB7").setCellValue(sheetLength);
                ExcelPoiUtil.getCellRef(sheet, "BC7").setCellValue(sheetWidth);
                ExcelPoiUtil.getCellRef(sheet, "BB8").setCellValue(sheetStacking);
                // 기준무게 (필름제외)
                ExcelPoiUtil.getCellRef(sheet, "AS7").setCellValue(itemInfo.getStdWeight());
                String stdSize = "("+ itemInfo.getStdSize() + ")";
                ExcelPoiUtil.getCellRef(sheet, "AS8").setCellValue(stdSize);
                // 목형 No.
                ExcelPoiUtil.getCellRef(sheet, "S8").setCellValue(itemInfo.getWoodenPattern());
            }
            //충전, 포장 공정 고유 표시값.
            else {
                //Lot 표기 1열 AD7, 2열 AD8
                String[] lotNoArray = workOrderItemInfo.getLotNo().split("!!");
                if(lotNoArray.length == 2) { //로트번호가 두줄일 경우
                    ExcelPoiUtil.getCellRef(sheet, "AD7").setCellValue(lotNoArray[0]);
                    ExcelPoiUtil.getCellRef(sheet, "AD8").setCellValue(lotNoArray[1]);
                } else { //로트번호가 한줄일 경우
                    ExcelPoiUtil.getCellRef(sheet, "AD7").setCellValue(workOrderItemInfo.getLotNo());
                    ExcelPoiUtil.getCellRef(sheet, "AD8").setCellValue("");
                }
                // 표시용량
                ExcelPoiUtil.getCellRef(sheet, "S8").setCellValue(itemInfo.getDisplayCapacity());
            }

            /** 시험번호 그리드 **/
            //시작 row 번호 (범위는  13 ~ 22)
            int rowNo = 13;
            for ( MatUseEtcView matUseEtcView : matUseEtcList ) {
                //시험번호 C13
                ExcelPoiUtil.getCellRef(sheet, "C"+rowNo).setCellValue(matUseEtcView.getTestNoStr());
                //재료명 H13
                ExcelPoiUtil.getCellRef(sheet, "H"+rowNo).setCellValue(matUseEtcView.getMatName());
                //규격 N13
                ExcelPoiUtil.getCellRef(sheet, "N"+rowNo).setCellValue(matUseEtcView.getSpecInfo());
                //성상및특성 T13
                ExcelPoiUtil.getCellRef(sheet, "T"+rowNo).setCellValue(matUseEtcView.getExAppearance());

                //충전, 포장일 경우에만
                if(!isCoating){
                    //단위포장규격
                    ExcelPoiUtil.getCellRef(sheet, "AA"+rowNo).setCellValue(matUseEtcView.getPackingSpecValue());
                    ExcelPoiUtil.getCellRef(sheet, "AD"+rowNo).setCellValue(matUseEtcView.getPackingSpecUnit());
                }

                //예상소요량 AI13
                double reqQty = (matUseEtcView.getReqQty() != null)? matUseEtcView.getReqQty().doubleValue() : 0;
                ExcelPoiUtil.getCellRef(sheet, "AI"+rowNo).setCellValue(reqQty);
                //단위 AL13 AQ13  AU13  AY13
                String unit = matUseEtcView.getUnit();
                ExcelPoiUtil.getCellRef(sheet, "AL"+rowNo).setCellValue(unit);
                ExcelPoiUtil.getCellRef(sheet, "AQ"+rowNo).setCellValue(unit);
                ExcelPoiUtil.getCellRef(sheet, "AU"+rowNo).setCellValue(unit);
                ExcelPoiUtil.getCellRef(sheet, "AY"+rowNo).setCellValue(unit);
                //총사용량 AN13 2021-12-21 사용량 -> 총사용량으로 변경
                double totalQtySum = (matUseEtcView.getTotalQtySum() != null)? matUseEtcView.getTotalQtySum().doubleValue() : 0;
                ExcelPoiUtil.getCellRef(sheet, "AN"+rowNo).setCellValue(totalQtySum);
                //원불량 AS13
                double badMatQtySum = (matUseEtcView.getBadMatQtySum() != null)? matUseEtcView.getBadMatQtySum().doubleValue() : 0;
                ExcelPoiUtil.getCellRef(sheet, "AS"+rowNo).setCellValue(badMatQtySum);
                //작업불량  AW13
                double badWorkQtySum = (matUseEtcView.getBadWorkQtySum() != null)? matUseEtcView.getBadWorkQtySum().doubleValue() : 0;
                ExcelPoiUtil.getCellRef(sheet, "AW"+rowNo).setCellValue(badWorkQtySum);
                rowNo++;
            }

            /** 작업시간 그리드 **/
            // 3건 이하는 그리드에 표시, 그 이상일 경우는 공백으로 표시.
            int recordSize = prodRecordList.size();
            if(recordSize > 0) {
                int timeRowNo = 26; //26 ~ 28
                for ( ProdRecord prodRecord : prodRecordList ) {
                    //일자  A26  mm월dd형식
                    String recordDateFormat = prodRecord.getProdDate().format(DateTimeFormatter.ofPattern("MM월dd일"));
                    ExcelPoiUtil.getCellRef(sheet, "A" + timeRowNo).setCellValue(recordDateFormat);
                    //작업시작 ~ 작업종료  00:00형식 시작시간 : D26  종료시간: J26
                    ExcelPoiUtil.getCellRef(sheet, "D" + timeRowNo).setCellValue(prodRecord.getProdStartTime());
                    ExcelPoiUtil.getCellRef(sheet, "J" + timeRowNo).setCellValue(prodRecord.getProdEndTime());
                    //작업인원 O26
                    ExcelPoiUtil.getCellRef(sheet, "O" + timeRowNo).setCellValue(prodRecord.getProdWorkerCnt());
                    //반제품사용량(벌크제품, 포장품 사용량) R26
                    double inputQty = (prodRecord.getInputQty() != null)? prodRecord.getInputQty().doubleValue() : 0;
                    ExcelPoiUtil.getCellRef(sheet, "R" + timeRowNo).setCellValue(inputQty);
                    //코팅수량(충전, 포장 수량) V26
                    double prodQty = (prodRecord.getProdQty() != null)? prodRecord.getProdQty().doubleValue() : 0;
                    ExcelPoiUtil.getCellRef(sheet, "V" + timeRowNo).setCellValue(prodQty);

                    if(timeRowNo == 28) break;
                    timeRowNo++;
                }
            }

            /** 수율 그리드 **/
            //수율( + 계산식 + ) Z24
            ExcelPoiUtil.getCellRef(sheet, "Z24").setCellValue(itemInfo.getDisplayYield());
            // lot수율    기준 00% 이상  AE25
            double stdYield = (itemInfo.getStdYield() != null)? itemInfo.getStdYield().doubleValue() : 0;
            ExcelPoiUtil.getCellRef(sheet, "AE25").setCellValue(stdYield);
            // 총사용량  AE26
            double matUseQty = (workOrderItemInfo.getMatUseQty() != null)? workOrderItemInfo.getMatUseQty().doubleValue() : 0;
            ExcelPoiUtil.getCellRef(sheet, "AE26").setCellValue(matUseQty);
            // 생산수량  AE27
            double prodQty = (workOrderItemInfo.getProdQty() != null)? workOrderItemInfo.getProdQty().doubleValue() : 0;
            ExcelPoiUtil.getCellRef(sheet, "AE27").setCellValue(prodQty);
            // 생산수율  AE28
            double yieldRate = (workOrderItemInfo.getYieldRate() != null)? workOrderItemInfo.getYieldRate().doubleValue() : 0;
            ExcelPoiUtil.getCellRef(sheet, "AE28").setCellValue(yieldRate);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("지시 및 기록서", excelContent.length, "xlsx");

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    /** 생산품(제조, 코팅, 충전, 포장) 바코드 PDF 다운로드 **/
    public ResponseEntity<Resource> getProdLabel(@RequestBody ProdLabelItem[] labelItems) throws Exception {

        List<LabelPrint> labelPrintList = new ArrayList<>();

        for (ProdLabelItem labelItem : labelItems ) {
            ItemMasterView itemMasterView = itemMasterViewService.getById(labelItem.getItemCd());
            if (itemMasterView.getItemCondition() == null) {
                itemMasterView.setItemConditionName("실온");
            }
            String itemTypeCd = itemMasterView.getItemTypeCd();
            for (int i = 0; i < labelItem.getPrintCnt(); i++) {
                ItemTestNoView itemTestNo = itemTestNoViewService.getById(labelItem.getItemTestNo());
                LabelPrint labelPrint = new LabelPrint(
                        "(" + itemMasterView.getItemCd() + ")",
                        itemMasterView.getItemName(),
                        itemMasterView.getItemConditionName(),
                        labelItem.getItemTestNo(),
                        labelItem.getProdNo(),
                        labelItem.getExpiryDate(),
                        new DecimalFormat("#,##0").format(labelItem.getProdQty()) + "kg",
                        itemTestNo.getPassStateName()
                );
                labelPrint.setLabelTitle(ItemTypeCd.getLableTitle(itemTypeCd));
                labelPrintList.add(labelPrint);
            }
        }

        try {
            InputStream reportStream = getClass().getResourceAsStream("/report/mat_prod_label_v3.jrxml");
            byte[] pdfContent = JasperUtil.getPdfBinary(reportStream, labelPrintList);

            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("prod_label_list", pdfContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("리포트에서 에러발생");
        }
    }
    public List<ReqItemQty> getItemQtyList(List<String> workOrderItemIdList) {
        Map<String, Object> param = new HashMap<>();
        param.put("workOrderItemIdList", workOrderItemIdList);
        return this.baseMapper.getItemQtyList(param);
    }

    public List<Map<String, Object>> getProdPerformance(PeriodVo periodVo) {
        return this.baseMapper.getProdPerformance(periodVo);
    }

    public List<Map<String, Object>> getProdPerformanceByCustomer(PeriodVo periodVo) {
        return this.baseMapper.getProdPerformanceByCustomer(periodVo);
    }

    public List<EquipRunTagValue> getEquipRunValues(EquipRunVo equipRunVo) {
        return this.baseMapper.getEquipRunValues(equipRunVo);
    }

}
