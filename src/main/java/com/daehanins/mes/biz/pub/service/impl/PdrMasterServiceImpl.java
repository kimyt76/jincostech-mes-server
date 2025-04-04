package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.mat.entity.MatStock;
import com.daehanins.mes.biz.pub.entity.*;
import com.daehanins.mes.biz.pub.mapper.PdrMasterMapper;
import com.daehanins.mes.biz.pub.service.*;
import com.daehanins.mes.common.utils.ExcelPoiUtil;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * PdrMaster 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2022-12-21
 */
@Service
public class PdrMasterServiceImpl extends ServiceImpl<PdrMasterMapper, PdrMaster> implements IPdrMasterService {

    @Autowired
    private IPdrMasterViewService pdrMasterViewService;

    @Autowired
    private IPdrSellService pdrSellService;

    @Autowired
    private IPdrSellViewService pdrSellViewService;

    @Autowired
    private IPdrMatViewService pdrMatViewService;

    @Autowired
    private IPdrMatSubViewService pdrMatSubViewService;

    @Autowired
    private IPdrWorkerViewService pdrWorkerViewService;

    @Autowired
    private IPdrLaborService pdrLaborService;

    @Autowired
    private IPdrLaborViewService pdrLaborViewService;

    @Autowired
    private IPdrExpenseService pdrExpenseService;

    private static final DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd (E)");

    public boolean checkExist (PdrMaster pdrMaster) {
        QueryWrapper<PdrMaster> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("prod_date", pdrMaster.getProdDate());
        queryWrapper.eq("area_cd", pdrMaster.getAreaCd());
        return this.count(queryWrapper) > 0;
    }

    public PdrMaster initPdrMaster (PdrMaster pdrMaster) {
        pdrMaster.setPdrMasterId(String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId()));
        this.saveOrUpdate(pdrMaster);
        return pdrMaster;
    }

    @Transactional
    public Boolean deleteWithItems (String id) {
        Boolean isDelSell = pdrSellService.removeByMasterId(id); // delete pdrSell with pdrWorker - pdrMat - pdrMatSub
        Boolean isDelLabor = pdrLaborService.remove(new QueryWrapper<PdrLabor>().eq("pdr_master_id", id));
        Boolean isDelExpense = pdrExpenseService.remove(new QueryWrapper<PdrExpense>().eq("pdr_master_id", id));
        Boolean isDelMaster = this.removeById(id);
        return (isDelMaster && isDelSell && isDelLabor && isDelExpense);
    }

    public ResponseEntity<Resource> getPdrExcel (String id) throws Exception {

        PdrMasterView pdrMasterView = pdrMasterViewService.getById(id);
        List<PdrSellView> pdrSellList = pdrSellViewService.listByMasterId(id);
        List<PdrLaborView> pdrLaborList = pdrLaborViewService.listByMasterId(id);
        List<PdrExpense> pdrExpenseList = pdrExpenseService.listByMasterId(id);

        try {

            InputStream excelStream = getClass().getResourceAsStream("/excel/pdr_report.xlsx");
            Workbook workbook = ExcelPoiUtil.createWorkbook(excelStream);
            Sheet sheet = workbook.getSheet("Sheet1");

            //작성일 B8
            ExcelPoiUtil.getCellRef(sheet, "B8").setCellValue(sdf.format(pdrMasterView.getProdDate()));

            //작업시간 E8 (작업시간 : 09:00 -  21:00 )
            ExcelPoiUtil.getCellRef(sheet, "E8").setCellValue("(작업시간 : " + pdrMasterView.getStdTime() + " )");

            /** 판매금액 파트 A12 ~ A42 (30행) **/
            // CODE, 업체명, 영업부담당자, 품명, 구분1, 구분2, 생산계획수량, 생산수량, 판매단가, 생산단가, 매출금액
            // A12, B12, C12, D12, H12, I12, J12, K12, L12, N12, P12
            int sellIdx = 12;
            int costIdx = 7;
            for (PdrSellView item : pdrSellList){
                //CODE
                ExcelPoiUtil.getCellRef(sheet, "A" + sellIdx).setCellValue(item.getItemCd());
                //업체명
                ExcelPoiUtil.getCellRef(sheet, "B" + sellIdx).setCellValue(item.getCustomerName());
                //영업부담당자
                ExcelPoiUtil.getCellRef(sheet, "C" + sellIdx).setCellValue(item.getSalesMemberCd());
                //품명
                ExcelPoiUtil.getCellRef(sheet, "D" + sellIdx).setCellValue(item.getItemName());
                //구분1
                ExcelPoiUtil.getCellRef(sheet, "H" + sellIdx).setCellValue(item.getItemGb1());
                //구분2
                ExcelPoiUtil.getCellRef(sheet, "I" + sellIdx).setCellValue(item.getItemGb2());
                //생산계획수량
                Double orderQty = (item.getOrderQty() != null)? item.getOrderQty().doubleValue() : 0;
                ExcelPoiUtil.getCellRef(sheet, "J" + sellIdx).setCellValue(orderQty);
                //생산수량
                Double prodQty = (item.getProdQty() != null)? item.getProdQty().doubleValue() : 0;
                ExcelPoiUtil.getCellRef(sheet, "K" + sellIdx).setCellValue(prodQty);
                //판매단가
                Double sellPrice = (item.getSellPrice() != null)? item.getSellPrice().doubleValue() : 0;
                ExcelPoiUtil.getCellRef(sheet, "L" + sellIdx).setCellValue(sellPrice);
                //생산단가
                Double prodPrice = (item.getProdPrice() != null)? item.getProdPrice().doubleValue() : 0;
                ExcelPoiUtil.getCellRef(sheet, "N" + sellIdx).setCellValue(prodPrice);
                //매출금액
                Double sellAmt = (item.getSellAmt() != null)? item.getSellAmt().doubleValue() : 0;
                ExcelPoiUtil.getCellRef(sheet, "P" + sellIdx).setCellValue(sellAmt);


                List<PdrMatView> matList = pdrMatViewService.listBySellId(item.getPdrSellId());
                List<PdrMatSubView> matSubList = pdrMatSubViewService.listBySellId(item.getPdrSellId());
                List<PdrWorkerView> workerList = pdrWorkerViewService.listBySellId(item.getPdrSellId());

                double m1Cost = (item.getM1Cost() != null)? item.getM1Cost().doubleValue() : 0;
                double m2Cost = (item.getM2Cost() != null)? item.getM2Cost().doubleValue() : 0;
                double diCost = (item.getDirectCost() != null)? item.getDirectCost().doubleValue() : 0;

                //소계
                ExcelPoiUtil.getCellRef(sheet, "Q" + costIdx + "0" ).setCellValue(m1Cost + m2Cost + diCost);

                for (int i = 0; i< 10; i++) {
                    //원재료비
                    if (matList.size()> 0) {
                        ExcelPoiUtil.getCellRef(sheet, "A" + costIdx + "" + i).setCellValue((matList.get(i).getItemCd() != null )? matList.get(i).getItemCd() : "");
                        ExcelPoiUtil.getCellRef(sheet, "B" + costIdx + "" + i).setCellValue((matList.get(i).getItemName() != null )? matList.get(i).getItemName() : "");
                        String m1Amt = (matList.get(i).getAmt() != null && matList.get(i).getAmt() > 0)? matList.get(i).getAmt().toString() : "";
                        ExcelPoiUtil.getCellRef(sheet, "D" + costIdx + "" + i).setCellValue(m1Amt);
                        String m1Price = (matList.get(i).getPrice() != null && matList.get(i).getPrice() > 0)? matList.get(i).getPrice().toString() : "";
                        ExcelPoiUtil.getCellRef(sheet, "E" + costIdx + "" + i).setCellValue(m1Price);
                        String m1_Cost = (matList.get(i).getM1Cost() != null && matList.get(i).getM1Cost() > 0)? matList.get(i).getM1Cost().toString() : "";
                        ExcelPoiUtil.getCellRef(sheet, "F" + costIdx + "" + i).setCellValue(m1_Cost);
                    }
                    if (matSubList.size() > 0) {
                        //부재료비
                        ExcelPoiUtil.getCellRef(sheet, "H" + costIdx + "" + i).setCellValue((matSubList.get(i).getItemName() != null )? matSubList.get(i).getItemName() : "");
                        String m2Amt = (matSubList.get(i).getAmt() != null && matSubList.get(i).getAmt() > 0)? matSubList.get(i).getAmt().toString() : "";
                        ExcelPoiUtil.getCellRef(sheet, "I" + costIdx + "" + i).setCellValue(m2Amt);
                        String m2Price = (matSubList.get(i).getPrice() != null && matSubList.get(i).getPrice() > 0)? matSubList.get(i).getPrice().toString() : "";
                        ExcelPoiUtil.getCellRef(sheet, "J" + costIdx + "" + i).setCellValue(m2Price);
                        String m2_cost = (matSubList.get(i).getM2Cost() != null && matSubList.get(i).getM2Cost() > 0)? matSubList.get(i).getM2Cost().toString() : "";
                        ExcelPoiUtil.getCellRef(sheet, "K" + costIdx + "" + i).setCellValue(m2_cost);
                    }
                    if (workerList.size() > 0) {
                        //직접인건비&식대
                        String cnt = (workerList.get(i).getCnt() != null && workerList.get(i).getCnt() > 0)? workerList.get(i).getCnt().toString() : "";
                        ExcelPoiUtil.getCellRef(sheet, "K" + costIdx + "" + i).setCellValue(cnt);
                        double wageAmt = (workerList.get(i).getWageAmt() != null && !workerList.get(i).getWageAmt().equals(BigDecimal.ZERO))? workerList.get(i).getWageAmt().doubleValue() : 0;
                        ExcelPoiUtil.getCellRef(sheet, "K" + costIdx + "" + i).setCellValue(wageAmt);
                        double mealAmt = (workerList.get(i).getMealAmt() != null && !workerList.get(i).getMealAmt().equals(BigDecimal.ZERO))? workerList.get(i).getMealAmt().doubleValue() : 0;
                        ExcelPoiUtil.getCellRef(sheet, "K" + costIdx + "" + i).setCellValue(mealAmt);
                    }
                }
                sellIdx++;
                costIdx++;
            }

            //남은 셀 숨기기 처리
            if(sellIdx < 62) {
                for (int i = sellIdx - 1; i< 62; i++) {
                    sheet.getRow(i).setZeroHeight(true);
                }
            }
            if(costIdx < 57) {
                int idx = (costIdx * 10) - 1;
                for (int j = idx; j < 570; j++) {
                    sheet.getRow(j).setZeroHeight(true);
                }
            }
            //매출금액 합계 P45
            ExcelPoiUtil.getCellRef(sheet, "P64").setCellValue(pdrMasterView.getTotalSellAmt());

            //원재료비 총합 E570
            int m1Total = (pdrMasterView.getM1Cost() != null)? pdrMasterView.getM1Cost() : 0;
            ExcelPoiUtil.getCellRef(sheet, "E570").setCellValue(m1Total);
            ExcelPoiUtil.getCellRef(sheet, "A591").setCellValue(m1Total);
            //부재료비 총합 J570
            int m2Total = (pdrMasterView.getM2Cost() != null)? pdrMasterView.getM2Cost() : 0;
            ExcelPoiUtil.getCellRef(sheet, "J570").setCellValue(m2Total);
            ExcelPoiUtil.getCellRef(sheet, "D591").setCellValue(m2Total);
            //직접인건비&식대 O570
            int diTotal = (pdrMasterView.getDirectCost() != null)? pdrMasterView.getDirectCost() : 0;
            ExcelPoiUtil.getCellRef(sheet, "O570").setCellValue(diTotal);
            ExcelPoiUtil.getCellRef(sheet, "G591").setCellValue(diTotal);

            // A + B + C Q570
            ExcelPoiUtil.getCellRef(sheet, "Q570").setCellValue(m1Total + m2Total + diTotal);

            //근무시간 남 여 인원소계 시급 금액 비고
            //D575 F575 G H I J M
            int laborIdx = 575;
            for (PdrLaborView item : pdrLaborList) {
                //근무시간
                int laborHour = (item.getLaborHour() != null)? item.getLaborHour() : 0;
                ExcelPoiUtil.getCellRef(sheet, "D" + laborIdx).setCellValue(laborHour);
                //남
                int maleCnt = (item.getMaleCnt() != null)? item.getMaleCnt() : 0;
                ExcelPoiUtil.getCellRef(sheet, "F" + laborIdx).setCellValue(maleCnt);
                //여
                int femaleCnt = (item.getFemaleCnt() != null)? item.getFemaleCnt() : 0;
                ExcelPoiUtil.getCellRef(sheet, "G" + laborIdx).setCellValue(femaleCnt);
                //여
                int totalCnt = (item.getTotalCnt() != null)? item.getTotalCnt() : 0;
                ExcelPoiUtil.getCellRef(sheet, "H" + laborIdx).setCellValue(totalCnt);
                //시급
                double hourlyWage = (item.getHourlyWage() != null)? item.getHourlyWage().doubleValue() : 0;
                ExcelPoiUtil.getCellRef(sheet, "I" + laborIdx).setCellValue(hourlyWage);
                //금액
                double laborAmt = (item.getLaborAmt() != null)? item.getLaborAmt().doubleValue() : 0;
                ExcelPoiUtil.getCellRef(sheet, "J" + laborIdx).setCellValue(laborAmt);
                //memo
                ExcelPoiUtil.getCellRef(sheet, "M" + laborIdx).setCellValue(item.getMemo());
                laborIdx++;
            }

            //인건비 합계 J578
            int laborTotal = (pdrMasterView.getLaborCost() != null)? pdrMasterView.getLaborCost() : 0;
            ExcelPoiUtil.getCellRef(sheet, "J578").setCellValue(laborTotal);
            ExcelPoiUtil.getCellRef(sheet, "I591").setCellValue(laborTotal);

            //H582 L582
            int etcIdx = 582;
            for (PdrExpense item : pdrExpenseList) {
                double directCost = (item.getDirectCost() != null)? item.getDirectCost().doubleValue() : 0;
                ExcelPoiUtil.getCellRef(sheet, "H" + etcIdx).setCellValue(directCost);
                double indirectCost = (item.getIndirectCost() != null)? item.getIndirectCost().doubleValue() : 0;
                ExcelPoiUtil.getCellRef(sheet, "L" + etcIdx).setCellValue(indirectCost);
            }

            //기타경비 소계 P582
            int etcTotal = (pdrMasterView.getEtcCost() != null)? pdrMasterView.getEtcCost() : 0;
            ExcelPoiUtil.getCellRef(sheet, "P582").setCellValue(etcTotal);
            ExcelPoiUtil.getCellRef(sheet, "H588").setCellValue(etcTotal);
            ExcelPoiUtil.getCellRef(sheet, "M591").setCellValue(etcTotal);

            //생산원가 P591 P593
            int prodCost = (pdrMasterView.getProdCost() != null)? pdrMasterView.getProdCost() : 0;
            ExcelPoiUtil.getCellRef(sheet, "P591").setCellValue(prodCost);
            ExcelPoiUtil.getCellRef(sheet, "P593").setCellValue(prodCost);

            //총 매출금액 P592
            int totalSellAmt = (pdrMasterView.getTotalSellAmt() != null) ? pdrMasterView.getTotalSellAmt() : 0;
            ExcelPoiUtil.getCellRef(sheet, "P591").setCellValue(totalSellAmt);

            //경상이익 P594
            int orIncome = (pdrMasterView.getOrdinaryIncome() != null)? pdrMasterView.getOrdinaryIncome() : 0;
            ExcelPoiUtil.getCellRef(sheet, "P591").setCellValue(orIncome);

            //이익율 P595
            int prRate = (pdrMasterView.getProfitRate() != null)? pdrMasterView.getProfitRate() : 0;
            ExcelPoiUtil.getCellRef(sheet, "P591").setCellValue(pdrMasterView.getProfitRate());



            byte[] excelContent = ExcelPoiUtil.toByteArray(workbook);
            ByteArrayResource resource = new ByteArrayResource(excelContent);
            HttpHeaders header = ExcelPoiUtil.getHeader("생산일보", excelContent.length, "xlsx");

            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("엑셀파일 생성중 에러발생!");
        }
    }
}
