package com.daehanins.mes.biz.work.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.daehanins.mes.base.BaseController;
import com.daehanins.mes.biz.common.code.ProcessCd;
import com.daehanins.mes.biz.common.code.UseState;
import com.daehanins.mes.biz.common.code.WorkItemState;
import com.daehanins.mes.biz.common.code.WorkOrderItemStatus;
import com.daehanins.mes.biz.common.vo.LabelPrint;
import com.daehanins.mes.biz.common.vo.WeighLabelPrint;
import com.daehanins.mes.biz.mat.entity.MatOrderStateView;
import com.daehanins.mes.biz.mat.vo.MatLabelItem;
import com.daehanins.mes.biz.mobile.vo.MatUseSaveItem;
import com.daehanins.mes.biz.pub.entity.Area;
import com.daehanins.mes.biz.pub.entity.ItemMaster;
import com.daehanins.mes.biz.pub.service.IAreaService;
import com.daehanins.mes.biz.qt.entity.ItemTestNoView;
import com.daehanins.mes.biz.work.entity.*;
import com.daehanins.mes.biz.work.service.*;
import com.daehanins.mes.biz.work.vo.WorkOrderItemWithMatUse;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import com.daehanins.mes.common.utils.JasperUtil;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 자재소요량MatUse Front 컨트롤러
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-09
 */
@RestController
@RequestMapping("/work/mat-use")
public class MatUseController extends BaseController<MatUse, MatUseView, String> {

    @Autowired
    private IMatUseService matUseService;

    @Autowired
    private IMatUseViewService matUseViewService;

    @Autowired
    private IWorkOrderItemService workOrderItemService;

    @Autowired
    private IWorkOrderItemViewService workOrderItemViewService;

    @Autowired
    private IProdIngService prodIngService;

    @Autowired
    private IAreaService areaService;


    @Override
    public IMatUseService getTableService() {
        return this.matUseService;
    }

    @Override
    public IMatUseViewService getViewService() {
    return this.matUseViewService;
    }

    @RequestMapping(value = "/saveWeighItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<List<MatUse>> saveWeighItems(@RequestBody List<MatUse> matUseList){

        List<MatUse> data = new ArrayList<MatUse>();
        // item 신규,수정 처리
        matUseList.forEach( item -> {
            if (item.getWeighYn().equals("Y") ) {
                item.setUseState("WEIGH"); // 칭량
                if (item.getWeighDatetime() == null) {
                    item.setWeighDatetime(LocalDateTime.now());
                }
            }
            this.matUseService.saveOrUpdate(item);
            data.add(item);
        });

        return new RestUtil<List<MatUse>>().setData(data);
    }

    @RequestMapping(value = "/updateBagWeight", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<MatUse> updateBagWeight(@RequestBody MatUse matUse){

        this.matUseService.saveOrUpdate(matUse);

        return new RestUtil<MatUse>().setData(matUse);

    }

    @RequestMapping(value = "/saveProdItems", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<List<MatUse>> saveProdItems(@RequestBody List<MatUse> matUseList){

        List<MatUse> data = this.matUseService.saveProdItems(matUseList);
        return new RestUtil<List<MatUse>>().setData(data);
    }

    @RequestMapping(value = "/getByWorkOrderItemId/{workOrderItemId}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponse<WorkOrderItemWithMatUse>  getByWorkOrderItemId(@PathVariable String workOrderItemId) {

        WorkOrderItemWithMatUse result = new WorkOrderItemWithMatUse();

        WorkOrderItemView workOrderItemView = workOrderItemViewService.getById(workOrderItemId);

        String targetWorkOrderItemId = workOrderItemId;
        if (workOrderItemView.getProcessCd().equals(ProcessCd.제조)) {
            QueryWrapper<WorkOrderItem> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("work_order_batch_id", workOrderItemView.getWorkOrderBatchId())
                        .eq("process_cd", ProcessCd.칭량);
            targetWorkOrderItemId = workOrderItemService.getOne(queryWrapper).getWorkOrderItemId();
        }

        List<MatUseView>  matUseViewList= getViewService().getByWorkOrderItemId(targetWorkOrderItemId);

        result.setWorkOrderItemView(workOrderItemView);
        result.setMatUseViewList(matUseViewList);

        return new RestUtil<WorkOrderItemWithMatUse>().setData(result);
    }

    @RequestMapping(value = "/getMatUseExcel/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource>  getMatUseExcel(@PathVariable String id) throws Exception {

        QueryWrapper<MatUseView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_order_item_id", id).orderByAsc("prod_state").orderByAsc("ser_no");
        List<MatUseView> matUseViewList= getViewService().list(queryWrapper);

        WorkOrderItemView workOrderItemView = workOrderItemViewService.getById(id);

        try {
            InputStream excelStream = getClass().getResourceAsStream("/excel/mat_use_list.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            // 양식의 시트 로드
            Sheet sheet = workbook.getSheet("칭량작업");

            // 기본 변수 선언
            Row row = null;
            Cell cell = null;
            int rowNo = 2;     // 출력시작 Row,  POI row는 0부터 시작
            int lineNo = 1;    // 줄번호 표시용

            String excelTitle = "작업지시번호: " + workOrderItemView.getOrderNo()
                              + " / 제조번호 : " + workOrderItemView.getProdNo()
                              + " / 품명 : " + workOrderItemView.getItemName();

            ExcelPoiUtil.getCell(sheet, 0, 0).setCellValue(excelTitle);
            // 양식파일의 첫줄에 셀스타일 적용한 경우,  셀 스타일을 구함
            List<CellStyle> cellStyleList = ExcelPoiUtil.getRowCellStyle(sheet, rowNo, 11);
            for (MatUseView matUseView : matUseViewList){
                // 셀스타일 적용시 getStyleCell , 미적용시 getCell 사용
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 0, cellStyleList.get(0)).setCellValue(matUseView.getItemCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 1, cellStyleList.get(1)).setCellValue(matUseView.getItemName());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 2, cellStyleList.get(2)).setCellValue(matUseView.getAppearance());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 3, cellStyleList.get(3)).setCellValue(matUseView.getProdState());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 4, cellStyleList.get(4)).setCellValue(matUseView.getReqQty().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 5, cellStyleList.get(5)).setCellValue(matUseView.getWeighQty().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 6, cellStyleList.get(6)).setCellValue(matUseView.getBagWeight().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 7, cellStyleList.get(7)).setCellValue(matUseView.getTotalQty().doubleValue());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 8, cellStyleList.get(8)).setCellValue(matUseView.getTestNoJoin());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 9, cellStyleList.get(9)).setCellValue(matUseView.getWeighMemberCd());
                ExcelPoiUtil.getStyleCell(sheet, rowNo, 10, cellStyleList.get(10)).setCellValue(matUseView.getWeighConfirmMemberCd());
                rowNo++;
            }
            // 끝라인에 다운로드 시간 표시
            SimpleDateFormat sdf = new SimpleDateFormat ( "yyyy/MM/dd a hh:mm:ss");
            String saveTime = sdf.format (System.currentTimeMillis());
            ExcelPoiUtil.getCell(sheet, rowNo, 0).setCellValue(saveTime);

            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("mat_use_list", excelContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }


    @RequestMapping(value = "/investmentMatUse", method = {RequestMethod.POST, RequestMethod.PUT})
    @ResponseBody
    public RestResponse<List<MatUseView>> investmentMatUse(@RequestBody MatUseSaveItem requestParam){

        List<MatUseView> list = matUseService.investmentMatUse(requestParam);

        return new RestUtil<List<MatUseView>>().setData(list);
    }


    @RequestMapping(value = "/printWeighLabel", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Resource> printWeighLabel(@RequestBody MatUseView[] labelItems) throws Exception {

        List<WeighLabelPrint> labelPrintList = new ArrayList<>();

        DecimalFormat df = new DecimalFormat("#,##0.000");
        DecimalFormat df2 = new DecimalFormat("#,##0.00000");
        String unit = " kg";
        WorkOrderItemView workOrderItem = this.workOrderItemViewService.getById(labelItems[0].getWorkOrderItemId());
        String prodItemCd = workOrderItem.getItemCd();
        String prodItemName = workOrderItem.getItemName();
        String prodQty = df.format(workOrderItem.getOrderQty()) + unit;
        String lotNo = workOrderItem.getLotNo();
        String prodNo = workOrderItem.getProdNo();
        Area area = this.areaService.getById(workOrderItem.getAreaCd());

        // WorkOrder 조회하여  areaCd 확인, 외주인 경우 itemName 표시하지 않음  QQQQ
        String itemName = "";
        for (MatUseView labelItem : labelItems ) {

            itemName = area.getInboundYn().equals("Y") ?  labelItem.getItemName() : "---";

            WeighLabelPrint labelPrint = new WeighLabelPrint(
                    labelItem.getMatUseId(),
                    prodItemCd,
                    prodItemName,
                    labelItem.getProdState() + "-" + labelItem.getSerNo(),
                    itemName,
                    prodQty,
                    df.format(labelItem.getBagWeight()) + unit,
                    df2.format(labelItem.getWeighQty()) + unit,
                    df.format(labelItem.getTotalQty()) + unit,
                    lotNo,
                    prodNo
            );
            labelPrintList.add(labelPrint);
        }

        try {
            InputStream reportStream = getClass().getResourceAsStream("/report/weigh_label_v2.jrxml");
            byte[] pdfContent = JasperUtil.getPdfBinary(reportStream, labelPrintList);
            ByteArrayResource resource = new ByteArrayResource(pdfContent);
            HttpHeaders header = JasperUtil.getHeader("mat_label_list", pdfContent.length);

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("리포트에서 에러발생");
        }

    }
}

