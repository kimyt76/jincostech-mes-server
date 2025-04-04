package com.daehanins.mes.biz.qt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.mat.entity.MatOrderView;
import com.daehanins.mes.biz.mat.vo.MatLabelItem;
import com.daehanins.mes.biz.qt.entity.ItemTestNo;
import com.daehanins.mes.biz.qt.entity.ItemTestNoView;
import com.daehanins.mes.biz.qt.service.IItemTestNoService;
import com.daehanins.mes.biz.qt.service.IItemTestNoViewService;
import com.daehanins.mes.biz.qt.vo.ReqPrinting;
import com.daehanins.mes.biz.tag.entity.TagInfo;
import com.daehanins.mes.common.exception.BizException;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import com.daehanins.mes.common.utils.JasperUtil;
import com.daehanins.mes.common.utils.SearchUtil;
import org.apache.poi.ss.usermodel.*;
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
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 품목시험번호ItemTestNo Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-03
 */
@RestController
@RequestMapping("/qt/item-test-no")
public class ItemTestNoController extends BaseController<ItemTestNo, ItemTestNoView, String> {

    @Autowired
    private IItemTestNoService itemTestNoService;

    @Autowired
    private IItemTestNoViewService itemTestNoViewService;

    @Override
    public IItemTestNoService getTableService() {
        return this.itemTestNoService;
    }

    @Override
    public IItemTestNoViewService getViewService() {
        return this.itemTestNoViewService;
    }

    @RequestMapping(value = "/printQrCode", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Resource> printMatLabel(@RequestBody ReqPrinting[] reqPrintings) throws Exception {

        try {
            byte[] pdfContent = itemTestNoService.createQrCodeLabels(reqPrintings);
            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("qrcode_label_list", pdfContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BizException("QR코드 생성 중 에러발생");
        }
    }

    @RequestMapping(value = "/getExcel",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> getExcel(@RequestParam(name="condition", required=false ) String conditionJson) throws Exception {

        Map<String, Object> paramMap = SearchUtil.parseParam(conditionJson);
        QueryWrapper<ItemTestNoView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        queryWrapper.orderByDesc("create_date").orderByDesc("test_no");
        List<ItemTestNoView> data = getViewService().list(queryWrapper);

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/item_test_no_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("Sheet1");

            // 기본 변수 선언
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            String excelTitle = "회사명: (주)진코스텍 / " + paramMap.get("startDate") + " ~ " + paramMap.get("endDate");
            ExcelPoiUtil.getCell(sheet, 0, 0).setCellValue(excelTitle);
            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 10);
            for (ItemTestNoView item : data){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(lineNo);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(item.getTestNo());
                String createDate = (item.getCreateDate() != null)? item.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(createDate);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(item.getItemCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(item.getItemName());
                String lotNo = (item.getLotNo() != null)? item.getLotNo().replaceAll("!!", " ") : "";
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(lotNo);
                double qty = (item.getQty() != null)? item.getQty().doubleValue() : 0;
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(qty);
                String expiryDate = (item.getExpiryDate() != null)? item.getExpiryDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(expiryDate);
                String customer = (item.getCustomerName() != null)? item.getCustomerName() : "";
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue(customer);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue(item.getPassStateName());
                rowNo++;
                lineNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("item_test_no_list", excelContent.length, "xlsx");

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

}
