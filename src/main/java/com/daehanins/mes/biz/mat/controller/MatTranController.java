package com.daehanins.mes.biz.mat.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.adm.entity.ErpConfig;
import com.daehanins.mes.biz.adm.service.IErpConfigService;
import com.daehanins.mes.biz.common.code.ItemTypeCd;
import com.daehanins.mes.biz.common.vo.*;
import com.daehanins.mes.biz.mat.entity.*;
import com.daehanins.mes.biz.mat.service.IMatTranItemService;
import com.daehanins.mes.biz.mat.service.IMatTranItemViewService;
import com.daehanins.mes.biz.mat.service.IMatTranService;
import com.daehanins.mes.biz.mat.service.IMatTranViewService;
import com.daehanins.mes.biz.mat.vo.*;
import com.daehanins.mes.biz.pub.entity.ItemMaster;
import com.daehanins.mes.biz.pub.service.IItemMasterService;
import com.daehanins.mes.biz.qt.entity.ItemTestNoView;
import com.daehanins.mes.biz.qt.service.IItemTestNoViewService;
import com.daehanins.mes.common.exception.BizException;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import com.daehanins.mes.common.utils.JasperUtil;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.utils.SearchUtil;
import com.daehanins.mes.common.vo.RestResponse;
import com.fasterxml.jackson.databind.JsonNode;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 자재거래MatTran Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-09
 */
@RestController
@RequestMapping("/mat/mat-tran")
public class MatTranController extends BaseController<MatTran, MatTranView, String> {

    @Autowired
    private IMatTranService matTranService;

    @Autowired
    private IMatTranViewService matTranViewService;

    @Autowired
    private IMatTranItemService matTranItemService;

    @Autowired
    private IMatTranItemViewService matTranItemViewService;

    @Autowired
    private IItemMasterService itemMasterService;

    @Autowired
    private IItemTestNoViewService itemTestNoViewService;

    @Override
    public IMatTranService getTableService() {
        return this.matTranService;
    }

    @Override
    public IMatTranViewService getViewService() {
        return this.matTranViewService;
    }

    @Autowired
    WebClient webClient;

    @Autowired
    IErpConfigService erpConfigService;

    @RequestMapping(value = "/getWithItems/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatTranReadWithItems> getWithItems(@PathVariable String id){
        MatTranReadWithItems mtItems = new MatTranReadWithItems();
        MatTran matTran = getTableService().getById(id);

        QueryWrapper<MatTranItemView> queryWrapper = new QueryWrapper<MatTranItemView>().eq(StringUtils.camelToUnderline("matTranId"), id);
        List<MatTranItemView> matTranItems = this.matTranItemViewService.list(queryWrapper);
        mtItems.setMatTran(matTran);
        mtItems.setMatTranItems(matTranItems);
        return new RestUtil<MatTranReadWithItems>().setData(mtItems);
    }

    // matOrderId로 matTran내역을 구함
    @RequestMapping(value = "/getWithItemsByOrderId/{matOrderId}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatTranReadWithItems> getWithItemsByOrderId(@PathVariable String matOrderId){
        MatTranReadWithItems mtItems = new MatTranReadWithItems();
        MatTran matTran = getTableService().getOne(new QueryWrapper<MatTran>().eq("mat_order_id", matOrderId));
        mtItems.setMatTran(matTran);

        if (matTran != null) {
            String matTranId = matTran.getMatTranId();
            QueryWrapper<MatTranItemView> queryWrapper = new QueryWrapper<MatTranItemView>().eq(StringUtils.camelToUnderline("matTranId"), matTran.getMatTranId());
            List<MatTranItemView> matTranItems = this.matTranItemViewService.list(queryWrapper);
            mtItems.setMatTranItems(matTranItems);
        }
        return new RestUtil<MatTranReadWithItems>().setData(mtItems);
    }

    @RequestMapping(value = "/saveWithItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<MatTranSaveWithItems> saveWithItems(@RequestBody MatTranSaveWithItems requestParam){

        MatTran matTran = requestParam.getMatTran();
        List<MatTranItem> matTranItems = requestParam.getMatTranItems();
        List<MatTranItem> matTranDeleteItems = requestParam.getMatTranDeleteItems();

        MatTranSaveWithItems data = getTableService().saveWithItems(matTran, matTranItems, matTranDeleteItems);

        return new RestUtil<MatTranSaveWithItems>().setData(data);
    }

    @RequestMapping(value = "/deleteWithItems/{ids}",method = {RequestMethod.DELETE, RequestMethod.POST})
    @ResponseBody
    public RestResponse<Object> deleteWithItems(@PathVariable String[] ids){
        List<String> idList = Arrays.asList(ids);

        String msg = getTableService().deleteWithItems(idList);

        return new RestUtil<>().setMessage(msg);
    }

    @RequestMapping(value = "/erpSavePurchase/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatTran> erpSavePurchase(@PathVariable String id) throws Exception {
        MatTranView tran = getViewService().getById(id);
        List<MatTranItemView> tranItemList = matTranItemViewService.list(new QueryWrapper<MatTranItemView>().eq("mat_tran_id", id));

        ErpPurchaseListVo purchaseListVo = new ErpPurchaseListVo();
        int lineNo = 0;
        for (MatTranItemView tranItem: tranItemList) {
            ErpPurchase purchase = new ErpPurchase();
            purchase.setIO_DATE(tran.getTranDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));    // 입고일자
            purchase.setUPLOAD_SER_NO("1");                     // 업로드 그룹번호
            purchase.setCUST(tran.getCustomerCd());             // 거래처코드
            purchase.setCUST_DES(tran.getCustomerName());       // 거래처명
            purchase.setWH_CD(tran.getSrcStorageCd());          // 입고창고
            purchase.setEMP_CD(tran.getMemberCd());             // 담당자
            purchase.setU_MEMO1(tran.getOrderType());           // 발주구분
            purchase.setPROD_CD(tranItem.getItemCd());          // 품목코드
            purchase.setPROD_DES(tranItem.getItemName());       // 품목명
            purchase.setSIZE_DES(tranItem.getSpec());           // 규격
            purchase.setQTY(tranItem.getQty().toString());      // 수량
            purchase.setPRICE(tranItem.getPrice().toString());  // 단가
            purchase.setSUPPLY_AMT(tranItem.getSupplyAmt().toString()); // 공급가
            purchase.setVAT_AMT(tranItem.getVat().toString());          // 부가세
            lineNo++;
            purchaseListVo.getPurchasesList().add(new ErpPurchaseLine(String.valueOf(lineNo), purchase));
        }

//        String data = savePurchasesList(purchaseListVo);

        JsonNode res = webClient.post().uri("/Purchases/SavePurchases?SESSION_ID=" + getErpSessionId())
                .body(Mono.just(purchaseListVo), ErpPurchaseListVo.class)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        // 서버오류 검사
        String status = res.get("Status").textValue();
        if (!status.equals("200")) {
            throw new BizException("ERP 서버접속중 오류");
        }

        // 라인별 발생한 오류메시지 구함
        int failCnt = res.get("Data").get("FailCnt").intValue();
        String message = "";
        if (failCnt > 0) {
            // 결과 detail array를 iterate하여 에러코드 수집
            Iterator<JsonNode> it = res.get("Data").get("ResultDetails").iterator();
            while (it.hasNext()) {
                JsonNode detailItem = it.next();
                if (!detailItem.get("IsSuccess").booleanValue()) {
                    message += ", line[" + detailItem.get("Line").intValue() + "] " + detailItem.get("TotalError").textValue();
                }
            }
            throw new BizException("전표생성중 에러발생:" + message);
        }

        // ERP에 생성된 전표번호 구함
        String slipNo = "";
        JsonNode slipNos = res.get("Data").get("SlipNos");
        for (final JsonNode slipNoNode : slipNos) {
            slipNo = slipNoNode.textValue();
            break;
        }

        // ErpYn , 전표번호를 구매입고에 반영
        MatTran tranEntity = getTableService().getById(id);
        tranEntity.setErpYn("Y");
        tranEntity.setEndYn("Y");
        if (slipNo != "") {
            String memo = "[ERP:" + slipNo + "]" + tranEntity.getMemo();
            tranEntity.setMemo(memo);
        }
        getTableService().updateById(tranEntity);

        return new RestUtil<MatTran>().setData(tranEntity);
    }

    private String getErpSessionId() throws Exception {

        ErpConfig sys = erpConfigService.getById("64301");

        ErpConfigVo configVo = new ErpConfigVo();
        configVo.setCOM_CODE(sys.getComCode());
        configVo.setUSER_ID(sys.getUserId());
        configVo.setAPI_CERT_KEY(sys.getApiCertKey());
        configVo.setLAN_TYPE(sys.getLanType());
        configVo.setZONE(sys.getZone());

        JsonNode res = webClient.post().uri("/OAPILogin")
                .body(Mono.just(configVo), ErpConfigVo.class)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        String code = res.get("Data").get("Code").textValue();
        String sessionId = "";
        if (code.equals("00")) {
            sessionId = res.findValue("SESSION_ID").textValue();
        } else {
            String message = res.findValue("Message").textValue();
            throw new Exception("ERP로그인 실패::" + message);
        }
        return sessionId;
    }

    @RequestMapping(value = "/printMatLabel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Resource> printMatLabel(@RequestBody MatLabelItem[] labelItems) throws Exception {

        List<LabelPrint> labelPrintList = new ArrayList<>();

        DecimalFormat df = new DecimalFormat("#,##0.000");
        try {
            for (MatLabelItem labelItem : labelItems ) {
                ItemMaster itemMaster = this.itemMasterService.getById(labelItem.getItemCd());
                String itemTypeCd = itemMaster.getItemTypeCd();
                for (int i = 0; i < labelItem.getPrintCnt(); i++) {
                    ItemTestNoView itemTestNo = this.itemTestNoViewService.getById(labelItem.getTestNo());
                    LabelPrint labelPrint = new LabelPrint(
                            "(" + itemMaster.getItemCd() + ")",
                            itemMaster.getItemName(),
                            itemMaster.getItemCondition(),
                            labelItem.getTestNo(),
                            labelItem.getLotNo(),
                            labelItem.getExpiryDate(),
                            df.format(labelItem.getQty()) + " kg",
                            itemTestNo.getPassStateName()
                    );
                    labelPrint.setLabelTitle(ItemTypeCd.getLableTitle(itemTypeCd));
                    labelPrintList.add(labelPrint);
                }
            }
            InputStream reportStream = getClass().getResourceAsStream("/report/mat_prod_label_v3.jrxml");
            byte[] pdfContent = JasperUtil.getPdfBinary(reportStream, labelPrintList);

            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("mat_label_list", pdfContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BizException("리포트에서 에러발생");
        }
    }

    @RequestMapping(value = "/printMatProcLabel/{itemTestNo}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> printMatProcLabel(@PathVariable String itemTestNo) throws Exception {

        ItemTestNoView itemTestNoView = itemTestNoViewService.getById(itemTestNo);
        ItemMaster itemMaster = itemMasterService.getById(itemTestNoView.getItemCd());

        List<LabelPrint> labelPrintList = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#,##0.000");

        LabelPrint labelPrint = new LabelPrint(
                "(" + itemTestNoView.getItemCd() + ")",
                itemTestNoView.getItemName(),
                itemMaster.getItemCondition(),
                itemTestNoView.getTestNo(),
                itemTestNoView.getLotNo(),
                (itemTestNoView.getExpiryDate() == null)? "" : itemTestNoView.getExpiryDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                df.format(itemTestNoView.getQty()) + " kg",
                itemTestNoView.getPassStateName()
        );
        labelPrint.setLabelTitle(ItemTypeCd.getLableTitle(itemMaster.getItemTypeCd()));
        labelPrintList.add(labelPrint);

        try {
            InputStream reportStream = getClass().getResourceAsStream("/report/mat_prod_label_v3.jrxml");
            byte[] pdfContent = JasperUtil.getPdfBinary(reportStream, labelPrintList);

            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("mat_label_list", pdfContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BizException("리포트에서 에러발생");
        }
    }

    @RequestMapping(value = "/getPdf", method = RequestMethod.GET)
    public ResponseEntity<Resource> getPdf(@RequestParam(name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<MatTranView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<MatTranView> matTranViewList = matTranViewService.list(queryWrapper);

        try {
            InputStream reportStream = getClass().getResourceAsStream("/report/mat_in_list.jrxml");
            byte[] pdfContent = JasperUtil.getPdfBinary(reportStream, matTranViewList);
            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("mat_in_list", pdfContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("리포트 생성중 에러발생!");
        }
    }

    @RequestMapping(value = "/getExcel",method = RequestMethod.GET)
    public ResponseEntity<Resource> getExcel(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<MatTranView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        Map<String, Object> paramMap = SearchUtil.parseParam(conditionJson);
        queryWrapper.orderByDesc("tran_no");
        List<MatTranView> matTranViewList = matTranViewService.list(queryWrapper);
        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/mat_in_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("구매입고조회");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            String excelTitle = "회사명: (주)진코스텍 / " + paramMap.get("startDate") + " ~ " + paramMap.get("endDate");
            ExcelPoiUtil.getCell(sheet, 0, 0).setCellValue(excelTitle);
            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 14);
            for (MatTranView matTranView : matTranViewList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(matTranView.getTranNo());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(matTranView.getCustomerCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(matTranView.getCustomerName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(matTranView.getItemName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(matTranView.getSumQty().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(matTranView.getSumSupplyAmt().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(matTranView.getSrcStorageName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(matTranView.getOrderType());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue(matTranView.getErpYn().equals("Y")? "완료": "미연계");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue(matTranView.getMemberName());
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("mat_in_state_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    @RequestMapping(value = "/getExcelAdjust",method = RequestMethod.GET)
    public ResponseEntity<Resource> getExcelAdjust(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<MatTranView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        Map<String, Object> paramMap = SearchUtil.parseParam(conditionJson);
        queryWrapper.orderByDesc("tran_no");
        List<MatTranView> matTranViewList = matTranViewService.list(queryWrapper);
        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/mat_adjust_list.xlsx");
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
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 7);
            for (MatTranView matTranView : matTranViewList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(matTranView.getTranNo());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(matTranView.getTranName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(matTranView.getSrcStorageName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(matTranView.getItemName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(matTranView.getSumQty().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(matTranView.getMemberCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(matTranView.getMemo());
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("mat_in_state_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    @RequestMapping(value = "/getExcelMatMove",method = RequestMethod.GET)
    public ResponseEntity<Resource> getExcelMatMove(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<MatTranView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        Map<String, Object> paramMap = SearchUtil.parseParam(conditionJson);
        queryWrapper.orderByDesc("tran_no");
        List<MatTranView> matTranViewList = matTranViewService.list(queryWrapper);
        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/mat_move_list.xlsx");
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
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 8);
            for (MatTranView matTranView : matTranViewList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(matTranView.getTranNo());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(matTranView.getTranName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(matTranView.getSrcStorageName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(matTranView.getDestStorageName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(matTranView.getItemName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(matTranView.getSumQty().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(matTranView.getMemberCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(matTranView.getMemo());
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("mat_move_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

}
