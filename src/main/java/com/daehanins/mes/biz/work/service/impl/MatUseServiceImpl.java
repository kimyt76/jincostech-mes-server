package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.common.code.UseState;
import com.daehanins.mes.biz.common.code.WorkItemState;
import com.daehanins.mes.biz.common.code.WorkOrderItemStatus;
import com.daehanins.mes.biz.common.schedule.BatchSchedule;
import com.daehanins.mes.biz.qt.service.IItemTestNoService;
import com.daehanins.mes.biz.work.entity.*;
import com.daehanins.mes.biz.work.mapper.MatUseMapper;
import com.daehanins.mes.biz.work.service.*;
import com.daehanins.mes.biz.mobile.vo.MatUseSaveItem;
import com.daehanins.mes.common.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 자재소요량MatUse 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-09
 */
@Service
public class MatUseServiceImpl extends ServiceImpl<MatUseMapper, MatUse> implements IMatUseService {

    private static final Logger logger = LoggerFactory.getLogger(BatchSchedule.class);

    @Autowired
    private IMatUseService matUseService;

    @Autowired
    private IWorkOrderItemService workOrderItemService;

    @Autowired
    private IGoodsBomService goodsBomService;

    @Autowired
    private IGoodsBomItemService goodsBomItemService;

    @Autowired
    private IItemTestNoService itemTestNoService;

    @Autowired
    private IProdIngService prodIngService;

    @Autowired
    private IMatUseViewService matUseViewService;


    @Transactional
    public List<MatUseView> investmentMatUse (MatUseSaveItem matUseSaveItem) {

        /*업데이트할 대상 조회*/
        MatUse matUse = matUseService.getById( matUseSaveItem.getMatUseId() );
        WorkOrderItem workOrderItem = workOrderItemService.getById( matUseSaveItem.getWorkOrderItemId() );

        /* 투입된 matUse의 상태를 변경한다.*/
        matUse.setProdDatetime(LocalDateTime.now());
        matUse.setProdYn("Y");
        matUse.setUseState(UseState.USED);
        /*정제수의 경우 투입시, 칭량량과 칭량여부를 업데이트한다.*/
        if(matUse.getItemCd().equals("JRW00215") || matUse.getItemCd().equals("JRMSC00011")) {
            matUse.setWeighQty(matUseSaveItem.getWeighQty());
            matUse.setWeighYn("Y");
        }

        matUse.setProdMemberCd(matUseSaveItem.getMemberCd());
        matUseService.saveOrUpdate(matUse);

        /*투입된 matUse의 prodState에 해당하는 prodIng를 조회한다.*/
        QueryWrapper<ProdIng> PIQueryWrapper = new QueryWrapper<>();
        PIQueryWrapper.eq(StringUtils.camelToUnderline("workOrderItemId"), matUseSaveItem.getWorkOrderItemId());
        PIQueryWrapper.eq(StringUtils.camelToUnderline("prodState"), matUse.getProdState());
        ProdIng prodIng = prodIngService.getOne(PIQueryWrapper);

        /*투입단계의 시작시간과 종료시간을 변경한다.*/
        if(prodIng.getStartDatetime() == null){
            prodIng.setStartDatetime( LocalDateTime.now() );
        }
        prodIng.setEndDatetime( LocalDateTime.now() );
        prodIngService.saveOrUpdate(prodIng);

        /*결과 쿼리를 리턴한다.*/
        List<MatUseView> list = matUseViewService.getByWorkOrderItemId(matUseSaveItem.getWorkOrderItemId());

        return list;
    }

    /** 재고사용(칭량) 생성. */
    @Transactional
    public void saveByWorkOrderItem (WorkOrderItem workOrderItem) throws Exception{

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

        String currentProdState = "";
        int serNo = 0;
        for (GoodsBomItem goodsBomItem : goodsBomItems) {
            // 제조상별로 상,serNo 생성위한 첵크
            if (!currentProdState.equals(goodsBomItem.getProdState())) {
                currentProdState = goodsBomItem.getProdState();
                serNo = 0;
            }
            // 자재 품목별 소요량 생성
            MatUse matUse = new MatUse();
            matUse.setWorkOrderItemId(workOrderItem.getWorkOrderItemId());
            matUse.setStorageCd(workOrderItem.getStorageCd());
            matUse.setItemCd(goodsBomItem.getItemCd());
            matUse.setProdState(goodsBomItem.getProdState());
            matUse.setSerNo(++serNo);
            matUse.setWeighYn("N");
            matUse.setProdYn("N");
            // 소숫점 6째자리 반올림
            BigDecimal reqQty = (goodsBomItem.getContentRatio() == null) ? BigDecimal.ZERO : orderQty.multiply(goodsBomItem.getContentRatio()).divide(new BigDecimal(100)).setScale(6, BigDecimal.ROUND_HALF_UP);
            matUse.setReqQty(reqQty);
            matUse.setWeighQty(BigDecimal.ZERO);
            matUse.setBagWeight(BigDecimal.ZERO);
            matUse.setUseState("ORDER");   // 작업지시
            this.save(matUse);
        }
    }

    @Transactional
    public List<MatUse> saveProdItems(List<MatUse> matUseList) {
        List<MatUse> data = new ArrayList<MatUse>();

        // 작업지시상세
        String workOrderItemId = matUseList.get(0).getWorkOrderItemId();

        // item 신규,수정 처리
        matUseList.forEach( item -> {
            if (item.getProdYn().equals("Y") ) {
                item.setUseState(UseState.USED); // 투입
                if (item.getProdDatetime() == null) {
                    item.setProdDatetime(LocalDateTime.now());
                }
            }
            this.matUseService.saveOrUpdate(item);
            data.add(item);
        });

        // 모든 것이 투입완료되지는 않은 상태 = 제조 상태
        int count = this.matUseService.count(new QueryWrapper<MatUse>()
                .eq("work_order_item_id", workOrderItemId)
                .ne("prod_yn","Y")
        );
        // 이미 제조로 등록되지 않은 경우, 제조로 반영
        if (count > 0) {
            this.workOrderItemService.update(new UpdateWrapper<WorkOrderItem>()
            .eq("work_order_item_id", workOrderItemId).eq("work_item_state", WorkItemState.칭량완료)
            .set("work_item_state", WorkItemState.제조));
        }

        this.baseMapper.updateProdIngTime(workOrderItemId);

        return data;
    }

    /** 칭량생성 삭제 */
    @Transactional
    public void deleteByWorkOrderItemId (String workOrderItemId){
        QueryWrapper<MatUse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_order_item_id", workOrderItemId);
        this.remove(queryWrapper);
    }
}
