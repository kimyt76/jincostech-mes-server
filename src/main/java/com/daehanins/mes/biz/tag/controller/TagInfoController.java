package com.daehanins.mes.biz.tag.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.pub.entity.ScaleInfoView;
import com.daehanins.mes.biz.tag.entity.TagInfo;
import com.daehanins.mes.biz.tag.service.ITagInfoService;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
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
import java.util.List;

/**
 * <p>
 * TagInfo Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2021-04-08
 */
@RestController
@RequestMapping("/tag/tag-info")
public class TagInfoController extends BaseController<TagInfo, TagInfo, String> {

    @Autowired
    private ITagInfoService tagInfoService;

//    @Autowired
//    private ITagInfoViewService tagInfoViewService;

    @Override
    public ITagInfoService getTableService() {
        return this.tagInfoService;
    }

    @Override
    public ITagInfoService getViewService() {
    return this.tagInfoService;
    }

    @RequestMapping(value = "/getExcel",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> getExcel(@RequestParam(name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<TagInfo> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<TagInfo> data = getViewService().list(queryWrapper);
        try {
            // 엑셀양식 로드
            InputStream excelStream = getClass().getResourceAsStream("/excel/tag_info_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("Sheet1");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 11);
            for (TagInfo item : data){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(lineNo++);   // 줄번호
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(item.getTagCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(item.getTagName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(item.getDataType());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(item.getGatherType());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(item.getSource());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(item.getAddress());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(item.getItemId());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue(item.getReadSize());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue(item.getConversionFactor());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 10, cellStyleList.get(10)).setCellValue(item.getManageYn());
                rowNo++;
            }

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("tag_info_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }


}

