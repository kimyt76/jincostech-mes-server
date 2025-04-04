package com.daehanins.mes.biz.qt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.Update;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.adm.entity.CommonCode;
import com.daehanins.mes.biz.adm.service.ICommonCodeService;
import com.daehanins.mes.biz.common.code.*;
import com.daehanins.mes.biz.common.util.BizDateUtil;
import com.daehanins.mes.biz.mat.vo.MatLabelItem;
import com.daehanins.mes.biz.pub.entity.ItemMaster;
import com.daehanins.mes.biz.pub.entity.ItemMasterView;
import com.daehanins.mes.biz.pub.service.IItemMasterViewService;
import com.daehanins.mes.biz.qt.entity.ItemTestNo;
import com.daehanins.mes.biz.qt.entity.ItemTestNoView;
import com.daehanins.mes.biz.qt.entity.QualityTest;
import com.daehanins.mes.biz.qt.mapper.ItemTestNoMapper;
import com.daehanins.mes.biz.qt.service.IItemTestNoService;
import com.daehanins.mes.biz.qt.service.IItemTestNoViewService;
import com.daehanins.mes.biz.qt.service.IQualityTestService;
import com.daehanins.mes.biz.qt.vo.QrCodeInfo;
import com.daehanins.mes.biz.qt.vo.ReqPrinting;
import com.daehanins.mes.biz.work.entity.ProdResult;
import com.daehanins.mes.biz.work.entity.WorkOrderItem;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;
import com.daehanins.mes.biz.work.service.IWorkOrderItemService;
import com.daehanins.mes.biz.work.service.IWorkOrderItemViewService;
import com.daehanins.mes.common.utils.AuthUtil;
import com.daehanins.mes.common.utils.BarcodeUtil;
import com.daehanins.mes.common.utils.JasperUtil;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 품목시험번호ItemTestNo 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-03
 */
@Service
public class ItemTestNoServiceImpl extends ServiceImpl<ItemTestNoMapper, ItemTestNo> implements IItemTestNoService {

    @Autowired
    private IWorkOrderItemViewService workOrderItemViewService;

    @Autowired
    private IWorkOrderItemService workOrderItemService;

    @Autowired
    private IQualityTestService qualityTestService;

    @Autowired
    private IItemTestNoViewService itemTestNoViewService;

    @Autowired
    private IItemMasterViewService itemMasterViewService;

    @Autowired
    private ICommonCodeService commonCodeService;

    public Integer getNextSeq(LocalDate testDate, String areaGb, String itemGb) {
        return this.baseMapper.getNextSeq(testDate, areaGb, itemGb);
    }

    public String getNextTestNo(LocalDate testDate, String areaGb, String itemGb, Integer serNo) {
        // yyyyMMdd + itemGb + %03d
        StringBuilder sb = new StringBuilder( testDate.format(DateTimeFormatter.ofPattern("yyMMdd")) );
        sb.append( areaGb);
        sb.append( itemGb);
        sb.append( String.format("%03d", serNo));
        return sb.toString();
    }

    @Transactional
    public ItemTestNo saveTestNoWithWorkStart (WorkOrderItemView workOrderItemView) {

        String areaCd = workOrderItemView.getAreaCd();
        String processCd = workOrderItemView.getProcessCd();

        LocalDate prodDate = LocalDate.now(); //시험번호 생성일, 현재날짜
        String areaGb = AreaCd.findAreaGb(areaCd); // 시험번호에 삽입할 구역번호
        String itemGb = ItemGb.findItemGb(workOrderItemView.getProcessCd()); //시험번호에 삽입할 품목구분번호
        Integer serNo = getNextSeq(prodDate, areaGb, itemGb); //당일 해당 품목구분번호에 해당하는 순번 채번
        String testNo = getNextTestNo(prodDate, areaGb, itemGb, serNo); //시험번호 포맷으로 생성
        String lotNo = (workOrderItemView.getProcessCd().equals(ProcessCd.포장))? workOrderItemView.getLotNo2() : workOrderItemView.getLotNo();

        ItemMasterView itemMasterView = itemMasterViewService.getById(workOrderItemView.getItemCd());
        String prodMdType = itemMasterView.getProdMdType();

        ItemTestNo itemTestNo = new ItemTestNo();

        if (itemGb == ItemGb.반제품) {
            LocalDate shelfLife = LocalDate.now().plusDays(365 * 2);
            LocalDate expiryDate = LocalDate.now().plusDays(365 * 2);
            if(prodMdType != null && !prodMdType.isEmpty()) {
                QueryWrapper<CommonCode> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("code_type", "PROD_MD_TYPE");
                queryWrapper.eq("code", prodMdType);
                CommonCode commonCode = commonCodeService.getOne(queryWrapper);
                if(commonCode.getOpt2() != null && !commonCode.getOpt2().isEmpty()) {
                    expiryDate = LocalDate.now().plusDays(Integer.parseInt(commonCode.getOpt2()));
                }
                if(commonCode.getOpt3() != null && !commonCode.getOpt3().isEmpty()) {
                    shelfLife = LocalDate.now().plusDays(Integer.parseInt(commonCode.getOpt3()));
                }
                itemTestNo.setExpiryDate(expiryDate);
                itemTestNo.setShelfLife(shelfLife);
            }
        }
        itemTestNo.setTestNo(testNo);
        itemTestNo.setCreateDate(prodDate);
        itemTestNo.setAreaGb(areaGb);
        itemTestNo.setItemGb(itemGb);
        itemTestNo.setSerNo(serNo);
        itemTestNo.setItemCd(workOrderItemView.getItemCd());
        itemTestNo.setLotNo(lotNo);
        itemTestNo.setProdNo(workOrderItemView.getProdNo());
        itemTestNo.setCustomerCd(workOrderItemView.getCustomerCd());
        itemTestNo.setQty(BigDecimal.ZERO); //최초 생성시에는 0 으로 생성.

        //시험검사가 있는 공정인지 체크
        boolean testYn = ProcessCd.getTestYn(processCd);

        //제조, 충전일 경우에만 REQ, 그외에는 END, PASS
        itemTestNo.setTestState((testYn)? TestState.REQ : TestState.END);
        itemTestNo.setPassState((testYn)? PassState.REQ : PassState.PASS);
        itemTestNo.setEndYn("N");
        this.saveOrUpdate(itemTestNo);

        return itemTestNo;
    }

    @Transactional
    public boolean updateQtyByWorkEnd (WorkOrderItemView workOrderItemView) {
        UpdateWrapper<ItemTestNo> testNoUpdateWrapper = new UpdateWrapper<>();
        testNoUpdateWrapper.eq("test_no", workOrderItemView.getItemTestNo());
        testNoUpdateWrapper.set("qty", workOrderItemView.getProdQty());
        return this.update(testNoUpdateWrapper);
    }

    public byte[] createQrCodeLabels (ReqPrinting[] reqPrintings) throws Exception {
        List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
        for ( ReqPrinting item : reqPrintings ) {
            JasperPrint qrCodeLabel = this.createQrCodeInfo(item.getTestNo(), item.getQty());
            for ( int i = 0 ; i < item.getPrintCnt() ; i++ ) {
                jasperPrintList.add(qrCodeLabel);
            }
        }
        JRPdfExporter exporter = new JRPdfExporter();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
        exporter.exportReport();
        return baos.toByteArray();
    }

    public JasperPrint createQrCodeInfo (String testNo, BigDecimal qty) throws Exception {
        QrCodeInfo qrCodeInfo = new QrCodeInfo();
        DecimalFormat df = new DecimalFormat("#,##0.000");
        DecimalFormat df2 = new DecimalFormat("#,##0");

        ItemTestNoView itemTestNoView = itemTestNoViewService.getById(testNo);
        ItemMasterView itemMasterView = itemMasterViewService.getById(itemTestNoView.getItemCd());
        String itemTypeCd = itemMasterView.getItemTypeCd();

        String itemCdWithBracket =  "(" + itemMasterView.getItemCd() + ")";
        qrCodeInfo.setItemCd(itemCdWithBracket);

        qrCodeInfo.setItemName(itemMasterView.getItemName());
        qrCodeInfo.setItemTypeCd(itemTypeCd);

        String supplyGb = (itemMasterView.getItemGrp1Name() == null) ? "사급" : itemMasterView.getItemGrp1Name();
        qrCodeInfo.setSupplyGb(supplyGb);

        String itemCondition = (itemMasterView.getItemCondition() == null) ? "실온" : itemMasterView.getItemConditionName();
        qrCodeInfo.setItemCondition(itemCondition);
        qrCodeInfo.setTestNo(testNo);

        //로트번호 치환 !! -> " "

        String lotNo = (itemTestNoView.getLotNo() != null)?itemTestNoView.getLotNo().replaceAll("!!", " ") : "";


        qrCodeInfo.setLotNo(lotNo);
        qrCodeInfo.setProdNo(itemTestNoView.getProdNo());

        String unit = (itemMasterView.getUnit() == null)? "" : itemMasterView.getUnit();
        Boolean isKg = (unit.equals("kg") || unit.equals("KG") || unit.equals("Kg"));
        String strQty = (isKg) ? df.format(qty) : df2.format(qty);
        qrCodeInfo.setStrQty(strQty + " " + unit);

        qrCodeInfo.setCustomerName(itemTestNoView.getCustomerName());
        qrCodeInfo.setCreateDate(itemTestNoView.getCreateDate());
        qrCodeInfo.setExpiryDate(itemTestNoView.getExpiryDate());
        qrCodeInfo.setShelfLife(itemTestNoView.getShelfLife());
        qrCodeInfo.setPassStateName(itemTestNoView.getPassStateName());

        qrCodeInfo.setBarcodeImage(this.createQrCodeImage(testNo));//바코드 이미지 생성.

        if(itemTypeCd.equals(ItemTypeCd.완제품)) {
            qrCodeInfo.setChargingTestNo(this.getCharingTestNo(testNo));
        }

        List<QrCodeInfo> qrCodeInfoList = new ArrayList<>();
        qrCodeInfoList.add(qrCodeInfo);

        Map<String, Object> parameters = new HashMap<>();

        InputStream reportStream = getClass().getResourceAsStream("/report/prod_label_" + itemTypeCd + ".jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(qrCodeInfoList);

        return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
    }

    public BufferedImage createQrCodeImage (String testNo) throws Exception {
        BufferedImage result;
        try {
            result = BarcodeUtil.generateQRCodeImage(testNo);
        } catch(Exception ex) {
            result = null;
        }
        return result;
    }

    public String getCharingTestNo (String testNo) {

        QueryWrapper<WorkOrderItem> packgingQuery = new QueryWrapper<>();
        packgingQuery.eq("item_test_no", testNo);
        WorkOrderItem packingItem = workOrderItemService.getOne(packgingQuery);

        QueryWrapper<WorkOrderItem> chargingQuery = new QueryWrapper<>();
        chargingQuery.eq("work_order_batch_id", packingItem.getWorkOrderBatchId());
        chargingQuery.eq("process_cd", ProcessCd.충전);
        WorkOrderItem chargingItem = workOrderItemService.getOne(chargingQuery);
        String result = chargingItem.getItemTestNo();

        return (result != null && !result.equals("")) ? result : testNo;
    }
}
