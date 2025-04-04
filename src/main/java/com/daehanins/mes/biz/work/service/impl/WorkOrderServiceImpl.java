package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.common.code.ProcessCd;
import com.daehanins.mes.biz.common.code.ProdStorageCd;
import com.daehanins.mes.biz.common.code.SaveActionCd;
import com.daehanins.mes.biz.common.code.WorkOrderItemStatus;
import com.daehanins.mes.biz.mat.entity.MatTranItem;
import com.daehanins.mes.biz.mat.service.IMatTranItemService;
import com.daehanins.mes.biz.mat.service.IMatTranService;
import com.daehanins.mes.biz.pub.service.IItemMasterService;
import com.daehanins.mes.biz.pub.service.IStorageService;
import com.daehanins.mes.biz.work.entity.*;
import com.daehanins.mes.biz.work.mapper.WorkOrderMapper;
import com.daehanins.mes.biz.work.service.*;
import com.daehanins.mes.biz.work.vo.WorkOrderBatchItem;
import com.daehanins.mes.biz.work.vo.WorkOrderBatchSaveWithItems;
import com.daehanins.mes.biz.work.vo.WorkOrderReadWithItems;
import com.daehanins.mes.common.exception.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 작업지시WorkOrder 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-28
 */
@Service
public class WorkOrderServiceImpl extends ServiceImpl<WorkOrderMapper, WorkOrder> implements IWorkOrderService {

    @Autowired
    private IWorkOrderViewService workOrderViewService;

    @Autowired
    private IWorkOrderBatchService workOrderBatchService;

    @Autowired
    private IWorkOrderItemService workOrderItemService;

    @Autowired
    private IWorkOrderItemViewService workOrderItemViewService;

    @Autowired
    private IMatUseService matUseService;

    @Autowired
    private IMatUseEtcService matUseEtcService;

    @Autowired
    private IProdIngService prodIngService;

    @Autowired
    private IMatTranService matTranService;

    @Autowired
    private IMatTranItemService matTranItemService;

    /** 주문 & 배치 & 작업지시 조회 **/
    public WorkOrderReadWithItems getWithItems (String workOrderId) {

        WorkOrderReadWithItems resultData = new WorkOrderReadWithItems();

        WorkOrderView workOrder             = workOrderViewService.getById(workOrderId);
        List<WorkOrderBatch> batchList  = workOrderBatchService.getByOrderId(workOrderId);
        List<WorkOrderItemView> itemList    = workOrderItemViewService.getByWorkOrderId(workOrderId);

        resultData.setWorkOrderView(workOrder);
        resultData.setWorkOrderBatchItems(reassembleBatchItemList(batchList, itemList));

        return resultData;
    }

    /** 주문 & 배치 & 작업지시 Obj 조립 **/
    public List<WorkOrderBatchItem> reassembleBatchItemList ( List<WorkOrderBatch> batchList, List<WorkOrderItemView> itemList ) {
        List<WorkOrderBatchItem> resultList = new ArrayList<>();

        for ( WorkOrderBatch workOrderBatch : batchList ) {
            WorkOrderBatchItem batchItem = new WorkOrderBatchItem();
            batchItem.initWorkOrderBatchItem(workOrderBatch);

            for ( WorkOrderItemView workOrderItemView : itemList ) {
                if (workOrderBatch.getWorkOrderBatchId().equals(workOrderItemView.getWorkOrderBatchId())) {
                    fillWorkOrderItemView(batchItem, workOrderItemView);
                }
            }
            resultList.add(batchItem);
        }
        return resultList;
    }

    /** 주문 & 배치 & 작업지시 Obj 조립 **/
    public void fillWorkOrderItemView (WorkOrderBatchItem batchItem, WorkOrderItemView workOrderItemView) {
        switch (workOrderItemView.getProcessCd()) {
            case ProcessCd.칭량:
                batchItem.setWeighInfo(workOrderItemView);
                break;
            case ProcessCd.제조:
                batchItem.setProdInfo(workOrderItemView);
                break;
            case ProcessCd.코팅:
                batchItem.setCoatingInfo(workOrderItemView);
                break;
            case ProcessCd.충전:
                batchItem.setChargingInfo(workOrderItemView);
                break;
            case ProcessCd.포장:
                batchItem.setPackingInfo(workOrderItemView);
                break;
            default:
                break;
        }
    }

    /** 주문 & 배치 & 작업지시 저장 **/
    @Transactional
    public WorkOrderReadWithItems saveWithItems (WorkOrder wo, List<WorkOrderBatchSaveWithItems> woItems, List<WorkOrderBatchSaveWithItems> woDeleteItems) throws Exception {

        //작업지시(주문) 저장
        this.saveOrUpdate(wo);
        String workOrderId = wo.getWorkOrderId();
        String areaCd = wo.getAreaCd();

        //삭제 대상 배치 및 작업지시 삭제
        this.deleteBatchWithItems(woDeleteItems);

        // 배치 및 작업지시 저장
        int batchSerNo = 0;
        for ( WorkOrderBatchSaveWithItems batchItem : woItems ) {
            //배치 저장.
            batchSerNo++;
            WorkOrderBatch targetBatch = batchItem.getBatchInfo();
            targetBatch.setWorkOrderId(workOrderId);
            targetBatch.setBatchSerNo(batchSerNo);
            targetBatch.setAreaCd(areaCd);
            workOrderBatchService.saveOrUpdate(targetBatch);

            //작업지시 저장
            this.workOrderItemSaveOrUpdate(targetBatch, batchItem.getWeighInfo());
            this.workOrderItemSaveOrUpdate(targetBatch, batchItem.getProdInfo());
            this.workOrderItemSaveOrUpdate(targetBatch, batchItem.getCoatingInfo());
            this.workOrderItemSaveOrUpdate(targetBatch, batchItem.getChargingInfo());
            this.workOrderItemSaveOrUpdate(targetBatch, batchItem.getPackingInfo());
        }
        return getWithItems(workOrderId);
    }
    
    /** 주문 저장 **/
    @Override
    public boolean saveOrUpdate(WorkOrder entity) {
        if (entity.getSerNo() == null) {
            entity.setSerNo(this.baseMapper.getNextSeq(entity.getOrderDate()));
        }
        return super.saveOrUpdate(entity);
    }
    
    /** 작업지시 저장 **/
    @Transactional
    public void workOrderItemSaveOrUpdate (WorkOrderBatch workOrderBatch, WorkOrderItem workOrderItem) throws Exception {

        WorkOrderItem targetItem = workOrderItemService.getById(workOrderItem.getWorkOrderItemId());

        switch ( checkActionByItem(workOrderItem, targetItem) ) {
            // 신규저장.
            case SaveActionCd.신규 :
                workOrderItem.setWorkOrderBatchId(workOrderBatch.getWorkOrderBatchId());
                //작업 시작 시 작업처를 등록하는 것으로 변경.
                //workOrderItem.setStorageCd(this.defaultStorageCd(workOrderBatch.getAreaCd(), workOrderItem.getProcessCd()));
                workOrderItemService.saveWorkOrderItem(workOrderItem);
                break;
            // 수정.
            case SaveActionCd.수정 :
                workOrderItemService.updateWorkOrderItem(workOrderItem);
                break;
            // 삭제.
            case SaveActionCd.삭제 :
                workOrderItemService.removeById(workOrderItem.getWorkOrderItemId());
                break;
            // 취소.
            case SaveActionCd.취소 :
                workOrderItemService.cancelWorkOrderItem(workOrderItem);
                break;
            default:
                //pass 처리.
                break;
        }
    }
    
    /** 작업지시 판별작업 **/
    public String checkActionByItem (WorkOrderItem workOrderItem, WorkOrderItem targetItem) {

        /** A. 신규 작업지시에 대한 처리 **/
        if (Objects.isNull(targetItem)) {
            /** 1. 정상적인 데이터일 경우 - 신규저장 **/
            if (StringUtils.isNotBlank(workOrderItem.getItemCd()) && workOrderItem.getOrderQty() != null && workOrderItem.getOrderDate() != null) {
                return SaveActionCd.신규;
            /** 2. 그 외 - 패스 **/
            }else {
                return SaveActionCd.패스;
            }
        }
        /** B. 기존 작업지시에 대한 처리 **/
        /** 1. 서버의 상태값이 작업지시 상태가 아닐 경우 - 패스 **/
        if (!targetItem.getWorkOrderItemStatus().equals(WorkOrderItemStatus.작업지시) ) {
            return SaveActionCd.패스;
        }
        /** 2. 전달된 상태값이 작업취소일 경우 - 취소상태로 변경 **/
        if (workOrderItem.getWorkOrderItemStatus().equals(WorkOrderItemStatus.작업취소)) {
            return SaveActionCd.취소;
        }
        /** 3. 전달된 입력값이 모두 Null일 경우 - 삭제 **/
        if (StringUtils.isBlank(workOrderItem.getItemCd()) && workOrderItem.getOrderQty() == null && workOrderItem.getOrderDate() == null ){
            return SaveActionCd.삭제;
        }
        /** 전달된 입력값이 모두 있을 경우 **/
        if (StringUtils.isNotBlank(workOrderItem.getItemCd()) && workOrderItem.getOrderQty() != null && workOrderItem.getOrderDate() != null){
            /** 4. 전달된 입력값이 서버의 값과 모두 동일할 경우 - 패스 **/
            if(targetItem.getItemCd().equals(workOrderItem.getItemCd())
                && targetItem.getOrderQty() == workOrderItem.getOrderQty()
                && targetItem.getOrderDate() == workOrderItem.getOrderDate()){
                return SaveActionCd.패스;
            } else {
                /** 5. 전달된 입력값이 서버의 값과 다를 경우 - 수정 **/
                return SaveActionCd.수정;
            }
        }
        return SaveActionCd.패스;
    }

    /** 삭제대상 배치 & 작업지시 삭제 **/
    @Transactional
    public void deleteBatchWithItems (List<WorkOrderBatchSaveWithItems> woDeleteItems) {
        for ( WorkOrderBatchSaveWithItems batchItem : woDeleteItems ) {
            //삭제 대상 배치ID 조회
            String removeTargetBatchId = batchItem.getBatchInfo().getWorkOrderBatchId();
            //matUse 삭제
            this.matUseService.remove(new QueryWrapper<MatUse>().in("work_order_item_id", batchItem.getWeighInfo().getWorkOrderItemId()));
            this.matUseEtcService.remove(new QueryWrapper<MatUseEtc>().in("work_order_item_id", batchItem.getWeighInfo().getWorkOrderItemId()));
            this.prodIngService.remove(new QueryWrapper<ProdIng>().in("work_order_item_id", batchItem.getWeighInfo().getWorkOrderItemId()));
            //삭제 대상 배치ID에 해당하는 작업지시 삭제
            workOrderItemService.remove(new QueryWrapper<WorkOrderItem>().eq(StringUtils.camelToUnderline("workOrderBatchId"), removeTargetBatchId));
            //배치 삭제
            workOrderBatchService.removeById(removeTargetBatchId);
        }
    }

    @Transactional
    public String deleteWithItems ( String workOrderId ) throws Exception {
        String message = "";

        //진행된 작업지시가 0건일 시
        if ( workOrderItemViewService.countUnmodifiableItem(workOrderId) == 0 ) {
            List<WorkOrderItemView> workOrderItemViews = workOrderItemViewService.getByWorkOrderId(workOrderId);
            List<String> workOrderItemIds = new ArrayList<>();
            for (WorkOrderItemView item : workOrderItemViews){
                matUseService.remove(new QueryWrapper<MatUse>().eq("work_order_item_id", item.getWorkOrderItemId()));
                matUseEtcService.remove(new QueryWrapper<MatUseEtc>().eq("work_order_item_id", item.getWorkOrderItemId()));
                prodIngService.remove(new QueryWrapper<ProdIng>().eq("work_order_item_id", item.getWorkOrderItemId()));
                workOrderItemIds.add(item.getWorkOrderItemId());
            }
            // matUse, workOrderItem  삭제
            //matUseService.remove(new QueryWrapper<MatUse>().in("work_order_item_id", workOrderItemIds));
            workOrderItemService.removeByIds(workOrderItemIds);

            //workOrderBatch 삭제
            workOrderBatchService.remove(new QueryWrapper<WorkOrderBatch>().eq("work_order_id", workOrderId));

            //workOrder 삭제
            if(this.removeById(workOrderId)) {
                message = "success";
            } else {
                message = "작업지시를 삭제하는데 실패했습니다.";
                throw new BizException(message);
            }
        } else {
            message = "삭제불가! 이미 진행된 작업지시가 존재합니다.";
            throw new BizException(message);
        }
        return message;
    }

    //TODO 해당 서비스가 미사용이 결정되면 삭제한다. 다른 공정에서 발생할수도...?
    @Transactional
    public WorkOrderReadWithItems cancelBatchItems ( String workOrderBatchId ) throws Exception {
        String message = "";
        List<WorkOrderItem> targetItems = workOrderItemService.getByBatchId(workOrderBatchId);
        for ( WorkOrderItem item : targetItems ) {
            //해당 배치의 작업지시 상태인 것만 작업 취소로 수정.
            if(item.getWorkOrderItemStatus().equals(WorkOrderItemStatus.작업지시)){
                item.setWorkOrderItemStatus(WorkOrderItemStatus.작업취소);
                if( workOrderItemService.updateById(item) ) {
                    message = "success";
                } else {
                    message = "작업 취소 처리에 실패했습니다. 다시 시도해주세요.";
                    throw new BizException(message);
                }
            }
        }
        return this.getWithItems(workOrderBatchService.getById(workOrderBatchId).getWorkOrderId());
    }

    /** 기본작업처 코드 리턴 */
    public String defaultStorageCd ( String areaCd, String processCd ) {

        String result = "";

        if ( areaCd.equals("A001") && processCd.equals("PRC001")) result = ProdStorageCd.시화칭량;
        if ( areaCd.equals("A001") && processCd.equals("PRC002")) result = ProdStorageCd.시화제조;
        if ( areaCd.equals("A001") && processCd.equals("PRC003")) result = ProdStorageCd.시화코팅;
        if ( areaCd.equals("A002") && processCd.equals("PRC001")) result = ProdStorageCd.안산칭량;
        if ( areaCd.equals("A002") && processCd.equals("PRC002")) result = ProdStorageCd.안산제조;
        if ( areaCd.equals("A002") && processCd.equals("PRC003")) result = ProdStorageCd.안산코팅;

        return result;
    }

    @Transactional
    public boolean reorderWorkBatch (String workOrderBatchId) {
        boolean result = true;
        List<WorkOrderItem> targetItems = workOrderItemService.getByBatchId(workOrderBatchId);
        for ( WorkOrderItem item : targetItems ) {
            if (item.getTranYn().equals("Y")) {
                matTranItemService.remove(new QueryWrapper<MatTranItem>().eq("mat_tran_id", item.getTranId()));
                matTranService.removeById(item.getTranId());
                item.setTranId(null);
                item.setTranYn("N");
            }
            item.setWorkStartTime(null);
            item.setWorkEndTime(null);
            item.setWorkOrderItemStatus(WorkOrderItemStatus.작업지시);
            workOrderItemService.updateById(item);
        }
        return true;
    }
}
