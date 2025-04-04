package com.daehanins.mes.common.utils;

import org.apache.commons.jexl3.JxltEngine;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jeonsj
 */
public class ExcelPoiUtil {

    private static int ROW_ACCESS_WINDOW_SIZE = 100;
    /**
     * TODO::
     *      setCellFormula() 에 사용되는 구문을 쉽게 조합할 수 있는 기능 제공
     *
     *
     */
    public static Workbook createWorkbook() {

        return new XSSFWorkbook();
    }

    public static Workbook createWorkbook(InputStream fileStream) throws Exception {
        return new XSSFWorkbook(fileStream);
    }

    public static Workbook createHssfWorkbook(InputStream fileStream) throws Exception {
        return new HSSFWorkbook(fileStream);
    }

    public static Workbook createSxssfWorkbook(XSSFWorkbook xssfWorkbook) {

        return new SXSSFWorkbook(xssfWorkbook, ROW_ACCESS_WINDOW_SIZE);
    }

    public static Workbook getWorkbook(String filename) {
        try (FileInputStream stream = new FileInputStream(filename)) {
            return new XSSFWorkbook(stream);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Workbook getHssfWorkbook(String filename) {
        try (FileInputStream stream = new FileInputStream(filename)) {
            return new HSSFWorkbook(stream);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Row getRow(Sheet sheet, int rownum) {
        Row row = sheet.getRow(rownum);
        if (row == null) {
            row = sheet.createRow(rownum);
        }
        return row;
    }

    public static List<CellStyle> getRowCellStyle(Sheet sheet, int rownum, int columnCount) {
        List<CellStyle> cellStyleList = new ArrayList<>();
        for (int i = 0; i < columnCount; i++ ) {
            cellStyleList.add(getCell(sheet, rownum, i).getCellStyle());
        }
        return cellStyleList;
    }

    public static Cell getCell(Row row, int cellnum) {
        Cell cell = row.getCell(cellnum);
        if (cell == null) {
            cell = row.createCell(cellnum);
        }
        return cell;
    }

    public static Cell getCellRef (Sheet sheet, String ref) {
        CellReference cellReference = new CellReference(ref);
        Row row = sheet.getRow(cellReference.getRow());
        Cell cell = row.getCell(cellReference.getCol());
        return cell;
    }

    public static Cell getCell(Sheet sheet, int rownum, int cellnum) {
        Row row = getRow(sheet, rownum);
        return getCell(row, cellnum);
    }

    public static Cell getStyleCell(Sheet sheet, int rownum, int cellnum, CellStyle style) {
        Row row = getRow(sheet, rownum);
        Cell cell = getCell(row, cellnum);
        cell.setCellStyle(style);
        return cell;
    }

    public static HttpHeaders getHeader(String fileName, int contentsLength) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "vnd.ms-excel"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".xlsx");
        header.setContentLength(contentsLength);
        return header;
    }

    public static HttpHeaders getHeader(String fileName, int contentsLength, String ext) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "vnd.ms-excel"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".xls");
        header.setContentLength(contentsLength);
        return header;
    }

    public static byte[] toByteArray(Workbook workbook) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            workbook.write(baos);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return baos.toByteArray();
    }


    public static  void writeExcel(Workbook workbook, String filepath) {
        try (FileOutputStream stream = new FileOutputStream(filepath)) {
            workbook.write(stream);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
