package com.daehanins.mes.biz.qt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.adm.entity.CommonCode;
import com.daehanins.mes.biz.adm.service.ICommonCodeService;
import com.daehanins.mes.biz.common.code.*;
import com.daehanins.mes.biz.mat.entity.MatTran;
import com.daehanins.mes.biz.mat.entity.MatTranItem;
import com.daehanins.mes.biz.mat.service.IMatTranItemService;
import com.daehanins.mes.biz.mat.service.IMatTranService;
import com.daehanins.mes.biz.pub.entity.ItemMasterView;
import com.daehanins.mes.biz.pub.entity.Storage;
import com.daehanins.mes.biz.pub.service.IItemMasterViewService;
import com.daehanins.mes.biz.pub.service.IStorageService;
import com.daehanins.mes.biz.qt.entity.*;
import com.daehanins.mes.biz.qt.mapper.QualityTestMapper;
import com.daehanins.mes.biz.qt.service.*;
import com.daehanins.mes.biz.qt.vo.QualityTestReportItem;
import com.daehanins.mes.biz.qt.vo.QualityTestSaveWithItems;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;
import com.daehanins.mes.common.exception.BizException;
import com.daehanins.mes.common.utils.AuthUtil;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 품질검사QualityTest 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-03
 */
@Service
public class QualityTestServiceImpl extends ServiceImpl<QualityTestMapper, QualityTest> implements IQualityTestService {

    @Autowired
    private IQualityTestViewService qualityTestViewService;

    @Autowired
    private IQualityTestMethodService qualityTestMethodService;

    @Autowired
    private IQualityTestMethodViewService qualityTestMethodViewService;

    @Autowired
    private IItemTestNoService itemTestNoService;

    @Autowired
    private IItemTestNoViewService itemTestNoViewService;

    @Autowired
    private IItemMasterViewService itemMasterViewService;

    @Autowired
    private IStorageService storageService;

    @Autowired
    private IMatTranService matTranService;

    @Autowired
    private IMatTranItemService matTranItemService;

    @Autowired
    private ICommonCodeService commonCodeService;


    /** 검사 지시 및 성적서 **/
    private final int REPO_MAX_ROW = 15;    // 승인칸 있을 시, 최대 ROW수
    private final int REPO_DIVIDE_CNT = 17; // 승인칸 없을 시, 최대 ROW수
    private final int TEST_REPORT_MAX_ROWS = 15;        // 검사지시 및 성적서 페이지 구분 카운트 (15줄)

    private final int JOURNAL_LINE_CNT = 9;

    /** 시험일지 **/
    private final int TEST_LOG_MAX_ROWS = 10;    // 검사항목 최대 16개 출력
    @Transactional
    public QualityTestSaveWithItems saveWithItems(QualityTest qualityTest, List<QualityTestMethod> qualityTestMethods, List<QualityTestMethod> deleteQualityTestMethods) {

        QualityTestSaveWithItems data = new QualityTestSaveWithItems();
        // 창고코드로 구역(공장)정보 구하여 설정함
        Storage storage = this.storageService.getById(qualityTest.getStorageCd());
        qualityTest.setAreaCd(storage.getAreaCd());

        if (this.saveOrUpdate(qualityTest)) {
            data.setQualityTest(qualityTest);
        }
        // 시험번호 테이블의 상태를 업데이트
        this.itemTestNoService.update(
                new ItemTestNo().setTestState(qualityTest.getTestState()).setPassState(qualityTest.getPassState()),
                Wrappers.<ItemTestNo>lambdaUpdate()
                        .eq(ItemTestNo::getTestNo, qualityTest.getTestNo())
        );

        // item 삭제 처리
        deleteQualityTestMethods.forEach( item -> {
            if (StringUtils.isNotBlank(item.getQualityTestMethodId())) {
                this.qualityTestMethodService.removeById(item.getQualityTestMethodId());
            }
        });

        int index = 0;
        for(QualityTestMethod item : qualityTestMethods) {
            index++;
            item.setDisplayOrder(index);
            item.setQualityTestId(qualityTest.getQualityTestId());
            this.qualityTestMethodService.saveOrUpdate(item);
            data.getQualityTestMethods().add(item);
        }

        QualityTest result = this.getById(qualityTest.getQualityTestId());
        if (result.getTranYn().equals("N") && result.getTestQty() != null && result.getTestQty().compareTo(BigDecimal.ZERO) > 0) {
            MatTran matTran = new MatTran();
            matTran.setTranCd(TranCd.T);
            matTran.setTranDate(qualityTest.getTestDate());
            matTran.setSerNo(null);
            matTran.setMemberCd(qualityTest.getSampleMemberCd());
            matTran.setConfirmMemberCd(qualityTest.getSampleMemberCd());
            matTran.setAreaCd(qualityTest.getAreaCd());
            matTran.setSrcStorageCd(qualityTest.getStorageCd());
            matTran.setVatType("Y");
            matTran.setErpYn("N");
            matTran.setConfirmState("OK");
            matTran.setEndYn("Y");
            matTran.setMemo("");

            ItemTestNo itemTestNo = itemTestNoService.getById(qualityTest.getTestNo());
            ItemMasterView itemMaster = itemMasterViewService.getById(itemTestNo.getItemCd());
            String matTranId = matTran.getMatTranId();

            MatTranItem matTranItem = new MatTranItem();
            matTranItem.setMatTranId(matTranId);
            matTranItem.setItemCd(itemTestNo.getItemCd());
            matTranItem.setItemName(itemMaster.getItemName());
            String itemTypeCd = itemMaster.getItemTypeCd();
            matTranItem.setItemTypeCd(itemTypeCd);
            BigDecimal qty = qualityTest.getTestQty().multiply(new BigDecimal(-1));
            matTranItem.setQty(qty);
            matTranItem.setLotNo(itemTestNo.getLotNo());
            matTranItem.setTestNo(qualityTest.getTestNo());

            matTranService.saveOrUpdate(matTran);
            matTranItemService.saveOrUpdate(matTranItem);

            qualityTest.setTranYn("Y");
            qualityTest.setMatTranId(matTranId);
            this.saveOrUpdate(qualityTest);
        }

        return data;
    }

    @Transactional
    public String deleteWithItems(List<String> idList) {
        String msg;

        // 검사상세의 검사방법 삭제
        for (String qualityTestId : idList) {
            qualityTestMethodService.remove(new QueryWrapper<QualityTestMethod>().eq("quality_test_id", qualityTestId));
        }
        // 검사 삭제
        if (this.removeByIds(idList)) {
            msg = "success";
        } else {
            msg = "fail";
        }
        return msg;
    }

    @Transactional
    public boolean makeSampleUse(LocalDate testDate) {

        List<QualityTest> qualityTestList = this.list(new QueryWrapper<QualityTest>()
            .eq("test_date", testDate)
        );

        // 자재tran이 존재하는 경우 삭제
        qualityTestList.forEach(item -> {
            if (item.getTranYn().equals("Y")) {
                List<String> idList =  Arrays.asList(item.getMatTranId());
                this.matTranService.deleteWithItems(idList);
            }
        });

        // 자재tran 생성
        qualityTestList.forEach(item -> {
            ItemTestNoView itemTestNo = this.itemTestNoViewService.getById(item.getTestNo());
            // QQQ 시험번호가 없는 경우, 초기 테스트 시점에 문제발생 예측되어 skip함   2020/09/04  jeonsj
            if (itemTestNo == null) {
                return;
            }
            MatTran matTran = new MatTran();
            matTran.setTranCd(TranCd.T);
            matTran.setTranDate(item.getTestDate());
            matTran.setMemberCd(item.getSampleMemberCd());
            matTran.setAreaCd(item.getAreaCd());
            matTran.setSrcStorageCd(item.getStorageCd());
            matTran.setCustomerCd(itemTestNo.getCustomerCd());
            matTran.setErpYn("Y");
            matTran.setEndYn("Y");
            matTran.setConfirmState(ConfirmState.OK);
            matTran.setConfirmMemberCd(item.getOrderMemberCd());

            MatTranItem matTranItem = new MatTranItem();
            matTranItem.setItemCd(itemTestNo.getItemCd());
            matTranItem.setItemName(itemTestNo.getItemName());
            matTranItem.setItemTypeCd(itemTestNo.getItemTypeCd());
            matTranItem.setLotNo(itemTestNo.getLotNo());
            // 검체채취량은 g으로 입력되므로 kg으로 변환하여 등록한다.
            matTranItem.setQty(item.getSampleQty().divide(new BigDecimal(1000),6,BigDecimal.ROUND_UP));
            matTranItem.setTestNo(item.getTestNo());
            matTranItem.setConfirmState(ConfirmState.OK);

            this.matTranService.saveWithItems(matTran, Arrays.asList(matTranItem), new ArrayList<MatTranItem>());

            // 자재tranId와 처리여부 반영
            item.setTranYn("Y");
            item.setMatTranId(matTran.getMatTranId());
            this.saveOrUpdate(item);
        });

        return true;
    }

    @Transactional
    public boolean saveQualityTestWithWorkStart (String testNo, String areaCd) {
        QualityTest newTest = new QualityTest();
        newTest.setTestNo(testNo);
        newTest.setReqDate(LocalDate.now()); //검사요청 시간은 현재시간으로
        newTest.setReqMemberCd(AuthUtil.getMemberCd()); //검사요청자는 로그인사용자
        newTest.setAreaCd(areaCd);
        newTest.setStorageCd(ProdStorageCd.getFieldStorageCd(areaCd)); // 생산품은 모두 현장창고로 입고.
        newTest.setReqQty(BigDecimal.ZERO); // 최초 생성시 에는 0으로 생성.
        newTest.setTestState(TestState.REQ); // 검사 요청 상태
        newTest.setPassState(PassState.REQ); // 검사 요청 상태
        newTest.setRetestYn("N");   // 재검사여부 - 최초등록이므로 "N"
        newTest.setTranYn("N");
        return this.saveOrUpdate(newTest);
    }

    @Transactional
    public boolean saveQualityTestWithWorkStart2 (ItemTestNo itemTestNo, WorkOrderItemView workOrderItemView) {
        QualityTest newTest = new QualityTest();
        newTest.setTestNo(itemTestNo.getTestNo());
        newTest.setReqDate(LocalDate.now()); //검사요청 시간은 현재시간으로
        String memberCd = (workOrderItemView.getManagerId() != null && !workOrderItemView.getManagerId().equals(""))
                ?  workOrderItemView.getManagerId() : AuthUtil.getMemberCd();
        newTest.setReqMemberCd(memberCd); //검사요청자는 로그인사용자
        newTest.setAreaCd(workOrderItemView.getAreaCd());
        newTest.setStorageCd(ProdStorageCd.getFieldStorageCd(workOrderItemView.getAreaCd())); // 생산품은 모두 현장창고로 입고.
        newTest.setReqQty(BigDecimal.ZERO); // 최초 생성시 에는 0으로 생성.
        newTest.setTestState(TestState.REQ); // 검사 요청 상태
        newTest.setPassState(PassState.REQ); // 검사 요청 상태
        newTest.setRetestYn("N");   // 재검사여부 - 최초등록이므로 "N"
        newTest.setTranYn("N");
        return this.saveOrUpdate(newTest);
    }


    /** 작업지시 완료 후, 생산량 수정 **/
    @Transactional
    public boolean updateReqQtyByWorkEnd (WorkOrderItemView workOrderItemView) {
        UpdateWrapper<QualityTest> qualityTestUpdateWrapper = new UpdateWrapper<>();
        qualityTestUpdateWrapper.eq("test_no", workOrderItemView.getItemTestNo());
        qualityTestUpdateWrapper.eq("req_qty", BigDecimal.ZERO);
        qualityTestUpdateWrapper.set("req_qty", workOrderItemView.getProdQty());
        return this.update(qualityTestUpdateWrapper);
    }

    /** 시험성적 및 기록서 출력 **/
    public byte[] printQualityTestReports (String qualityTestId) throws Exception {

        //시험검사 조회
        QualityTestView qualityTest = qualityTestViewService.getById(qualityTestId);

        //검사항목 조회
        QueryWrapper<QualityTestMethodView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.camelToUnderline("qualityTestId"), qualityTestId);
        List<QualityTestMethodView> qtMethods = qualityTestMethodViewService.list(queryWrapper);

        String itemGb = qualityTest.getItemGb();

        //param 생성
        Map<String, Object> param = this.getParamMap(qualityTest, itemGb);

        // 리스트 구성
        List<QualityTestReportItem> qtItemList = getQtItemList(qtMethods, qualityTest.getConfirmMemberCd());

        List<QualityTestReportItem> qtReportList = filledListItem(qtItemList, REPO_MAX_ROW, REPO_DIVIDE_CNT);
        List<QualityTestReportItem> qtLogList = filledListItem(qtItemList, TEST_LOG_MAX_ROWS, TEST_LOG_MAX_ROWS);

        //성적서 생성
        JasperPrint jasperPrint1= writeJasperReportPage("quality_test_report_M" + itemGb, qtReportList, param);
        //시험일지 생성
        JasperPrint jasperPrint2= writeJasperReportPage("quality_test_log_M" + itemGb, qtLogList, param);

        List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
        jasperPrintList.add(jasperPrint1);
        jasperPrintList.add(jasperPrint2);

        JRPdfExporter exporter = new JRPdfExporter();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));    // 다수의 jasperPrint 입렦
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));         // ByteArrayStream 출력

        exporter.exportReport();

        return baos.toByteArray();
    }

    public Map<String, Object> getParamMap (QualityTestView qualityTest, String itemGb) throws Exception{

        //품목정보 조회
        String itemCd = qualityTest.getItemCd();
        //ItemMaster itemMaster = itemMasterService.getById(itemCd);
        ItemMasterView itemMasterView = itemMasterViewService.getById(itemCd);

        Map<String, Object> resultMap = new HashMap<>();
        //로고 삽입
        BufferedImage logoImage = ImageIO.read(getClass().getResource("/static/images/logo1.png"));
        resultMap.put("logo", logoImage);
        // 반제품(3) 일 경우 제조번호로 표시
        boolean isMat = (itemGb.equals("1") || itemGb.equals("2") || itemGb.equals("6"));
        resultMap.put("titleLotNo", (isMat)? "Lot NO." : "제조번호" );

        //로트번호 상 !!가 있을 경우 띄어쓰기로 변경
        String lotNo = ( qualityTest.getLotNo() == null ) ? "" : qualityTest.getLotNo().replaceAll("!!", " ");
        resultMap.put("printLotNo", (isMat)? lotNo : qualityTest.getProdNo());
        // 원/부자재 일 경우 구매부, 그 외 생산부
        resultMap.put("reqDept", (itemGb.equals("1") || itemGb.equals("2"))? "구매부" : "생산부");

        // 시험번호 처리, 재검사 일경우 처리 포함.
        String printTestNo = qualityTest.getTestNo();
        if (StringUtils.isNotBlank(qualityTest.getRetestYn()) && qualityTest.getRetestYn().equals("Y")) {
            printTestNo = qualityTest.getTestNo() + " - " + qualityTest.getRetestSerNo();
        }
        resultMap.put("printTestNo", printTestNo);

        resultMap.put("itemCd", itemCd);
        resultMap.put("itemName", qualityTest.getItemName());
        resultMap.put("lotNo", lotNo);
        resultMap.put("prodNo", qualityTest.getProdNo());

        //날짜 처리.
        if (qualityTest.getReqDate() != null) {
            String reqDate = qualityTest.getReqDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            resultMap.put("reqDate", reqDate);
        }
        if (qualityTest.getTestDate() != null) {
            String testDate = qualityTest.getTestDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            resultMap.put("testDate", testDate);
        }
        if (qualityTest.getConfirmDate() != null) {
            String confirmDate = qualityTest.getConfirmDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            resultMap.put("confirmDate", confirmDate);
        }
        resultMap.put("customerName", qualityTest.getCustomerName());
        resultMap.put("reqMember", qualityTest.getReqMemberCd());
        resultMap.put("testMember", qualityTest.getTestMemberCd());
        resultMap.put("orderMember", qualityTest.getOrderMemberCd());
        resultMap.put("sampleMember", qualityTest.getSampleMemberCd());
        resultMap.put("confirmMember", qualityTest.getConfirmMemberCd());

        DecimalFormat df1 = new DecimalFormat("#,##0.000");
        DecimalFormat df2 = new DecimalFormat("#,##0");

        String orgUnit = itemMasterView.getUnit();
        //단위가 등록안되어있을경우 기본값은 "kg(ea)"
        String unit = (orgUnit == null || orgUnit.equals(""))? "kg(ea)" : orgUnit;
        String sampleUnit = (unit.equals("kg"))? "g(ml)" : unit;
        String reqQty = (unit.equals("kg"))? df1.format(qualityTest.getReqQty()) : df2.format(qualityTest.getReqQty());

        resultMap.put("reqQty", reqQty + " " + unit);
        resultMap.put("sampleQty", df2.format(qualityTest.getSampleQty()) + " " + sampleUnit);
        resultMap.put("itemGrp1Name", itemMasterView.getItemGrp1Name());
        resultMap.put("itemGrp3Name", itemMasterView.getItemGrp3Name());

        return resultMap;
    }

    public List<QualityTestReportItem> getQtItemList (List<QualityTestMethodView> qtMethods, String confirmMemberCd) {
        List<QualityTestReportItem> resultList = new ArrayList<>();
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
            qtItem.setConfirmMember(confirmMemberCd);
            resultList.add(qtItem);
        }
        return resultList;
    }

    public List<QualityTestReportItem> filledListItem (List<QualityTestReportItem> listItem, int maxRow, int divideCnt) {
        List<QualityTestReportItem> resultList = new ArrayList<>(listItem);

        int rowCount = resultList.size();
        int appendRowCount = 0;
        int remainCount = rowCount % ( divideCnt);

        if (remainCount == 0) {
            appendRowCount = maxRow;
        } else if (remainCount > maxRow){
            appendRowCount = divideCnt - remainCount + maxRow;
        } else {
            appendRowCount = maxRow - remainCount;
        }

        while(appendRowCount > 0 ) {
            appendRowCount--;
            QualityTestReportItem qtItem = new QualityTestReportItem();
            resultList.add(qtItem);
        }

        return resultList;
    }

    //시험 성적서 & 시험일지 레포트 생성.
    public JasperPrint writeJasperReportPage (String templateName, Collection<?> itemList, Map<String, Object> param) throws Exception {
        try {
            InputStream reportStream = getClass().getResourceAsStream("/report/" + templateName + ".jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(itemList);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, dataSource);
            return jasperPrint;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BizException( templateName + "생성 중 에러 발생");
        }
    }

    /** 시험지시 및 성적서 Excel **/
    public ResponseEntity<Resource> getQtReport (String qualityTestId) throws Exception {

        QualityTestView qualityTestView = qualityTestViewService.getById(qualityTestId);
        List<QualityTestMethodView> qualityTestMethodViews = qualityTestMethodViewService.getByQualityTestId(qualityTestId);
        String itemGb = qualityTestView.getItemGb();
        List<CommonCode> commonCodeList = commonCodeService.getByCodeType(this.getDocNo(itemGb, "A"));
        int size = qualityTestMethodViews.size();
        String templateName = this.getTemplateName(itemGb, size);

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/" + templateName + ".xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("Sheet1");
            Footer footer = sheet.getFooter();

            /** 공통코드 처리 part **/
            for (CommonCode item : commonCodeList) {
                if (item.getCode().equals("DOC_NO")) {
                    footer.setLeft(item.getCodeName());
                } else {
                    ExcelPoiUtil.getCellRef(sheet, item.getCode()).setCellValue(item.getCodeName());
                }
            }
            /** 상단 정보 part **/
            Map<String, Object> cellValueList = getCellValueList(itemGb, qualityTestView);

            for ( String key : cellValueList.keySet() ) {
                if(cellValueList.get(key) instanceof String) {
                    ExcelPoiUtil.getCellRef(sheet, key).setCellValue(cellValueList.get(key).toString());
                } else {
                    ExcelPoiUtil.getCellRef(sheet, key).setCellValue((double)cellValueList.get(key));
                }

            }

            /** 검사내역 part **/
            int rowNo = (itemGb.equals("3"))? 14 : 15;
            //A15 D15 J15 P15 T15 V15 X15
            for (QualityTestMethodView item : qualityTestMethodViews){
                ExcelPoiUtil.getCellRef(sheet, "A" + rowNo).setCellValue(item.getTestItem());
                ExcelPoiUtil.getCellRef(sheet, "D" + rowNo).setCellValue(item.getTestSpec());
                ExcelPoiUtil.getCellRef(sheet, "J" + rowNo).setCellValue(item.getTestResult());
                ExcelPoiUtil.getCellRef(sheet, "P" + rowNo).setCellValue(item.getTestDateString());
                ExcelPoiUtil.getCellRef(sheet, "T" + rowNo).setCellValue(item.getTestMemberName());
                ExcelPoiUtil.getCellRef(sheet, "V" + rowNo).setCellValue(qualityTestView.getConfirmMemberCd());
                ExcelPoiUtil.getCellRef(sheet, "X" + rowNo).setCellValue(item.getPassState());
                rowNo++;
            }

            //판정일자
            //M34 R34 W34 or 52
            String lastword = templateName.substring(templateName.length()-1, templateName.length());
            int confirmDateIdx = lastword.equals("1")? 34 : 52;
            String confirmDate = qualityTestView.getConfirmDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            ExcelPoiUtil.getCellRef(sheet, "M" + confirmDateIdx).setCellValue(confirmDate);
            ExcelPoiUtil.getCellRef(sheet, "R" + confirmDateIdx).setCellValue(confirmDate);
            ExcelPoiUtil.getCellRef(sheet, "W" + confirmDateIdx).setCellValue(confirmDate);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("qt_report_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }


    /** 시험일지 Excel **/
    public ResponseEntity<Resource> getQtJournal (String qualityTestId) throws Exception {

        QualityTestView qualityTestView = qualityTestViewService.getById(qualityTestId);
        List<QualityTestMethodView> qualityTestMethodViews = qualityTestMethodViewService.getByQualityTestId(qualityTestId);
        String itemGb = qualityTestView.getItemGb();
        List<CommonCode> commonCodeList = commonCodeService.getByCodeType(this.getDocNo(itemGb, "B"));

        try {
            //양식 로드
            InputStream excelStream = getClass().getResourceAsStream("/excel/qt_journal_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            Sheet sheet = workbook.getSheet("Sheet1");
            Footer footer = sheet.getFooter();

            // 공통 항목 처리
            for (CommonCode item : commonCodeList) {
                if (item.getCode().equals("DOC_NO")) {
                    footer.setLeft(item.getCodeName());
                } else {
                    ExcelPoiUtil.getCellRef(sheet, item.getCode()).setCellValue(item.getCodeName());
                }
            }

            // D6 품명
            ExcelPoiUtil.getCellRef(sheet, "D6").setCellValue(qualityTestView.getItemName());
            // D8 제조번호 or Lot No.        prodNo : 반제품, lotNo  : 원재료, 부자재, 완제품
            ExcelPoiUtil.getCellRef(sheet, "D8").setCellValue(
                    (itemGb.equals("3"))? qualityTestView.getProdNo() : qualityTestView.getLotNo()
            );
            // P8 시험번호
            ExcelPoiUtil.getCellRef(sheet, "P8").setCellValue(qualityTestView.getTestNo());
            qualityTestView.getProdNo();

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 10;     // 출력시작 Row,  POI row는 0부터 시작
            short rowHeight = 1310;
            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 19);
            for (QualityTestMethodView item : qualityTestMethodViews){
                //시험항목
                ExcelPoiUtil.getRow(sheet, rowNo).setHeight(rowHeight);

                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(item.getTestItem());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue("");
                sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 1));

                //시험방법
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(item.getTestMethod());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue("");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue("");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue("");
                sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 2, 5));

                //시험기준
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(item.getTestSpec());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue("");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue("");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue("");
                sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 6, 9));

                //시험결과
//                ExcelPoiUtil.getStyleCell(sheet, rowNo, 10, cellStyleList.get(10)).setCellValue(item.getTestResult());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 10, cellStyleList.get(10)).setCellValue("");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 11, cellStyleList.get(11)).setCellValue("");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 12, cellStyleList.get(12)).setCellValue("");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 13, cellStyleList.get(13)).setCellValue("");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 14, cellStyleList.get(14)).setCellValue("");
                sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 10, 14));

                //시험일자
//                ExcelPoiUtil.getStyleCell(sheet, rowNo, 15, cellStyleList.get(15)).setCellValue(item.getTestDateString());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 15, cellStyleList.get(15)).setCellValue("");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 16, cellStyleList.get(16)).setCellValue("");
                sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 15, 16));

                //시험자
//                ExcelPoiUtil.getStyleCell(sheet, rowNo, 17, cellStyleList.get(17)).setCellValue(item.getTestMemberName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 17, cellStyleList.get(17)).setCellValue("");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 18, cellStyleList.get(18)).setCellValue("");
                sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 17, 18));

                rowNo++;
            }

            int size = JOURNAL_LINE_CNT - (qualityTestMethodViews.size()%JOURNAL_LINE_CNT);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < 19; j++){
                    ExcelPoiUtil.getStyleCell(sheet, rowNo, j, cellStyleList.get(j)).setCellValue("");
                }
                ExcelPoiUtil.getRow(sheet, rowNo).setHeight(rowHeight);
                sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 0, 1));
                sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 2, 5));
                sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 6, 9));
                sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 10, 14));
                sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 15, 16));
                sheet.addMergedRegion(new CellRangeAddress(rowNo, rowNo, 17, 18));
                rowNo++;
            }

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("qt_journal", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    /** 문서번호 Get **/
    public String getDocNo (String itemGb, String type) {
        // 원재료 : JQP01-02 // 부재료 : JQP02-02 // 반제품 : JQP03-02 // 완제품 : JQP04-02
        String docNo = "";
        switch (itemGb) {
            case "1" : docNo = (type.equals("A"))? "JQP01-01" : "JQP01-02"; break;
            case "2" : docNo = (type.equals("A"))? "JQP02-01" : "JQP02-02"; break;
            case "3" : docNo = (type.equals("A"))? "JQP03-01" : "JQP03-02"; break;
            case "6" : docNo = (type.equals("A"))? "JQP04-01" : "JQP04-02"; break;
            default:   docNo = (type.equals("A"))? "JQP04-01" : "JQP04-02"; break;
        }
        return docNo;
    }

    public String getTemplateName (String itemGb, int size) {
        String template = "";
        switch (itemGb) {
            //페이지 구분 기준 : 원료, 자재, 완제품 15  반제품 16
            case "1" : template = (size <= 15)? "qt_report_m1_page1" : "qt_report_m1_page2"; break;
            case "2" : template = (size <= 15)? "qt_report_m2_page1" : "qt_report_m2_page2"; break;
            case "3" : template = (size <= 16)? "qt_report_m3_page1" : "qt_report_m3_page2"; break;
            case "6" : template = (size <= 15)? "qt_report_m6_page1" : "qt_report_m6_page2"; break;
            default:   template = (size <= 15)? "qt_report_m6_page1" : "qt_report_m6_page2"; break;
        }

        return template;
    }

    public Map<String, Object> getCellValueList (String itemGb, QualityTestView qualityTestView) {
        Map<String, Object> result = new HashMap<>();

        //기본 데이터 정리
        String itemName = (qualityTestView.getItemName() != null)? qualityTestView.getItemName() : "";
        String lotNo = (qualityTestView.getLotNo() != null)?   qualityTestView.getLotNo() : "";
        if(!lotNo.equals("")) lotNo.replaceAll("!!", " ");
        String createDate = (qualityTestView.getCreateDate() != null)? qualityTestView.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) : "";
        String dept = (itemGb.equals("1") || itemGb.equals("2"))? "구매부" : "생산부";
        String orderMember = (qualityTestView.getOrderMemberCd() != null)? qualityTestView.getOrderMemberCd() : "";
        String reqDate = (qualityTestView.getReqDate() != null)? qualityTestView.getReqDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) : "";
        double reqQty = (qualityTestView.getReqQty() != null)? qualityTestView.getReqQty().doubleValue() : 0;
        String reqMember = (qualityTestView.getReqMemberCd() != null)? qualityTestView.getReqMemberCd() : "";
        String testMember = (qualityTestView.getTestMemberCd() != null)? qualityTestView.getTestMemberCd() : "";
        String sampleMember = (qualityTestView.getSampleMemberCd() != null)? qualityTestView.getSampleMemberCd() : "";
        String itemCd = (qualityTestView.getItemCd() != null)? qualityTestView.getItemCd(): "";
        String customer = (qualityTestView.getCustomerName() != null)? qualityTestView.getCustomerName() : "";
        String testNo = (qualityTestView.getTestNo() != null)? qualityTestView.getTestNo() : "";
        if (StringUtils.isNotBlank(qualityTestView.getRetestYn()) && qualityTestView.getRetestYn().equals("Y")) {
            testNo = testNo + " - " + qualityTestView.getRetestSerNo();
        }
        double sampleQty = (qualityTestView.getSampleQty() != null)? qualityTestView.getSampleQty().doubleValue() : 0;
        String prodNo = (qualityTestView.getProdNo() != null)? qualityTestView.getProdNo() : "";
        String storageName = (qualityTestView.getStorageName() != null)? qualityTestView.getStorageName() : "자재 대기장소";

        if(itemGb.equals("1")){
            result.put("E7", itemName);
            result.put("E8", lotNo);
            result.put("E9", createDate);
            result.put("E10", dept);
            result.put("E11", orderMember);
            result.put("E12", reqDate);
            result.put("M9", reqQty);
            result.put("M10", reqMember);
            result.put("M11", testMember);
            result.put("M12", sampleMember);
            result.put("U9", itemCd);
            result.put("U10", customer);
            result.put("U11", testNo);
            result.put("U12", sampleQty);
        }
        if(itemGb.equals("2")) {
            ItemMasterView itemMasterView = itemMasterViewService.getById(qualityTestView.getItemCd());
            String itemType1 = (itemMasterView.getItemGrp3Name() != null) ? itemMasterView.getItemGrp3Name() : "";
            String itemType2 = (itemMasterView.getItemGrp1Name() != null) ? itemMasterView.getItemGrp1Name() : "";
            result.put("E7", itemName);
            result.put("E8", itemCd);
            result.put("E9", createDate);
            result.put("E10", dept);
            result.put("E11", orderMember);
            result.put("E12", reqDate);
            result.put("M8", itemType1);
            result.put("M9", reqQty);
            result.put("M10", reqMember);
            result.put("M11", testMember);
            result.put("M12", sampleMember);
            result.put("U8", testNo);
            result.put("U9", itemType2);
            result.put("U10", customer);
            result.put("U11", storageName);
            result.put("U12", sampleQty);
        }
        if(itemGb.equals("3")) {
            result.put("E7", itemName);
            result.put("E8", createDate);
            result.put("E9", dept);
            result.put("E10", orderMember);
            result.put("E11", reqDate);
            result.put("M8", reqQty);
            result.put("M9", reqMember);
            result.put("M10", testMember);
            result.put("M11", sampleMember);
            result.put("U8", itemCd);
            result.put("U9", prodNo);
            result.put("U10", testNo);
            result.put("U11", sampleQty);
        }
        if(itemGb.equals("6")) {
            result.put("E7", itemName);
            result.put("E8", lotNo);
            result.put("E9", reqDate);
            result.put("E10", dept);
            result.put("E11", orderMember);
            result.put("E12", reqDate);
            result.put("M9", reqQty);
            result.put("M10", reqMember);
            result.put("M11", testMember);
            result.put("M12", sampleMember);
            result.put("U9", itemCd);
            result.put("U10", prodNo);
            result.put("U11", testNo);
            result.put("U12", sampleQty);
        }
        return result;
    }
}
