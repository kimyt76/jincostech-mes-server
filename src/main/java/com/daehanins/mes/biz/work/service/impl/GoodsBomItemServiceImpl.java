package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.mat.service.IMatPointStockItemService;
import com.daehanins.mes.biz.mat.service.IMatPointStockService;
import com.daehanins.mes.biz.mat.vo.MatPointStockLast;
import com.daehanins.mes.biz.mat.vo.MatPointStockRead;
import com.daehanins.mes.biz.work.entity.GoodsBomItem;
import com.daehanins.mes.biz.work.mapper.GoodsBomItemMapper;
import com.daehanins.mes.biz.work.service.IGoodsBomItemService;
import com.daehanins.mes.biz.work.vo.*;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 제품BOM품목GoodsBomItem 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-23
 */
@Service
public class GoodsBomItemServiceImpl extends ServiceImpl<GoodsBomItemMapper, GoodsBomItem> implements IGoodsBomItemService {

    @Autowired
    IMatPointStockService matPointStockService;

    public List<GoodsBomItemByCosts> getGoodsBomItemByCostsList (String itemCd) {
        return this.baseMapper.getGoodsBomItemByCostsList(itemCd);
    };

    public List<GoodsBomItemByCosts> getGoodsBomItemByCostsListSub (String itemCd) {
        return this.baseMapper.getGoodsBomItemByCostsListSub(itemCd);
    };

    public List<UsageByItems> getUsageByItemList (UsageByItemVo usageByItemVo) {
        return this.baseMapper.getUsageByItemList(usageByItemVo);
    };

    public List<ConsumptionItem> getConsumptionList (List<ConsumptionProdItem> consumptionProdItemList) {
        return this.baseMapper.getConsumptionList(consumptionProdItemList);
    }

    public List<ConsumptionItem> getConsumptionSubList (List<ConsumptionProdItem> consumptionProdItemList) {
        return this.baseMapper.getConsumptionSubList(consumptionProdItemList);
    }

    public ResponseEntity<Resource> getConsumptionExcel (List<ConsumptionItem> consumptionItemList) throws Exception {
        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/consumption_item_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("소요량계산리스트");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 11);
            for (ConsumptionItem consumptionItem : consumptionItemList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(consumptionItem.getItemCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(consumptionItem.getItemType());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(consumptionItem.getItemName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(consumptionItem.getSpec());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(consumptionItem.getStockQty().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(consumptionItem.getReqQty().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(consumptionItem.getDiff().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(consumptionItem.getCustomerName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue(consumptionItem.getInOut());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue(consumptionItem.getInPrice().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 10, cellStyleList.get(10)).setCellValue("");
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("consumption_item_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

}
