package com.daehanins.mes.biz.qt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.mat.entity.MatOrder;
import com.daehanins.mes.biz.mat.entity.MatOrderItem;
import com.daehanins.mes.biz.mat.entity.MatOrderItemView;
import com.daehanins.mes.biz.mat.entity.MatOrderView;
import com.daehanins.mes.biz.mat.vo.MatOrderReadWithItems;
import com.daehanins.mes.biz.mat.vo.MatOrderSaveWithItems;
import com.daehanins.mes.biz.pub.entity.Storage;
import com.daehanins.mes.biz.qt.entity.*;
import com.daehanins.mes.biz.qt.service.ITestTypeItemViewService;
import com.daehanins.mes.biz.qt.service.ITestTypeMethodService;
import com.daehanins.mes.biz.qt.service.ITestTypeService;
import com.daehanins.mes.biz.qt.service.ITestTypeViewService;
import com.daehanins.mes.biz.qt.vo.TestTypeReadWithItems;
import com.daehanins.mes.biz.qt.vo.TestTypeSaveWithItems;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.utils.SearchUtil;
import com.daehanins.mes.common.vo.PageVo;
import com.daehanins.mes.common.vo.RestResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 품질검사유형TestType Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-03
 */
@RestController
@RequestMapping("/qt/test-type")
public class TestTypeController extends BaseController<TestType, TestTypeView, String> {

    @Autowired
    private ITestTypeService testTypeService;

    @Autowired
    private ITestTypeViewService testTypeViewService;

    @Autowired
    private ITestTypeItemViewService testTypeItemViewService;

    @Autowired
    private ITestTypeMethodService testTypeMethodService;

    @Override
    public ITestTypeService getTableService() {
        return this.testTypeService;
    }

    @Override
    public ITestTypeViewService getViewService() {
    return this.testTypeViewService;
    }

    @RequestMapping(value = "/getTestTypePage",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<Page<TestTypeItemView>> getTestTypePage(@ModelAttribute PageVo param, @RequestParam (name="condition", required=false ) String conditionJson){
        Page<TestTypeItemView> page = new Page<>(param.getCurrentPage(), param.getPageSize());

        QueryWrapper<TestTypeItemView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        if (!StringUtils.isBlank(param.getSortColumn())) {
            queryWrapper.orderBy(true, param.isOrderAsc(), StringUtils.camelToUnderline(param.getSortColumn()));
        }
        Page<TestTypeItemView> data = this.testTypeItemViewService.page(page, queryWrapper );
        return new RestUtil<Page<TestTypeItemView>>().setData(data);
    }

//    @RequestMapping(value = "/getWithItems/{id}", method = RequestMethod.GET)
//    @ResponseBody
//    public RestResponse<TestTypeReadWithItems> getWithItems(@PathVariable String id){
//        TestTypeReadWithItems testTypeItems = new TestTypeReadWithItems();
//        TestTypeView testType = getViewService().getById(id);
//
//        QueryWrapper<TestTypeMethod> queryWrapper = new QueryWrapper<TestTypeMethod>()
//                .eq(StringUtils.camelToUnderline("testTypeId"), id)
//                .orderByAsc("display_order");
//        List<TestTypeMethod> testTypeMethods = this.testTypeMethodService.list(queryWrapper);
//        testTypeItems.setTestType(testType);
//        testTypeItems.setTestTypeMethods(testTypeMethods);
//        return new RestUtil<TestTypeReadWithItems>().setData(testTypeItems);
//    }

    @RequestMapping(value = "/getWithItems/{id}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<TestTypeReadWithItems> getWithItems(@PathVariable String id){
        TestTypeReadWithItems testTypeItems = new TestTypeReadWithItems();
//        TestTypeView testType = getViewService().getById(id);

        QueryWrapper<TestTypeMethod> queryWrapper = new QueryWrapper<TestTypeMethod>()
                .eq(StringUtils.camelToUnderline("itemCd"), id)
                .orderByAsc("display_order");
        List<TestTypeMethod> testTypeMethods = this.testTypeMethodService.list(queryWrapper);
//        testTypeItems.setTestType(testType);
        testTypeItems.setTestTypeMethods(testTypeMethods);
        return new RestUtil<TestTypeReadWithItems>().setData(testTypeItems);
    }


    @RequestMapping(value = "/saveWithItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<TestTypeSaveWithItems> saveWithItems(@RequestBody TestTypeSaveWithItems requestParam){

        TestType testType = requestParam.getTestType();

        List<TestTypeMethod> testTypeMethods = requestParam.getTestTypeMethods();
        List<TestTypeMethod> deleteTestTypeMethods = requestParam.getDeleteTestTypeMethods();

        TestTypeSaveWithItems data = getTableService().saveWithItems(testType, testTypeMethods, deleteTestTypeMethods);

        return new RestUtil<TestTypeSaveWithItems>().setData(data);
    }

    @RequestMapping(value = "/deleteWithItems/{ids}",method = {RequestMethod.DELETE, RequestMethod.POST})
    @ResponseBody
    public RestResponse<Object> deleteWithItems(@PathVariable String[] ids){
        List<String> idList = Arrays.asList(ids);
        String msg = getTableService().deleteWithItems(idList);
        return new RestUtil<>().setMessage(msg);
    }


    @RequestMapping(value = "/getExcel",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> getExcel(@RequestParam(name="condition", required=false ) String conditionJson) throws Exception {

        Map<String, Object> paramMap = SearchUtil.parseParam(conditionJson);
        QueryWrapper<TestTypeItemView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        queryWrapper.orderByAsc("item_cd");

        List<TestTypeItemView> data = testTypeItemViewService.list(queryWrapper);

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/test_type_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("Sheet1");

            // 기본 변수 선언
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            String excelTitle = "회사명: (주)진코스텍";
            ExcelPoiUtil.getCell(sheet, 0, 0).setCellValue(excelTitle);
            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 6);
            for (TestTypeItemView item : data){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(lineNo);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(item.getItemCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(item.getItemTypeName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(item.getItemName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(item.getTestItemJoin());
                String regStatus = (item.getRegYn().equals("Y")) ? "등록" : "미등록";
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(regStatus);
                rowNo++;
                lineNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("test_type_list", excelContent.length, "xlsx");

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }
}

