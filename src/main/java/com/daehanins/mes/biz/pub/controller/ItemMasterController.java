package com.daehanins.mes.biz.pub.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.pub.entity.Customer;
import com.daehanins.mes.biz.pub.entity.CustomerView;
import com.daehanins.mes.biz.pub.entity.ItemMaster;
import com.daehanins.mes.biz.pub.entity.ItemMasterView;
import com.daehanins.mes.biz.pub.service.IItemMasterService;
import com.daehanins.mes.biz.pub.service.IItemMasterViewService;
import com.daehanins.mes.biz.work.vo.SearchPeriod;
import com.daehanins.mes.biz.work.vo.UsageByItems;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import com.daehanins.mes.common.utils.JasperUtil;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.utils.SearchUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * <p>
 * 품목마스터ItemMaster Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-06
 */
@RestController
@RequestMapping("/pub/item-master")
public class ItemMasterController extends BaseController<ItemMaster, ItemMasterView, String> {

    @Autowired
    private IItemMasterService itemMasterService;

    @Autowired
    private IItemMasterViewService itemMasterViewService;

    @Override
    public IItemMasterService getTableService() {
        return this.itemMasterService;
    }

    @Override
    public IItemMasterViewService getViewService() {
        return this.itemMasterViewService;
    }

    @RequestMapping(value = "/getPdf",method = RequestMethod.GET)
    public ResponseEntity<Resource> getPdf(@RequestParam(name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<ItemMasterView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<ItemMasterView> itemMasterList = getViewService().list(queryWrapper);

        try {
            InputStream reportStream = getClass().getResourceAsStream("/report/item_master_list.jrxml");
            byte[] pdfContent = JasperUtil.getPdfBinary(reportStream, itemMasterList);
            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("item_master_list", pdfContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("리포트 생성중 에러발생!");
        }
    }

    @RequestMapping(value = "/getExcel",method = RequestMethod.GET)
    public ResponseEntity<Resource> getExcel(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<ItemMasterView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<ItemMasterView> itemMasterList = getViewService().list(queryWrapper);
        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/item_master_erp_list.xls");
            Workbook workbook = ExcelPoiUtil.createHssfWorkbook(excelStream);
//            File file = ResourceUtils.getFile("classpath:excel/item_master_erp_list.xls");
//            Workbook workbook = ExcelPoiUtil.createHssfWorkbook(new FileInputStream(file));
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("품목리스트");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 15);
            for (ItemMasterView itemMaster : itemMasterList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
//                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(lineNo++);   // 줄번호
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(itemMaster.getItemCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(itemMaster.getItemName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(itemMaster.getErpItemTypeName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(itemMaster.getUnit());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(itemMaster.getCustomerName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(itemMaster.getItemGrp2Name());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(itemMaster.getItemGrp3Name());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(itemMaster.getSpec());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue(itemMaster.getBarcode());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue(itemMaster.getInPrice() != null ? itemMaster.getInPrice().doubleValue() : 0);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 10, cellStyleList.get(10)).setCellValue(itemMaster.getOutPrice() != null ? itemMaster.getOutPrice().doubleValue() : 0);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 11, cellStyleList.get(11)).setCellValue(itemMaster.getItemGrp1Name());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 12, cellStyleList.get(12)).setCellValue(itemMaster.getSearchText());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 13, cellStyleList.get(13)).setCellValue(itemMaster.findUseYnErpName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 14, cellStyleList.get(14)).setCellValue("파일관리");
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("item_master_list", excelContent.length, "xls");

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    @RequestMapping(value = "/getExcelNew",method = RequestMethod.GET)
    public ResponseEntity<Resource> getExcelNew(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<ItemMasterView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<ItemMasterView> itemMasterList = getViewService().list(queryWrapper);

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/item_master_list_new.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("품목리스트");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 19);
            for (ItemMasterView itemMaster : itemMasterList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
//                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(lineNo++);   // 줄번호
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(itemMaster.getItemCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(itemMaster.getItemName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(itemMaster.getErpItemTypeName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(itemMaster.getUnit());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(itemMaster.getCustomerName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(itemMaster.getItemGrp2Name());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(itemMaster.getItemGrp3Name());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(itemMaster.getSpec());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue(itemMaster.getBarcode());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue(itemMaster.getInPrice() != null ? itemMaster.getInPrice().doubleValue() : 0);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 10, cellStyleList.get(10)).setCellValue(itemMaster.getOutPrice() != null ? itemMaster.getOutPrice().doubleValue() : 0);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 11, cellStyleList.get(11)).setCellValue(itemMaster.getItemGrp1Name());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 12, cellStyleList.get(12)).setCellValue(itemMaster.getSearchText());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 13, cellStyleList.get(13)).setCellValue(itemMaster.findUseYnErpName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 14, cellStyleList.get(14)).setCellValue("파일관리");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 15, cellStyleList.get(15)).setCellValue(itemMaster.getItemCondition());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 16, cellStyleList.get(16)).setCellValue(itemMaster.getAppearance());
                String safeStockYn = (itemMaster.getUseSafeStockYn().equals("Y"))? "사용" : "미사용";
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 17, cellStyleList.get(17)).setCellValue(safeStockYn);
                double safeStockQty = (itemMaster.getSafeStockQty() != null)? itemMaster.getSafeStockQty().doubleValue() : 0;
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 18, cellStyleList.get(18)).setCellValue(safeStockQty);
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("item_master_list_new", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    @RequestMapping(value = "/saveItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<List<ItemMaster>> saveItems(@RequestBody List<ItemMaster> itemMasterList){

        List<ItemMaster> data = getTableService().saveItems(itemMasterList);

        return new RestUtil<List<ItemMaster>>().setData(data);
    }
}

