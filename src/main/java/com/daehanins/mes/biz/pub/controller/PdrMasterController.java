package com.daehanins.mes.biz.pub.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.common.code.WorkOrderItemStatus;
import com.daehanins.mes.biz.mat.entity.MatStock;
import com.daehanins.mes.biz.pub.entity.*;
import com.daehanins.mes.biz.pub.service.*;
import com.daehanins.mes.biz.pub.service.impl.PdrExpenseServiceImpl;
import com.daehanins.mes.biz.pub.vo.PdrMasterWithItems;
import com.daehanins.mes.biz.pub.vo.PdrSellWithItems;
import com.daehanins.mes.biz.qt.entity.QualityTest;
import com.daehanins.mes.biz.qt.entity.QualityTestMethod;
import com.daehanins.mes.biz.qt.vo.QualityTestSaveWithItems;
import com.daehanins.mes.biz.work.entity.WorkOrderBatchStatusView;
import com.daehanins.mes.biz.work.service.IMatUseEtcViewService;
import com.daehanins.mes.biz.work.vo.MatSubItem;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import com.daehanins.mes.common.utils.RestUtil;
import com.daehanins.mes.common.utils.SearchUtil;
import com.daehanins.mes.common.vo.RestResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * PdrMaster Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2022-12-21
 */
@RestController
@RequestMapping("/pub/pdr-master")
public class PdrMasterController extends BaseController<PdrMaster, PdrMasterView, String> {

    @Autowired
    private IPdrMasterService pdrMasterService;

    @Autowired
    private IPdrMasterViewService pdrMasterViewService;

    @Autowired
    private IPdrSellService pdrSellService;

    @Autowired
    private IPdrSellViewService pdrSellViewService;

    @Autowired
    private IPdrLaborService pdrLaborService;

    @Autowired
    private IPdrLaborViewService pdrLaborViewService;

    @Autowired
    private IPdrExpenseService pdrExpenseService;

    @Autowired
    private IPdrMatService pdrMatService;

    @Autowired
    private IPdrMatViewService pdrMatViewService;

    @Autowired
    private IPdrMatSubService pdrMatSubService;

    @Autowired
    private IPdrMatSubViewService pdrMatSubViewService;

    @Autowired
    private IPdrWorkerService pdrWorkerService;

    @Autowired
    private IPdrWorkerViewService pdrWorkerViewService;

    @Autowired
    private IMatUseEtcViewService matUseEtcViewService;

    @Override
    public IPdrMasterService getTableService() {
        return this.pdrMasterService;
    }

    @Override
    public IPdrMasterViewService getViewService() {
    return this.pdrMasterViewService;
    }

    @RequestMapping(value = "/initPdrMaster", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<PdrMaster> initPdrMaster(@RequestBody PdrMaster pdrMaster) throws Exception{
        PdrMaster result;
        if (this.getTableService().checkExist(pdrMaster)) {
            throw new Exception("해당 날짜의 생산일보는 이미 등록되어있습니다.");
        } else {
            result = this.getTableService().initPdrMaster(pdrMaster);
        }
        return new RestUtil<PdrMaster>().setData(result);
    }

    @RequestMapping(value = "/updatePdrMaster", method = RequestMethod.POST)
    @ResponseBody
    public RestResponse<PdrMasterView> updatePdrMaster(@RequestBody PdrMaster pdrMaster) throws Exception{
        this.getTableService().saveOrUpdate(pdrMaster);
        PdrMasterView result = this.getViewService().getById(pdrMaster.getPdrMasterId());
        return new RestUtil<PdrMasterView>().setData(result);
    }

    @RequestMapping(value = "/getPdrMasterWithItems/{id}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<PdrMasterWithItems> getPdrMasterWithItems(@PathVariable String id){

        PdrMasterWithItems result = new PdrMasterWithItems();

        result.setPdrMasterView(this.getViewService().getById(id));
        result.setPdrSellList(pdrSellViewService.listByMasterId(id));
        result.setPdrLaborList(pdrLaborViewService.listByMasterId(id));
        result.setPdrExpenseList(pdrExpenseService.listByMasterId(id));

        return new RestUtil<PdrMasterWithItems>().setData(result);
    }

    @RequestMapping(value = "/getPdrSellWithItems/{id}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<PdrSellWithItems> getPdrSellWithItems(@PathVariable String id){

        PdrSellWithItems result = new PdrSellWithItems();

        result.setPdrSellView(pdrSellViewService.getById(id));
        result.setPdrMatViewList(pdrMatViewService.listBySellId(id));
        result.setPdrMatSubViewList(pdrMatSubViewService.listBySellId(id));
        result.setPdrWorkerViewList(pdrWorkerViewService.listBySellId(id));

        return new RestUtil<PdrSellWithItems>().setData(result);
    }

    @RequestMapping(value = "/getMatSubList/{id}",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<List<MatSubItem>> getMatSubList(@PathVariable String id){
        List<MatSubItem> result = matUseEtcViewService.getMatSubList(id);
        return new RestUtil<List<MatSubItem>>().setData(result);
    }

    //pdr_sell 저장
    @RequestMapping(value = "/savePdrSell", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<PdrMasterWithItems> savePdrSell(@RequestBody List<PdrSell> pdrSellList){

        pdrSellService.saveOrUpdateBatch(pdrSellList);

        String id = pdrSellList.get(0).getPdrMasterId();

        PdrMasterWithItems result = new PdrMasterWithItems();
        result.setPdrMasterView(this.getViewService().getById(id));
        result.setPdrSellList(pdrSellViewService.listByMasterId(id));

        return new RestUtil<PdrMasterWithItems>().setData(result);
    }

    //pdr_labor 저장
    @RequestMapping(value = "/savePdrLabor", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<PdrMasterWithItems> savePdrLabor(@RequestBody List<PdrLabor> pdrLaborList){

        pdrLaborService.saveOrUpdateBatch(pdrLaborList);

        String id = pdrLaborList.get(0).getPdrMasterId();

        PdrMasterWithItems result = new PdrMasterWithItems();
        result.setPdrMasterView(this.getViewService().getById(id));
        result.setPdrLaborList(pdrLaborViewService.listByMasterId(id));

        return new RestUtil<PdrMasterWithItems>().setData(result);
    }

    //pdr_expense 저장
    @RequestMapping(value = "/savePdrExpense", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<PdrMasterWithItems> savePdrExpense(@RequestBody List<PdrExpense> pdrExpenseList){

        pdrExpenseService.saveOrUpdateBatch(pdrExpenseList);

        String id = pdrExpenseList.get(0).getPdrMasterId();
        PdrMasterWithItems result = new PdrMasterWithItems();
        result.setPdrMasterView(this.getViewService().getById(id));
        result.setPdrExpenseList(pdrExpenseService.listByMasterId(id));

        return new RestUtil<PdrMasterWithItems>().setData(result);
    }

    //pdr_mat 저장
    @RequestMapping(value = "/savePdrMat", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<PdrSellWithItems> savePdrMat(@RequestBody List<PdrMat> pdrMatList){

        for (PdrMat pdrMat : pdrMatList) {
            pdrMatService.removeById(pdrMat.getPdrMatId());
        }
        pdrMatService.saveBatch(pdrMatList);

        String id = pdrMatList.get(0).getPdrSellId();
        PdrSellWithItems result = new PdrSellWithItems();

        result.setPdrSellView(pdrSellViewService.getById(id));
        result.setPdrMatViewList(pdrMatViewService.listBySellId(id));

        return new RestUtil<PdrSellWithItems>().setData(result);
    }

    //pdr_mat_sub 저장
    @RequestMapping(value = "/savePdrMatSub", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<PdrSellWithItems> savePdrMatSub(@RequestBody List<PdrMatSub> pdrMatSubList){

        for (PdrMatSub pdrMatSub : pdrMatSubList) {
            pdrMatSubService.removeById(pdrMatSub.getPdrMatSubId());
        }
        pdrMatSubService.saveBatch(pdrMatSubList);

        String id = pdrMatSubList.get(0).getPdrSellId();
        PdrSellWithItems result = new PdrSellWithItems();

        result.setPdrSellView(pdrSellViewService.getById(id));
        result.setPdrMatSubViewList(pdrMatSubViewService.listBySellId(id));

        return new RestUtil<PdrSellWithItems>().setData(result);
    }

    //pdr_worker 저장
    @RequestMapping(value = "/savePdrWorker", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<PdrSellWithItems> savePdrWorker(@RequestBody List<PdrWorker> pdrWorkerList){

        for (PdrWorker pdrWorker : pdrWorkerList) {
            pdrWorkerService.removeById(pdrWorker.getPdrWorkerId());
        }
        pdrWorkerService.saveBatch(pdrWorkerList);

        String id = pdrWorkerList.get(0).getPdrSellId();
        PdrSellWithItems result = new PdrSellWithItems();

        result.setPdrSellView(pdrSellViewService.getById(id));
        result.setPdrWorkerViewList(pdrWorkerViewService.listBySellId(id));

        return new RestUtil<PdrSellWithItems>().setData(result);
    }

    /** 생산일보 삭제 **/
    @RequestMapping(value = "/deleteWithItems/{id}",method = {RequestMethod.DELETE, RequestMethod.POST})
    @ResponseBody
    public RestResponse<Object> deleteWithItems(@PathVariable String id){
        String msg = (this.getTableService().deleteWithItems(id))? "ok" : "false";
        return new RestUtil<>().setMessage(msg);
    }

    /** 판매금액 1건 삭제 **/
    @RequestMapping(value = "/deleteSellWithItems/{id}",method = {RequestMethod.DELETE, RequestMethod.POST})
    @ResponseBody
    public RestResponse<PdrMasterWithItems> deleteSellWithItems(@PathVariable String id){

        PdrSell pdrSell = pdrSellService.getById(id);
        String masterId = pdrSell.getPdrMasterId();
        String msg = (pdrSellService.removeBySellId(id))? "ok" : "false";

        PdrMasterWithItems result = new PdrMasterWithItems();
        result.setPdrMasterView(this.getViewService().getById(masterId));
        result.setPdrSellList(pdrSellViewService.listByMasterId(masterId));

        return new RestUtil<PdrMasterWithItems>().setData(result);
    }

    @RequestMapping(value = "/getPdrExcel/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> getPdrExcel(@PathVariable String id) throws Exception {
        return this.getTableService().getPdrExcel(id);
    }

    @RequestMapping(value = "/getPdrMasterListExcel",method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> getPdrMasterListExcel(@RequestParam (name="condition", required=false ) String conditionJson) throws Exception {

        Map<String, Object> paramMap = SearchUtil.parseParam(conditionJson);
        QueryWrapper<PdrMasterView> queryWrapper = SearchUtil.parseWhereSql(conditionJson);
        List<PdrMasterView> data = this.getViewService().list(queryWrapper);

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/pdr_master_view_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            Sheet sheet = workbook.getSheet("Sheet1");
            // 기본 변수 선언
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            String excelTitle = "회사명: (주)진코스텍 / " + paramMap.get("startDate") + " ~ " + paramMap.get("endDate");
            ExcelPoiUtil.getCell(sheet, 0, 0).setCellValue(excelTitle);

            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 14);
            for (PdrMasterView item : data){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(lineNo++);   // 줄번호
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(item.getProdDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(item.getAreaName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(item.getStdTime());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(item.getM1Cost());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(item.getM2Cost());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(item.getDirectCost());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(item.getLaborCost());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue(item.getEtcCost());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue(item.getTotalSellAmt());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 10, cellStyleList.get(10)).setCellValue(item.getProdCost());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 11, cellStyleList.get(11)).setCellValue(item.getOrdinaryIncome());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 12, cellStyleList.get(12)).setCellValue(item.getProfitRate());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 13, cellStyleList.get(13)).setCellValue("");
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("pdr_master_view_list", excelContent.length, "xlsx");

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }
}

