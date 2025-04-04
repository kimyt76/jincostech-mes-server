package com.daehanins.mes.biz.pub.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.pub.entity.Equipment;
import com.daehanins.mes.biz.pub.entity.EquipmentView;
import com.daehanins.mes.biz.pub.entity.MemberView;
import com.daehanins.mes.biz.pub.entity.StorageView;
import com.daehanins.mes.biz.pub.service.IEquipmentService;
import com.daehanins.mes.biz.pub.service.IEquipmentViewService;
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
import java.util.List;

/**
 * <p>
 * 설비Equipment Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-08
 */
@RestController
@RequestMapping("/pub/equipment")
public class EquipmentController extends BaseController<Equipment, EquipmentView, String> {

    @Autowired
    private IEquipmentService equipmentService;

    @Autowired
    private IEquipmentViewService equipmentViewService;

    @Override
    public IEquipmentService getTableService() {
        return this.equipmentService;
    }

    @Override
    public IEquipmentViewService getViewService() { return this.equipmentViewService; }

    @RequestMapping(value = "/getProdEquipmentList",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<EquipmentView>> getProdEquipmentList(){
        QueryWrapper<EquipmentView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("prod_yn", "Y")
                    .eq("use_yn", "Y")
                    .orderByAsc("storage_cd").orderByAsc("display_order");
        List<EquipmentView> result = this.getViewService().list(queryWrapper);
        return new RestUtil<List<EquipmentView>>().setData(result);
    }

    @RequestMapping(value = "/getExcel",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> getExcel(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<EquipmentView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<EquipmentView> data = getViewService().list(queryWrapper);
        try {
            // 엑셀양식 로드
            InputStream excelStream = getClass().getResourceAsStream("/excel/equipment_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("Sheet1");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 7);
            for (EquipmentView item : data){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(lineNo++);   // 줄번호
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(item.getEquipmentCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(item.getEquipmentName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(item.getAreaName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(item.getStorageName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(item.getUseYnName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue("");
                rowNo++;
            }

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("equipment_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }
    
}

