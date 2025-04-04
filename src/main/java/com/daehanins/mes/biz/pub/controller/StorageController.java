package com.daehanins.mes.biz.pub.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.pub.entity.Area;
import com.daehanins.mes.biz.pub.entity.CustomerView;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import com.daehanins.mes.common.utils.JasperUtil;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.utils.SearchUtil;
import com.daehanins.mes.common.vo.RestResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import com.daehanins.mes.biz.pub.entity.Storage;
import com.daehanins.mes.biz.pub.entity.StorageView;
import com.daehanins.mes.biz.pub.service.IStorageService;
import com.daehanins.mes.biz.pub.service.IStorageViewService;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 창고Storage Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-04
 */
@RestController
@RequestMapping("/pub/storage")
public class StorageController extends BaseController<Storage, StorageView, String> {

    @Autowired
    private IStorageService storageService;

    @Autowired
    private IStorageViewService storageViewService;

    @Override
    public IStorageService getTableService() {
        return this.storageService;
    }

    @Override
    public IStorageViewService getViewService() {
        return this.storageViewService;
    }

    @RequestMapping(value = "/getWeigh",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<Storage>> getWeigh(){

        QueryWrapper<Storage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("storage_type", "B");
        List<Storage> list = getTableService().list(queryWrapper);
        return new RestUtil<List<Storage>>().setData(list);
    }

    @RequestMapping(value = "/getType/{stype}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<Storage>> getType(@PathVariable String stype){

        QueryWrapper<Storage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_cd", stype)
                .orderByAsc("area_cd")
                .orderByAsc("storage_cd");
        List<Storage> list = getTableService().list(queryWrapper);
        return new RestUtil<List<Storage>>().setData(list);
    }

    @RequestMapping(value = "/getTypeStock",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<Storage>> getTypeStock(){

        QueryWrapper<Storage> queryWrapper = new QueryWrapper<Storage>()
                .in("storage_type", "A","B")
                .orderByAsc("area_cd")
                .orderByAsc("storage_cd");
        List<Storage> list = getTableService().list(queryWrapper);
        return new RestUtil<List<Storage>>().setData(list);
    }


    @RequestMapping(value = "/getPdf",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> getPdf(@RequestParam(name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<StorageView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<StorageView> storageList = getViewService().list(queryWrapper);

        try {
            InputStream reportStream = getClass().getResourceAsStream("/report/storage_list.jrxml");
            byte[] pdfContent = JasperUtil.getPdfBinary(reportStream, storageList);
            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("customer_list", pdfContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("리포트 생성중 에러발생!");
        }
    }

    @RequestMapping(value = "/getExcel",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> getExcel(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<StorageView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<StorageView> storageList = getViewService().list(queryWrapper);
        try {
            // 엑셀양식 로드
            InputStream excelStream = getClass().getResourceAsStream("/excel/storage_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("Sheet1");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 14);
            for (StorageView storage : storageList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(lineNo++);   // 줄번호
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(storage.getStorageCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(storage.getStorageName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(storage.getAreaName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(storage.getProcessName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(storage.getProdYnName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue((storage.getAYn().equals("Y"))? "O" : "");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue((storage.getBYn().equals("Y"))? "O" : "");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue((storage.getCYn().equals("Y"))? "O" : "");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue((storage.getDYn().equals("Y"))? "O" : "");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 10, cellStyleList.get(10)).setCellValue((storage.getEYn().equals("Y"))? "O" : "");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 11, cellStyleList.get(11)).setCellValue((storage.getFYn().equals("Y"))? "O" : "");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 12, cellStyleList.get(12)).setCellValue(storage.getUseYnName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 13, cellStyleList.get(13)).setCellValue("");
                rowNo++;
            }

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("storage_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    private HttpHeaders getExcelHeader(String fileName, int contentsLength) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "vnd.ms-excel"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".xlsx");
        header.setContentLength(contentsLength);
        return header;
    }
}

