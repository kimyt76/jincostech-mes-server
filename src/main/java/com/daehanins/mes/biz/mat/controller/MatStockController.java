package com.daehanins.mes.biz.mat.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.mat.entity.*;
import com.daehanins.mes.biz.mat.entity.MatStock;
import com.daehanins.mes.biz.mat.service.IMatStockItemViewService;
import com.daehanins.mes.biz.mat.service.IMatStockService;
import com.daehanins.mes.biz.mat.service.IMatStockViewService;
import com.daehanins.mes.biz.mat.vo.*;
import com.daehanins.mes.biz.pub.entity.CustomerView;
import com.daehanins.mes.biz.qt.entity.ItemTestNoView;
import com.daehanins.mes.biz.qt.service.IItemTestNoViewService;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.utils.SearchUtil;
import com.daehanins.mes.common.vo.PageVo;
import com.daehanins.mes.common.vo.RestResponse;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * MatStock Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-13
 */
@RestController
@RequestMapping("/mat/mat-stock")
public class MatStockController extends BaseController<MatStock, MatStockView, String> {

    @Autowired
    private IMatStockService matStockService;

    @Autowired
    private IMatStockViewService matStockViewService;

    @Autowired
    private IMatStockItemViewService matStockItemViewService;

    @Autowired
    private IItemTestNoViewService iItemTestNoViewService;

    @Override
    public IMatStockService getTableService() {
        return this.matStockService;
    }

    @Override
    public IMatStockViewService getViewService() {
    return this.matStockViewService;
    }


    @RequestMapping(value = "/getWithItems/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatStockReadWithItems> getWithItems(@PathVariable String id){
        MatStockReadWithItems mtItems = new MatStockReadWithItems();
        MatStockView matStock = getViewService().getById(id);

        QueryWrapper<MatStockItemView> queryWrapper = new QueryWrapper<MatStockItemView>()
                .eq(StringUtils.camelToUnderline("matStockId"), id)
                .orderByAsc("item_cd");
        List<MatStockItemView> matStockItems = this.matStockItemViewService.list(queryWrapper);
        mtItems.setMatStock(matStock);
        mtItems.setMatStockItems(matStockItems);
        return new RestUtil<MatStockReadWithItems>().setData(mtItems);
    }

    /*
     * 재고조사 생성 및 장부재고 생성
     */
    @RequestMapping(value = "/initMatStock", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<MatStock> saveLedgerStocks(@RequestBody MatStock matStock) throws Exception{
        if (this.getTableService().checkStockExist(matStock)) {
            throw new Exception("해당 날짜의 재고조사는 이미 등록되어있습니다.");
        }
        this.getTableService().initMatStockWithItems(matStock);
        return new RestUtil<MatStock>().setData(matStock);
    }


    /*
     * 장부재고 생성
     */
    @RequestMapping(value = "/saveLedgerStocks/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatStockReadWithItems> saveLedgerStocks(@PathVariable String id){

        MatStockReadWithItems mtItems = matStockService.saveLedgerStock(id);

        return new RestUtil<MatStockReadWithItems>().setData(mtItems);
    }

    /*
     * 실사재고 반영
     */
    @RequestMapping(value = "/saveStockRealQty/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatStockReadWithItems> saveStockRealQty(@PathVariable String id){

        MatStockReadWithItems mtItems = matStockService.saveStockRealQty(id);

        return new RestUtil<MatStockReadWithItems>().setData(mtItems);
    }

    /*
     * 조정자료 생성
     */
    @RequestMapping(value = "/saveStockAdjustQty/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatStockReadWithItems> saveStockAdjustQty(@PathVariable String id){

        MatStockReadWithItems mtItems = matStockService.saveStockAdjustQty(id);

        return new RestUtil<MatStockReadWithItems>().setData(mtItems);
    }

    @RequestMapping(value = "/deleteWithItems/{ids}",method = {RequestMethod.DELETE, RequestMethod.POST})
    @ResponseBody
    public RestResponse<Object> deleteWithItems(@PathVariable String[] ids){
        List<String> idList = Arrays.asList(ids);
        String msg = getTableService().deleteWithItems(idList);
        return new RestUtil<>().setMessage(msg);
    }

    /*
     * 엑셀다운로드
     */
    @RequestMapping(value = "/getExcel",method = RequestMethod.GET)
    public ResponseEntity<Resource> getExcel(@RequestParam String id) throws Exception {

        QueryWrapper<MatStockItemView> queryWrapper = new QueryWrapper<MatStockItemView>()
                .eq(StringUtils.camelToUnderline("matStockId"), id)
                .orderByAsc("item_cd");
        List<MatStockItemView> matStockItemList = this.matStockItemViewService.list(queryWrapper);

        try {
            // 엑셀양식 로드
            InputStream excelStream = getClass().getResourceAsStream("/excel/mat_stock_item_list.xls");
            Workbook workbook = ExcelPoiUtil.createHssfWorkbook(excelStream);
//            File file = ResourceUtils.getFile("classpath:excel/customer_erp_list.xls");
//            Workbook workbook = ExcelPoiUtil.createHssfWorkbook(new FileInputStream(file));
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheetAt(0);

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 15);
            for (MatStockItemView matStockItem : matStockItemList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(matStockItem.getItemCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(matStockItem.getItemName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(matStockItem.getLotNo());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(matStockItem.getTestNo());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(matStockItem.getAccountQty() == null ? 0 : matStockItem.getAccountQty().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(matStockItem.getRealQty() == null ? 0 : matStockItem.getRealQty().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(matStockItem.getMemo());
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
//            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
//            String saveTime = sdf.format (System.currentTimeMillis());
//            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("customer_erp_list", excelContent.length, "xls");

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    @RequestMapping(value = "/getCurrentStockList",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<HashMap<String, Object>>> getCurrentStockList(@RequestParam (name="condition", required=false ) String conditionJson){
        Map<String, Object> param = SearchUtil.parseParam(conditionJson);
        // 날짜값 변환
        LocalDate calcDate = LocalDate.parse(param.get("calcDate").toString(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        param.put("calcDate", calcDate);

        List<MatPointStockRead> data = getTableService().getCurrentStockItemList2(param);

        String currentItemCd = "";
        String currentTestNo = "";
        BigDecimal totalQty = BigDecimal.ZERO;

        List<HashMap<String, Object>> resultList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> result = new HashMap<>();

        int rowIndex = 0;
        try {
            for (MatPointStockRead currentStock : data) {
                // 품목,시험번호가 변경되는 시점
                if (!currentItemCd.equals(currentStock.getItemCd()) ||
                        (currentStock.getTestNo() != null && !currentTestNo.equals(currentStock.getTestNo()))) {
                    // 최초만 제외
                    if (rowIndex > 0) {
                        result.put("totalQty", totalQty);
                        resultList.add(result);
                    }
                    currentItemCd = currentStock.getItemCd();
                    currentTestNo = currentStock.getTestNo();
                    totalQty = BigDecimal.ZERO;
                    result = new HashMap<>();
                    result.put("itemCd", currentStock.getItemCd());
                    result.put("itemName", currentStock.getItemName());
                    result.put("testNo", currentStock.getTestNo());
                    result.put("lotNo", currentStock.getLotNo());
                    result.put("safeStockQty", currentStock.getSafeStockQty());
                }
                result.put(currentStock.getStorageCd(), currentStock.getStockQty());
                totalQty = totalQty.add(currentStock.getStockQty());
                rowIndex++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        if (rowIndex > 0) {
            result.put("totalQty", totalQty);
            resultList.add(result);
        }

        return new RestUtil<List<HashMap<String, Object>>>().setData(resultList);
    }


    @RequestMapping(value = "/getCurrentStockTestNo",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatStockResultItems> getCurrentStockTestNo(StockStorageVo stockStorageVo){

        MatStockResultItems matStockResultItems = new MatStockResultItems();

        List<StockStorageVo> stockStorageVoList = matStockService.getTargetStorageList(stockStorageVo);
        matStockResultItems.setStorageList(stockStorageVoList);
        matStockResultItems.setStorageMap(stockStorageVo);

        List<Map<String, Object>> resultMap = matStockService.getStockItemListByTestNo(matStockResultItems);
        matStockResultItems.setResultMap(resultMap);

        return new RestUtil<MatStockResultItems>().setData(matStockResultItems);
    }

    @RequestMapping(value = "/getCurrentStockItemCd",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatStockResultItems> getCurrentStockItemCd(StockStorageVo stockStorageVo){

        MatStockResultItems matStockResultItems = new MatStockResultItems();

        List<StockStorageVo> stockStorageVoList = matStockService.getTargetStorageList(stockStorageVo);
        matStockResultItems.setStorageList(stockStorageVoList);
        matStockResultItems.setStorageMap(stockStorageVo);

        List<Map<String, Object>> resultMap = matStockService.getStockItemListByItemCd(matStockResultItems);
        matStockResultItems.setResultMap(resultMap);

        return new RestUtil<MatStockResultItems>().setData(matStockResultItems);
    }

    @RequestMapping(value = "/getWeighClosing",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<WeighClosingItems>> getWeighClosing(WeighClosingVo weighClosingVo){
        List<WeighClosingItems> weighClosingItems =
                (weighClosingVo.getType().equals("DAY"))?
                        matStockService.getWeighClosingDay(weighClosingVo) : matStockService.getWeighClosingMonth(weighClosingVo);
        return new RestUtil<List<WeighClosingItems>>().setData(weighClosingItems);
    }

    @RequestMapping(value = "/getWeighClosingDetail",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<WeighClosingDetailItem>> getWeighClosingDetail(WeighClosingDetailVo weighClosingDetailVo){
        List<WeighClosingDetailItem> weighClosingItems = matStockService.getWeighClosingDetail(weighClosingDetailVo);
        return new RestUtil<List<WeighClosingDetailItem>>().setData(weighClosingItems);
    }

    @RequestMapping(value = "/getSingleItemStockList",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<MatSingleItemStock>> getSingleItemStockList(MatStockMoveVo matStockMoveVo){
        List<MatSingleItemStock> data = getTableService().getCurrentItemStockList(matStockMoveVo);
        return new RestUtil<List<MatSingleItemStock>>().setData(data);
    }

    //수불부
    @RequestMapping(value = "/getSupplyRegister",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<MatSupplyRegisterVo>> getSupplyRegister(MatSupplyRegisterReqVo matSupplyRegisterReqVo){
        List<MatSupplyRegisterVo> data = getTableService().getSupplyRegister(matSupplyRegisterReqVo);
        return new RestUtil<List<MatSupplyRegisterVo>>().setData(data);
    }

    @RequestMapping(value = "/getStockItemListByExpiryDate",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatStockResultItems> getStockItemListByExpiryDate(SearchDate searchDate){

        StockStorageVo stockStorageVo = new StockStorageVo();
        stockStorageVo.setAreaCd(searchDate.getAreaCd());
        stockStorageVo.setStdDate(searchDate.getStdDate());
        stockStorageVo.setItemTypeCd(searchDate.getItemTypeCd());
        stockStorageVo.setItemCd(searchDate.getItemCd());
        stockStorageVo.setItemName(searchDate.getItemName());

        MatStockResultItems matStockResultItems = new MatStockResultItems();

        List<StockStorageVo> stockStorageVoList = matStockService.getTargetStorageList(stockStorageVo);
        List<Map<String, Object>> resultMap = matStockService.getStockItemListByExpiryDate(stockStorageVoList, searchDate);

        matStockResultItems.setStorageList(stockStorageVoList);
        matStockResultItems.setResultMap(resultMap);

        return new RestUtil<MatStockResultItems>().setData(matStockResultItems);
    }

    @RequestMapping(value = "/getStockItemListByCreateDate",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<MatStockResultItems> getStockItemListByCreateDate(SearchDate searchDate){

        StockStorageVo stockStorageVo = new StockStorageVo();
        stockStorageVo.setAreaCd(searchDate.getAreaCd());
        stockStorageVo.setStdDate(searchDate.getStdDate());
        stockStorageVo.setItemTypeCd(searchDate.getItemTypeCd());
        stockStorageVo.setItemCd(searchDate.getItemCd());
        stockStorageVo.setItemName(searchDate.getItemName());

        MatStockResultItems matStockResultItems = new MatStockResultItems();

        List<StockStorageVo> stockStorageVoList = matStockService.getTargetStorageList(stockStorageVo);
        List<Map<String, Object>> resultMap = matStockService.getStockItemListByCreateDate(stockStorageVoList, searchDate);

        matStockResultItems.setStorageList(stockStorageVoList);
        matStockResultItems.setResultMap(resultMap);

        return new RestUtil<MatStockResultItems>().setData(matStockResultItems);
    }
}
