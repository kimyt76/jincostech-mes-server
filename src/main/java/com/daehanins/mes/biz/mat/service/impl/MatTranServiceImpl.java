package com.daehanins.mes.biz.mat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.common.code.*;
import com.daehanins.mes.biz.common.util.BizDateUtil;
import com.daehanins.mes.biz.mat.entity.MatOrder;
import com.daehanins.mes.biz.mat.entity.MatTran;
import com.daehanins.mes.biz.mat.entity.MatTranItem;
import com.daehanins.mes.biz.mat.mapper.MatTranMapper;
import com.daehanins.mes.biz.mat.service.IMatOrderService;
import com.daehanins.mes.biz.mat.service.IMatTranItemService;
import com.daehanins.mes.biz.mat.service.IMatTranService;
import com.daehanins.mes.biz.mat.vo.MatTranSaveWithItems;
import com.daehanins.mes.biz.pub.entity.Storage;
import com.daehanins.mes.biz.pub.service.IStorageService;
import com.daehanins.mes.biz.qt.entity.ItemTestNo;
import com.daehanins.mes.biz.qt.entity.QualityTest;
import com.daehanins.mes.biz.qt.service.IItemTestNoService;
import com.daehanins.mes.biz.qt.service.IQualityTestService;
import com.daehanins.mes.biz.work.entity.MatUseEtcResultView;
import com.daehanins.mes.biz.work.entity.MatWeighView;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;
import com.daehanins.mes.biz.work.service.IMatUseEtcResultViewService;
import com.daehanins.mes.biz.work.service.IMatWeighViewService;
import com.daehanins.mes.common.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 자재거래MatTran 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-09
 */
@Service
public class MatTranServiceImpl extends ServiceImpl<MatTranMapper, MatTran> implements IMatTranService {

    @Autowired
    private IMatTranService matTranService;

    @Autowired
    private IMatTranItemService matTranItemService;

    @Autowired
    private IItemTestNoService itemTestNoService;

    @Autowired
    private IQualityTestService qualityTestService;

    @Autowired
    private IStorageService storageService;

    @Autowired
    private IMatOrderService matOrderService;

    @Autowired
    private IMatWeighViewService matWeighViewService;

    @Autowired
    private IMatUseEtcResultViewService matUseEtcResultViewService;

    @Override
    @Transactional
    public boolean saveOrUpdate(MatTran entity) {
        if (entity.getSerNo() == null) {
            entity.setSerNo(this.baseMapper.getNextSeq(entity.getTranCd(), entity.getTranDate()));
        }
        return super.saveOrUpdate(entity);
    }

    @Transactional
    public MatTranSaveWithItems saveWithItems(MatTran matTran, List<MatTranItem> matTranItems, List<MatTranItem> matTranDeleteItems) {
        MatTranSaveWithItems data = new MatTranSaveWithItems();

        Storage storage = this.storageService.getById(matTran.getSrcStorageCd());
        matTran.setAreaCd(storage.getAreaCd());
        if (matTran.getErpYn() == null) {
            matTran.setErpYn("N");
        }

        if (this.saveOrUpdate(matTran)) {
            data.setMatTran(matTran);
        }

        // item 삭제 처리
        List<String> deleteIds = new ArrayList<>();
        matTranDeleteItems.forEach( item -> {
            if (StringUtils.isNotBlank(item.getMatTranItemId())) {
                if (StringUtils.isNotBlank(item.getTestNo())) {
                    // 시험번호 삭제
                    ItemTestNo itemTestNo = this.itemTestNoService.getById(item.getTestNo());
                    itemTestNo.setEndYn("Y");
                    //queryWrapper.
                    this.itemTestNoService.saveOrUpdate(itemTestNo);
                    // 품질검사요청을 삭제
                    QualityTest qualityTest = qualityTestService.getOne(new QueryWrapper<QualityTest>().eq("test_no", item.getTestNo()));
                    qualityTest.setTestState("CXL");
                    this.qualityTestService.saveOrUpdate(qualityTest);
                }
                deleteIds.add(item.getMatTranItemId());
            }
        });
        this.matTranItemService.removeByIds(deleteIds);

        // item 신규,수정 처리
        List<MatTranItem> saveItems = new ArrayList<>();
        List<MatTranItem> updateItems = new ArrayList<>();

        matTranItems.forEach( item -> {
            // 입고(A,B,C)이고, 시험번호 없고, 입고수량이 있는 건
            String tranCd = matTran.getTranCd();
            if ( (tranCd.equals("A") || tranCd.equals("B") || tranCd.equals("C")) &&
                    item.getQty().compareTo(new BigDecimal("0")) > 0) {
                if (StringUtils.isBlank(item.getTestNo())) {
                    // 시험번호, 일련번호 채번
                    Integer serNo = this.itemTestNoService.getNextSeq(matTran.getTranDate(), matTran.findAreaGb(), item.findItemGb());
                    String testNo = this.itemTestNoService.getNextTestNo(matTran.getTranDate(), matTran.findAreaGb(), item.findItemGb(), serNo);

                    // 시험번호 생성
                    ItemTestNo itemTestNo = new ItemTestNo();
                    itemTestNo.setTestNo(testNo);
                    itemTestNo.setCreateDate(matTran.getTranDate());
                    itemTestNo.setAreaGb(matTran.findAreaGb());
                    itemTestNo.setItemGb(item.findItemGb());
                    itemTestNo.setSerNo(serNo);
                    itemTestNo.setItemCd(item.getItemCd());
                    itemTestNo.setLotNo(item.getLotNo());
                    itemTestNo.setQty(item.getQty());
                    itemTestNo.setCustomerCd(matTran.getCustomerCd());
                    if (item.getExpiryDate() != null) {
                        itemTestNo.setExpiryDate(item.getExpiryDate());
                        // shelfLife  입고일로 부터 2년 또는 유효기간 중 작은 값
                        itemTestNo.setShelfLife(BizDateUtil.calcShelfLife(matTran.getTranDate(), item.getExpiryDate()));
                    }
                    itemTestNo.setTestState(TestState.REQ);
                    itemTestNo.setPassState(PassState.REQ);
                    itemTestNo.setEndYn("N");
                    this.itemTestNoService.saveOrUpdate(itemTestNo);

                    // 품질검사요청 생성
                    QualityTest newTest = new QualityTest();
                    newTest.setTestNo(testNo);

                    newTest.setReqDate(matTran.getTranDate());
                    newTest.setReqMemberCd(matTran.getMemberCd());
                    newTest.setAreaCd(matTran.getAreaCd());
                    newTest.setStorageCd(matTran.getSrcStorageCd());
                    newTest.setReqQty(item.getQty());
                    newTest.setTestState(TestState.REQ);
                    newTest.setPassState(PassState.REQ);
                    newTest.setRetestYn("N");   // 재검사여부 - 최초등록이므로 "N"
                    newTest.setTranYn("N");   // 재검사여부 - 최초등록이므로 "N"
                    this.qualityTestService.saveOrUpdate(newTest);
                    // 구매(외주)입고품목에 시험번호 설정
                    item.setTestNo(testNo);
                } else {
                    ItemTestNo itemTestNo = this.itemTestNoService.getById(item.getTestNo());
                    itemTestNo.setAreaGb(matTran.findAreaGb());
                    itemTestNo.setItemGb(item.findItemGb());
                    itemTestNo.setItemCd(item.getItemCd());
                    itemTestNo.setLotNo(item.getLotNo());
                    if (item.getExpiryDate() != null) {
                        itemTestNo.setExpiryDate(item.getExpiryDate());
                        // shelfLife  입고일로 부터 2년 또는 유효기간 중 작은 값
                        itemTestNo.setShelfLife(BizDateUtil.calcShelfLife(matTran.getTranDate(), item.getExpiryDate()));
                    }
                    this.itemTestNoService.saveOrUpdate(itemTestNo);

                    QualityTest updTest = this.qualityTestService.getOne(new QueryWrapper<QualityTest>()
                            .eq("test_no", item.getTestNo())
                            .eq("req_date", matTran.getTranDate()));
                    updTest.setAreaCd(matTran.getAreaCd());
                    updTest.setStorageCd(matTran.getSrcStorageCd());
                    updTest.setReqQty(item.getQty());
                    this.qualityTestService.saveOrUpdate(updTest);
                }
            }
            if (StringUtils.isNotBlank(item.getMatTranId())) {
                updateItems.add(item);
            } else {
                item.setMatTranId(matTran.getMatTranId());
                saveItems.add(item);
            }
        });
        this.matTranItemService.saveBatch(saveItems, 1000);
        this.matTranItemService.updateBatchById(updateItems, 1000);

        data.setMatTranItems(matTranItems);

        // 요청이 있는 정보인 경우, tran이 등록 또는 업데이트 되면 matOrder를 진행상태로 변경함
        // 확인상태 대기는 진행중,  확인상태 승인,반려는 종료
        if (!StringUtils.isBlank(matTran.getMatOrderId())) {
            if (matTran.getConfirmState().equals(ConfirmState.ING)) {
                this.matOrderService.update(new UpdateWrapper<MatOrder>().set("order_state", OrderState.ING).eq("mat_order_id", matTran.getMatOrderId()));
            } else {
                this.matOrderService.update(new UpdateWrapper<MatOrder>()
                        .set("order_state", OrderState.END)
                        .set("end_yn", "Y")
                        .eq("mat_order_id", matTran.getMatOrderId()));
            }
        }

        // 여기서 구매입고 관련 상태를 다시 확인해야 한다. QQQ   2020.08.19  jeonsj    현재는 1회입고되면 그대로 종결되게 되어 있음
        if (matTran.getTranCd().equals("A")) {

        }

        return data;
    }

    @Transactional
    public String deleteWithItems(List<String> idList) {
        String msg;

        for (String matTranId : idList) {

            MatTran matTran = this.getById(matTranId);
            if (matTran == null) {
                continue;
            }
            List<MatTranItem> matTranItems = matTranItemService.list(new QueryWrapper<MatTranItem>().eq("mat_tran_id", matTranId));

            // 입고(A,B,C)이고, 시험번호 있는 경우 품질검사요청 삭제
            String tranCd = matTran.getTranCd();
            if (tranCd.equals("A") || tranCd.equals("B") || tranCd.equals("C")) {
                matTranItems.forEach( item -> {
                    if (StringUtils.isNotBlank(item.getTestNo())) {
                        // 시험번호 삭제
//                        this.itemTestNoService.removeById(item.getTestNo());
                        ItemTestNo itemTestNo = this.itemTestNoService.getById(item.getTestNo());
                        itemTestNo.setEndYn("Y");
                        //queryWrapper.
                        this.itemTestNoService.saveOrUpdate(itemTestNo);
                        // 품질검사요청을 삭제
                        List<QualityTest> qualityTests = qualityTestService.list(new QueryWrapper<QualityTest>().eq("test_no", item.getTestNo()));
                        if(qualityTests.size() > 0) {
                            for(QualityTest qualityTest : qualityTests) {
                                qualityTest.setTestState("CXL");
                                this.qualityTestService.saveOrUpdate(qualityTest);
                            }
                        }
                    }
                });
            }
            // 자재거래 아이템 삭제
            matTranItemService.remove(new QueryWrapper<MatTranItem>().eq("mat_tran_id", matTranId));
        }
        // 구매입고 삭제
        if (this.removeByIds(idList)) {
            msg = "success";
        } else {
            msg = "fail";
        }
        return msg;
    }


    @Transactional
    public MatTranSaveWithItems saveWithItemsByMobile(MatTran matTran, List<MatTranItem> matTranItems) {

        MatTranSaveWithItems data = new MatTranSaveWithItems();

        /* matOrder orderState 업데이트 */
        if (matTran.getMatOrderId() != null) {
            MatOrder matOrder = matOrderService.getById(matTran.getMatOrderId());
            matOrder.setOrderState("ING");
            matOrderService.updateById( matOrder );
        }
        /* matTran 저장 */
        if (this.saveOrUpdate(matTran)) {
            data.setMatTran(matTran);
        }

        /* matTranItem 저장 */
        matTranItems.forEach( item -> {
            item.setMatTranId(matTran.getMatTranId());
        });
        this.matTranItemService.saveBatch(matTranItems, 1000);
        data.setMatTranItems(matTranItems);
        return data;
    }

    @Transactional
    public String saveMatTranByWeighEnd (WorkOrderItemView workOrderItemView) {

        String areaCd =  workOrderItemView.getAreaCd();
        String storageCd = workOrderItemView.getStorageCd();

        MatTran matTran = new MatTran();
        matTran.setTranCd(TranCd.E); //제조출고
        matTran.setAreaCd(areaCd);
        matTran.setSrcStorageCd(storageCd);
        matTran.setDestStorageCd(ProdStorageCd.getFieldStorageCd(areaCd)); //구역별 현장창고
        matTran.setMemberCd(AuthUtil.getMemberCd());
        matTran.setTranDate(LocalDate.now());
        matTran.setEndYn("N");
        matTran.setConfirmState(ConfirmState.OK);

        QueryWrapper<MatWeighView> queryWrapper = new QueryWrapper<MatWeighView>();
        queryWrapper.eq("work_order_item_id", workOrderItemView.getWorkOrderItemId());

        List<MatWeighView>  matWeighViews = matWeighViewService.list(queryWrapper);

        List<MatTranItem> matTranItemList = new ArrayList<>();

        for (MatWeighView item : matWeighViews) {
            MatTranItem matTranItem = new MatTranItem();
            matTranItem.setMatTranItemId(item.getMatWeighId());
            matTranItem.setItemTypeCd("M1");    // 원재료,  칭량은 모두 원재료
            matTranItem.setItemCd(item.getItemCd());
            matTranItem.setItemName(item.getItemName());
            matTranItem.setLotNo(item.getLotNo());
            matTranItem.setTestNo(item.getTestNo());
            matTranItem.setQty(item.getWeighQty());
            matTranItem.setConfirmState(ConfirmState.OK);
            matTranItemList.add(matTranItem);
        }


        this.saveWithItems(matTran, matTranItemList, new ArrayList<MatTranItem>());

        return matTran.getMatTranId();
    }

    @Transactional
    public String updateMatUseEtcTran (WorkOrderItemView workOrderItemView) {

        String matTranId = workOrderItemView.getTranId();
        matTranItemService.remove(new QueryWrapper<MatTranItem>().eq("mat_tran_id", matTranId));

        List<MatUseEtcResultView>  matUseEtcResultViews = matUseEtcResultViewService.list(
                new QueryWrapper<MatUseEtcResultView>().eq("work_order_item_id", workOrderItemView.getWorkOrderItemId()));

        List<MatTranItem> matTranItemList = new ArrayList<>();
        for ( MatUseEtcResultView item : matUseEtcResultViews ) {
            MatTranItem matTranItem = new MatTranItem();
            matTranItem.setMatTranId(matTranId);
            //matTranItem.setMatTranItemId(item.getMatUseEtcResultId());
            matTranItem.setItemTypeCd(item.getItemTypeCd());
            matTranItem.setItemCd(item.getItemCd());
            matTranItem.setItemName(item.getItemName());
            matTranItem.setLotNo(item.getLotNo());
            matTranItem.setTestNo(item.getTestNo());
            matTranItem.setQty(item.getUseQty());
            matTranItem.setConfirmState(ConfirmState.OK);
            matTranItemList.add(matTranItem);
        }

        matTranItemService.saveBatch(matTranItemList, 1000);
        return matTranId;
    }

    @Transactional
    public String saveMatUseEtcTranByWorkEnd (WorkOrderItemView workOrderItemView) {

        String areaCd =  workOrderItemView.getAreaCd();
        String storageCd = workOrderItemView.getStorageCd();

        MatTran matTran = new MatTran();
        matTran.setTranCd(TranCd.E); //제조출고
        matTran.setAreaCd(areaCd);
        matTran.setSrcStorageCd(storageCd);
        matTran.setDestStorageCd(ProdStorageCd.getFieldStorageCd(areaCd)); //구역별 현장창고
        matTran.setMemberCd(AuthUtil.getMemberCd());
        matTran.setTranDate(LocalDate.now());
        matTran.setEndYn("N");
        matTran.setConfirmState(ConfirmState.OK);

        QueryWrapper<MatUseEtcResultView> queryWrapper = new QueryWrapper<MatUseEtcResultView>();
        queryWrapper.eq("work_order_item_id", workOrderItemView.getWorkOrderItemId());

        List<MatUseEtcResultView>  matUseEtcResultViews = matUseEtcResultViewService.list(queryWrapper);

        List<MatTranItem> matTranItemList = new ArrayList<>();
        matUseEtcResultViews.forEach( item -> {
            MatTranItem matTranItem = new MatTranItem();
            matTranItem.setMatTranItemId(item.getMatUseEtcResultId());
            matTranItem.setItemTypeCd(item.getItemTypeCd());
            matTranItem.setItemCd(item.getItemCd());
            matTranItem.setItemName(item.getItemName());
            matTranItem.setLotNo(item.getLotNo());
            matTranItem.setTestNo(item.getTestNo());
            matTranItem.setQty(item.getUseQty());
            matTranItem.setConfirmState(ConfirmState.OK);
            matTranItemList.add(matTranItem);
        });

        this.saveWithItems(matTran, matTranItemList, new ArrayList<MatTranItem>());

        return matTran.getMatTranId();
    }

    public boolean isExistMatUseEtc (WorkOrderItemView workOrderItemView) {
        QueryWrapper<MatUseEtcResultView> queryWrapper = new QueryWrapper<MatUseEtcResultView>();
        queryWrapper.eq("work_order_item_id", workOrderItemView.getWorkOrderItemId());
        int cnt = matUseEtcResultViewService.count(queryWrapper);
        return cnt > 0;
    }

    @Transactional
    public MatTran saveMatTranByWorkEnd (WorkOrderItemView workOrderItemView) {
        MatTran matTran = new MatTran();
        matTran.setTranCd(TranCd.B); //제조입고
        matTran.setTranDate(workOrderItemView.getProdDate());
        matTran.setAreaCd(workOrderItemView.getAreaCd());
        matTran.setSrcStorageCd(ProdStorageCd.getFieldStorageCd(workOrderItemView.getAreaCd())); // 구역별현장창고
        matTran.setMemberCd(AuthUtil.getMemberCd());
        matTran.setConfirmMemberCd(AuthUtil.getMemberCd());
        matTran.setConfirmState(ConfirmState.OK);
        matTran.setEndYn("Y");
        matTran.setConfirmState(ConfirmState.OK);
        this.saveOrUpdate(matTran);
        return matTran;
    }
}
