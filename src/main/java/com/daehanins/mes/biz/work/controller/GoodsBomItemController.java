package com.daehanins.mes.biz.work.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.mat.entity.MatTranView;
import com.daehanins.mes.biz.work.entity.GoodsBomItem;
import com.daehanins.mes.biz.work.entity.GoodsBomItemView;
import com.daehanins.mes.biz.work.service.IGoodsBomItemService;
import com.daehanins.mes.biz.work.service.IGoodsBomItemViewService;
import com.daehanins.mes.biz.work.vo.*;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
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
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 제품BOM품목GoodsBomItem Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-23
 */
@RestController
@RequestMapping("/work/goods-bom-item")
public class GoodsBomItemController extends BaseController<GoodsBomItem, GoodsBomItemView, String> {

    @Autowired
    private IGoodsBomItemService goodsBomItemService;

    @Autowired
    private IGoodsBomItemViewService goodsBomItemViewService;

    @Override
    public IGoodsBomItemService getTableService() {
        return this.goodsBomItemService;
    }

    @Override
    public IGoodsBomItemViewService getViewService() {
    return this.goodsBomItemViewService;
    }

    /** 소요품목 조회 **/
    @RequestMapping(value = "/getCostsList/{itemCd}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<GoodsBomItemByCosts>> getGoodsBomItemByCostsList(@PathVariable String itemCd){
        List<GoodsBomItemByCosts> resultList = this.goodsBomItemService.getGoodsBomItemByCostsList(itemCd);
        return new RestUtil<List<GoodsBomItemByCosts>>().setData(resultList);
    }

    @RequestMapping(value = "/getSubCostsList/{itemCd}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<GoodsBomItemByCosts>> getGoodsBomItemByCostsListSub(@PathVariable String itemCd){
        List<GoodsBomItemByCosts> resultList = this.goodsBomItemService.getGoodsBomItemByCostsListSub(itemCd);
        return new RestUtil<List<GoodsBomItemByCosts>>().setData(resultList);
    }

    /** 소요품목조회 엑셀다운로드 **/
    @RequestMapping(value = "/getExcel/{itemCd}",method = RequestMethod.GET)
    public ResponseEntity<Resource> getExcel(@PathVariable String itemCd) throws Exception {

        List<GoodsBomItemByCosts> goodsBomItemByCostsList = this.goodsBomItemService.getGoodsBomItemByCostsList(itemCd);

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/consumed_item_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("Sheet1");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            String excelTitle = "회사명: (주)진코스텍 ";
            ExcelPoiUtil.getCell(sheet, 0, 0).setCellValue(excelTitle);
            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 6);
            for (GoodsBomItemByCosts goodsBomItemByCosts : goodsBomItemByCostsList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(goodsBomItemByCosts.getItemCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(goodsBomItemByCosts.getItemName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(goodsBomItemByCosts.getBomVer());
                double sumRatio = (goodsBomItemByCosts.getSumRatio() != null) ? goodsBomItemByCosts.getSumRatio().doubleValue() : 0;
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(sumRatio);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(goodsBomItemByCosts.getProdItemCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(goodsBomItemByCosts.getProdItemName());
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("consumed_item_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    @RequestMapping(value = "/getSubExcel/{itemCd}",method = RequestMethod.GET)
    public ResponseEntity<Resource> getSubExcel(@PathVariable String itemCd) throws Exception {

        List<GoodsBomItemByCosts> goodsBomItemByCostsList = this.goodsBomItemService.getGoodsBomItemByCostsListSub(itemCd);

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/consumed_sub_item_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("Sheet1");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            String excelTitle = "회사명: (주)진코스텍 ";
            ExcelPoiUtil.getCell(sheet, 0, 0).setCellValue(excelTitle);
            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 7);
            for (GoodsBomItemByCosts goodsBomItemByCosts : goodsBomItemByCostsList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(goodsBomItemByCosts.getItemCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(goodsBomItemByCosts.getItemName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(goodsBomItemByCosts.getBomVer());
                double prodQty = (goodsBomItemByCosts.getProdQty() != null) ? goodsBomItemByCosts.getProdQty().doubleValue() : 0;
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(prodQty);
                double targetQty = (goodsBomItemByCosts.getQty() != null) ? goodsBomItemByCosts.getQty().doubleValue() : 0;
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(targetQty);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(goodsBomItemByCosts.getProdItemCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(goodsBomItemByCosts.getProdItemName());
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("consumed_item_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    /** 품목별사용량(원재료) 조회 **/
    @RequestMapping(value = "/getUsageByItemList", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<List<UsageByItems>> getUsageByItemList(@RequestBody UsageByItemVo usageByItemVo){

        List<UsageByItems> resultList = this.goodsBomItemService.getUsageByItemList(usageByItemVo);

        return new RestUtil<List<UsageByItems>>().setData(resultList);
    }

    /** 품목별사용량(원재료) 엑셀다운로드 **/
    @RequestMapping(value = "/getUsageExcel", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Resource> getUsageExcel(@RequestBody UsageByItemVo usageByItemVo) throws Exception {

        List<UsageByItems> usageByItemList = this.goodsBomItemService.getUsageByItemList(usageByItemVo);

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/usage_item_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("품목별사용량리스트");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            String excelTitle = "회사명: (주)진코스텍 / " + usageByItemVo.getStartDate().toString() + " ~ " + usageByItemVo.getEndDate().toString();

            ExcelPoiUtil.getCell(sheet, 0, 0).setCellValue(excelTitle);
            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 11);
            for (UsageByItems usageByItems : usageByItemList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(usageByItems.getProdDate().toString());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(usageByItems.getProdNo());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(usageByItems.getProdItemCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(usageByItems.getProdItemName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(usageByItems.getUnit());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(usageByItems.getOrderQty().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(usageByItems.getItemCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(usageByItems.getItemName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue(usageByItems.getSpec());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue(usageByItems.getQty().doubleValue());
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("usage_item_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    /** 소요량계산(원재료) 조회 **/
    @RequestMapping(value = "/getConsumptionList", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<List<ConsumptionItem>> getConsumptionList(@RequestBody List<ConsumptionProdItem> consumptionProdItemList){
        return new RestUtil<List<ConsumptionItem>>().setData(this.goodsBomItemService.getConsumptionList(consumptionProdItemList));
    }
    
    /** 소요량계산(부자재) 조회 **/
    @RequestMapping(value = "/getConsumptionSubList", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<List<ConsumptionItem>> getConsumptionSubList(@RequestBody List<ConsumptionProdItem> consumptionProdItemList){
        return new RestUtil<List<ConsumptionItem>>().setData(this.goodsBomItemService.getConsumptionSubList(consumptionProdItemList));
    }

    /** 소요량계산(원재료) 엑셀다운로드 **/
    @RequestMapping(value = "/getConsumptionExcel", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Resource> getConsumptionExcel(@RequestBody List<ConsumptionProdItem> consumptionProdItemList) throws Exception {
        return this.goodsBomItemService.getConsumptionExcel(this.goodsBomItemService.getConsumptionList(consumptionProdItemList));
    }
    
    /** 소요량계산(부자재) 엑셀다운로드 **/
    @RequestMapping(value = "/getConsumptionSubExcel", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Resource> getConsumptionSubExcel(@RequestBody List<ConsumptionProdItem> consumptionProdItemList) throws Exception {
        return this.goodsBomItemService.getConsumptionExcel(this.goodsBomItemService.getConsumptionSubList(consumptionProdItemList));
    }
}

