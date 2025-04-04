package com.daehanins.mes.biz.common.controller;

import com.daehanins.mes.biz.common.vo.Barcode;
import com.daehanins.mes.common.utils.BarcodeUtil;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/excel")
public class ExcelController {

    @GetMapping(value = "/sample1", produces=MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public  @ResponseBody byte[]  exportExcel(String reportFormat) throws FileNotFoundException, JRException {
        Workbook workbook = ExcelPoiUtil.createWorkbook();
        Sheet sheet = workbook.createSheet("Test Sheet");
        ExcelPoiUtil.getCell(sheet, 0, 0).setCellValue("TEST Result");
        ExcelPoiUtil.getCell(sheet, 0, 1).setCellValue(100);
        Cell cell = ExcelPoiUtil.getCell(sheet, 0, 2);
//        cell.setCellValue(Calendar.getInstance().getTime());
//        cell.setCellValue(LocalDateTime.now());


        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setFillBackgroundColor(IndexedColors.GOLD.index);
        Font font = workbook.createFont();
        font.setColor(IndexedColors.RED.index);
        cell.setCellStyle(style);

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        ExcelPoiUtil.getCell(sheet, 1, 0).setCellValue(1);
        ExcelPoiUtil.getCell(sheet, 1, 1).setCellValue(2);
        ExcelPoiUtil.getCell(sheet, 1, 2).setCellFormula("SUM(A2:B2)");

        return ExcelPoiUtil.toByteArray(workbook);
    }

    @GetMapping(value = "/sample2")
    public  ResponseEntity<Resource>  exportExcel2(String reportFormat) throws FileNotFoundException, JRException {
        Workbook workbook = ExcelPoiUtil.createWorkbook();
        Sheet sheet = workbook.createSheet("Test Sheet");
        ExcelPoiUtil.getCell(sheet, 0, 0).setCellValue("TEST Result");
        ExcelPoiUtil.getCell(sheet, 0, 1).setCellValue(100);
        Cell cell = ExcelPoiUtil.getCell(sheet, 0, 2);
        cell.setCellValue(Calendar.getInstance().getTime());

        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setFillBackgroundColor(IndexedColors.GOLD.index);
        Font font = workbook.createFont();
        font.setColor(IndexedColors.RED.index);
        cell.setCellStyle(style);

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        for (int i = 1; i < 10; i++) {
            ExcelPoiUtil.getCell(sheet, i, 0).setCellValue(1);
            ExcelPoiUtil.getCell(sheet, i, 1).setCellValue(2);
            ExcelPoiUtil.getCell(sheet, i, 2).setCellFormula("SUM(A" + (i+1) + ":B" + (i+1) + ")");
        }

        byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
        ByteArrayResource resource = new ByteArrayResource(excelContent);

        String fileName = "sample2";
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "vnd.ms-excel"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".xlsx");
        header.setContentLength(excelContent.length);

        return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
    }
}
