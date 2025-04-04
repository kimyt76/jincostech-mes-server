package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.common.code.ConfirmState;
import com.daehanins.mes.biz.common.code.ProdStorageCd;
import com.daehanins.mes.biz.common.code.TranCd;
import com.daehanins.mes.biz.mat.entity.MatTran;
import com.daehanins.mes.biz.mat.entity.MatTranItem;
import com.daehanins.mes.biz.mat.service.IMatTranService;
import com.daehanins.mes.biz.work.entity.*;
import com.daehanins.mes.biz.work.mapper.MatUseEtcMapper;
import com.daehanins.mes.biz.work.service.*;
import com.daehanins.mes.common.exception.BizException;
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
 * MatUseEtc 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2021-05-03
 */
@Service
public class MatUseEtcServiceImpl extends ServiceImpl<MatUseEtcMapper, MatUseEtc> implements IMatUseEtcService {

    @Autowired
    private IGoodsBomService goodsBomService;

    @Autowired
    private IGoodsBomItemService goodsBomItemService;

    @Autowired
    private IWorkOrderItemService workOrderItemService;

    @Autowired
    private IWorkOrderItemViewService workOrderItemViewService;

    @Autowired
    private IMatUseEtcResultViewService matUseEtcResultViewService;

    @Autowired
    private IMatTranService matTranService;

    /** 재고사용 생성 */
    @Transactional
    public void saveByWorkOrderItem (WorkOrderItem workOrderItem) throws Exception {
        String itemCd = workOrderItem.getItemCd();
        BigDecimal orderQty = workOrderItem.getOrderQty();

        QueryWrapper<GoodsBom> goodsBomQueryWrapper = new QueryWrapper<>();
        goodsBomQueryWrapper.eq("item_cd", itemCd).eq("default_yn", "Y");

        GoodsBom goodsBom = goodsBomService.getOne(goodsBomQueryWrapper);
        if (goodsBom == null) {
            throw new BizException("품목의 기본 BOM 정보를 확인하세요.");
        }

        String goodsBomId = goodsBom.getGoodsBomId();
        QueryWrapper<GoodsBomItem> goodsBomItemQueryWrapper = new QueryWrapper<>();

        // serNO를 bom등록 순서와 일치시키기 위해서 item_cd를 goods_bom_item_id 로 변경함
        goodsBomItemQueryWrapper.eq("goods_bom_id", goodsBomId)
                .orderByAsc("prod_state","goods_bom_item_id");

        List<GoodsBomItem> goodsBomItems = goodsBomItemService.list(goodsBomItemQueryWrapper);

        BigDecimal stdQty = goodsBom.getProdQty();

        int serNo = 0;
        for (GoodsBomItem goodsBomItem : goodsBomItems) {
            // 자재 품목별 소요량 생성
            MatUseEtc matUseEtc = new MatUseEtc();
            matUseEtc.setWorkOrderItemId(workOrderItem.getWorkOrderItemId());
            matUseEtc.setStorageCd(workOrderItem.getStorageCd());
            matUseEtc.setItemCd(goodsBomItem.getItemCd());
            matUseEtc.setSerNo(++serNo);
            // EA로 떨어지기때문에 소숫점없이 반올림처리.
            BigDecimal reqQty = (goodsBomItem.getQty() == null) ? BigDecimal.ZERO : orderQty.multiply(goodsBomItem.getQty()).divide(stdQty, 6, BigDecimal.ROUND_HALF_UP);
            matUseEtc.setReqQty(reqQty);
            this.save(matUseEtc);
        }
    }

    /** 재고사용 삭제 */
    @Transactional
    public void deleteByWorkOrderItemId (String workOrderItemId) {
        QueryWrapper<MatUseEtc> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_order_item_id", workOrderItemId);
        this.remove(queryWrapper);
    }

    @Transactional
    public String finishMatUseEtc (String workOrderItemId) {
        WorkOrderItemView workOrderItemView = workOrderItemViewService.getById(workOrderItemId);
        WorkOrderItem workOrderItem = workOrderItemService.getById(workOrderItemId);

        MatTran matTran = new MatTran();

        String targetAreaCd = workOrderItemView.getAreaCd();

        matTran.setTranCd(TranCd.E); // E: 제조출고  G: 외주출고
        matTran.setAreaCd(targetAreaCd);
        matTran.setDestStorageCd(ProdStorageCd.getFieldStorageCd(targetAreaCd)); //구역별 현장창고
        matTran.setSrcStorageCd(workOrderItemView.getStorageCd());
        matTran.setMemberCd(AuthUtil.getMemberCd());
        matTran.setTranDate(LocalDate.now());
        matTran.setEndYn("N");
        matTran.setConfirmState(ConfirmState.OK);

        List<MatUseEtcResultView> matUseEtcResultViews = matUseEtcResultViewService.list(
                new QueryWrapper<MatUseEtcResultView>().eq("work_order_ite_id", workOrderItem));

        List<MatTranItem> matTranItemList = new ArrayList<>();
        matUseEtcResultViews.forEach( item -> {
            MatTranItem matTranItem = new MatTranItem();
            matTranItem.setMatTranItemId(item.getMatUseEtcResultId());
            matTranItem.setItemTypeCd(item.getItemTypeCd());    // 원재료,  칭량은 모두 원재료
            matTranItem.setItemCd(item.getItemCd());
            matTranItem.setItemName(item.getItemName());
            matTranItem.setLotNo(item.getLotNo());
            matTranItem.setTestNo(item.getTestNo());
            matTranItem.setQty(item.getUseQty());
            matTranItem.setConfirmState(ConfirmState.OK);
            matTranItemList.add(matTranItem);
        });
        matTranService.saveWithItems(matTran, matTranItemList, new ArrayList<MatTranItem>());

        String msg = "success";

        return msg;
    }
}
