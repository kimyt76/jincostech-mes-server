package com.daehanins.mes.biz.mat.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.mat.entity.*;
import com.daehanins.mes.biz.mat.service.IMatProdOutViewService;
import com.daehanins.mes.biz.mat.service.IMatTranItemService;
import com.daehanins.mes.biz.mat.service.IMatTranItemViewService;
import com.daehanins.mes.biz.mat.service.IMatTranStateViewService;
import com.daehanins.mes.biz.qt.entity.QualityTestView;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import com.daehanins.mes.common.utils.JasperUtil;
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
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 자재거래품목MatTranItem Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-09
 */
@RestController
@RequestMapping("/mat/mat-tran-item")
public class MatTranItemController extends BaseController<MatTranItem, MatTranItemView, String> {

    @Autowired
    private IMatTranItemService matTranItemService;

    @Autowired
    private IMatTranItemViewService matTranItemViewService;

    @Autowired
    private IMatTranStateViewService matTranStateViewService;

    @Autowired
    private IMatProdOutViewService matProdOutViewService;


    @Override
    public IMatTranItemService getTableService() {
        return this.matTranItemService;
    }

    @Override
    public IMatTranItemViewService getViewService() {
        return this.matTranItemViewService;
    }

    @RequestMapping(value = "/getStatePage",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<Page<MatTranStateView>> getStatePage(@ModelAttribute PageVo param, @RequestParam (name="condition", required=false ) String conditionJson){
        Page<MatTranStateView> page = new Page<>(param.getCurrentPage(), param.getPageSize());

        QueryWrapper<MatTranStateView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        if (!StringUtils.isBlank(param.getSortColumn())) {
            queryWrapper.orderBy(true, param.isOrderAsc(), StringUtils.camelToUnderline(param.getSortColumn()));
        }
        Page<MatTranStateView> data = this.matTranStateViewService.page(page, queryWrapper );
        return new RestUtil<Page<MatTranStateView>>().setData(data);
    }

    @RequestMapping(value = "/getMatProdOutPage",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<Page<MatProdOutView>> getMatProdOutPage(@ModelAttribute PageVo param, @RequestParam (name="condition", required=false ) String conditionJson){
        Page<MatProdOutView> page = new Page<>(param.getCurrentPage(), param.getPageSize());

        QueryWrapper<MatProdOutView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        queryWrapper.orderByDesc("tran_date").orderByAsc("item_cd").orderByDesc("prod_no");
        Page<MatProdOutView> data = this.matProdOutViewService.page(page, queryWrapper );
        return new RestUtil<Page<MatProdOutView>>().setData(data);
    }


    @RequestMapping(value = "/getExcelProdOut",method = RequestMethod.GET)
    public ResponseEntity<Resource> getExcelProdOut(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception {

        Map<String, Object> paramMap = SearchUtil.parseParam(conditionJson);
        QueryWrapper<MatProdOutView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        queryWrapper.orderByDesc("tran_date").orderByAsc("item_cd").orderByDesc("prod_no");
        List<MatProdOutView> dataList = this.matProdOutViewService.list(queryWrapper);

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/mat_prod_out_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("Sheet1");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            String excelTitle = "회사명: (주)진코스텍 / " + paramMap.get("startDate") + " ~ " + paramMap.get("endDate");
            ExcelPoiUtil.getCell(sheet, 0, 0).setCellValue(excelTitle);
            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 13);
            for (MatProdOutView item : dataList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                String tranDate = item.getTranDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(lineNo);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(tranDate);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(item.getAreaName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(item.getSrcStorageName());
                String prodNo = item.getProdNo().replaceAll("!!", " ");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(prodNo);
                String lotNo = item.getLotNo().replaceAll("!!", " ");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(lotNo);
                String lotNo2 = item.getLotNo2().replaceAll("!!", " ");
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(lotNo2);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(item.getItemCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue(item.getItemName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue(item.getItemNameStr());
                int cnt = (item.getCnt() != null)? item.getCnt() : 0;
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 10, cellStyleList.get(10)).setCellValue(cnt);
                double sumQty = (item.getSumQty() != null)? item.getSumQty().doubleValue() : 0;
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 11, cellStyleList.get(11)).setCellValue(sumQty);
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 12, cellStyleList.get(12)).setCellValue("");
                rowNo++;
                lineNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("mat_prod_out_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }

    @RequestMapping(value = "/getPdf", method = RequestMethod.GET)
    public ResponseEntity<Resource> getPdf(@RequestParam(name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<MatTranStateView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<MatTranStateView> QualityTestViewList = matTranStateViewService.list(queryWrapper);

        try {
            InputStream reportStream = getClass().getResourceAsStream("/report/mat_in_state_list.jrxml");
            byte[] pdfContent = JasperUtil.getPdfBinary(reportStream, QualityTestViewList);
            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("mat_in_state_list", pdfContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("리포트 생성중 에러발생!");
        }
    }

    @RequestMapping(value = "/getExcel",method = RequestMethod.GET)
    public ResponseEntity<Resource> getExcel(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception {

        QueryWrapper<MatTranStateView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        Map<String, Object> paramMap = SearchUtil.parseParam(conditionJson);
        queryWrapper.orderByDesc("tran_no");
        List<MatTranStateView> matOrderStateList = this.matTranStateViewService.list(queryWrapper);
        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/mat_in_state_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("구매현황조회");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            String excelTitle = "회사명: (주)진코스텍 / " + paramMap.get("startDate") + " ~ " + paramMap.get("endDate");
            ExcelPoiUtil.getCell(sheet, 0, 0).setCellValue(excelTitle);
            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 16);
            for (MatTranStateView matTranState : matOrderStateList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(matTranState.getTranNo());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(matTranState.getTranDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(matTranState.getTranDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(matTranState.getCustomerName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(matTranState.getItemCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(matTranState.getItemName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(matTranState.getTestNo());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(matTranState.getSpec());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(matTranState.getQty().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue(matTranState.getPrice().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue(matTranState.getSupplyAmt().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 10, cellStyleList.get(10)).setCellValue(matTranState.getVat().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 11, cellStyleList.get(11)).setCellValue(matTranState.getTotalAmt().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 12, cellStyleList.get(12)).setCellValue(matTranState.getTestStateName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 13, cellStyleList.get(13)).setCellValue(matTranState.getPassStateName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 14, cellStyleList.get(14)).setCellValue(matTranState.getAreaName());
                if ( matTranState.getExpiryDate() == null ||  "".equals(matTranState.getExpiryDate())){
                    ExcelPoiUtil.getStyleCell(sheet, rowNo, 15, cellStyleList.get(15)).setCellValue("");
                }else{
                    ExcelPoiUtil.getStyleCell(sheet, rowNo, 15, cellStyleList.get(15)).setCellValue(matTranState.getExpiryDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
                }
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("mat_in_state_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }


}

