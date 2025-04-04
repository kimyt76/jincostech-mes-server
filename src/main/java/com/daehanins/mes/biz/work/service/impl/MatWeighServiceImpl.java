package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.common.code.UseState;
import com.daehanins.mes.biz.common.code.WorkItemState;
import com.daehanins.mes.biz.common.code.WorkOrderItemStatus;
import com.daehanins.mes.biz.work.entity.MatUse;
import com.daehanins.mes.biz.work.entity.MatWeigh;
import com.daehanins.mes.biz.work.entity.WorkOrderItem;
import com.daehanins.mes.biz.work.mapper.MatWeighMapper;
import com.daehanins.mes.biz.work.service.IMatUseService;
import com.daehanins.mes.biz.work.service.IMatWeighService;
import com.daehanins.mes.biz.work.service.IWorkOrderItemService;
import com.daehanins.mes.common.utils.RestUtil;
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
 * MatWeigh 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-09
 */
@Service
public class MatWeighServiceImpl extends ServiceImpl<MatWeighMapper, MatWeigh> implements IMatWeighService {

    @Autowired
    private IMatUseService matUseService;

    @Transactional
    public List<MatWeigh> saveItems(List<MatWeigh> matWeighList) throws Exception {

        this.saveOrUpdateBatch(matWeighList);
        String matUseId = matWeighList.get(0).getMatUseId();

        // 칭량내역을 자재소요량(MatUse)에 반영
        return sumWeighItems(matUseId);
    }

    /** matUseId 에 해당하는 matWeigh들을 집계하여 matUse 업데이트 */
    @Transactional
    public List<MatWeigh> sumWeighItems ( String matUseId ) {

        List<MatWeigh> matWeighList = this.list(new QueryWrapper<MatWeigh>().eq("mat_use_id", matUseId));
        MatUse matUse = matUseService.getById(matUseId);

        BigDecimal totalWeighQty = BigDecimal.ZERO;
        String testNoJoin = "";

        for (MatWeigh item : matWeighList) {
            totalWeighQty = totalWeighQty.add(item.getWeighQty());
            testNoJoin += item.getTestNo() + " ";
        }

        //칭량량 합계와 시험번호 반영
        matUse.setWeighQty(totalWeighQty);
        matUse.setTestNoJoin(testNoJoin);

        //칭량량이 지시량과 동일하거나 크면 칭량 완료로 표시
        if (totalWeighQty.compareTo(matUse.getReqQty()) >= 0 ) {
            matUse.setWeighYn("Y");
        }
        //칭량이 완료되었고, 칭량시간이 null 이라면 시간 등록
        if (matUse.getWeighYn().equals("Y") && matUse.getWeighDatetime() == null) {
            matUse.setWeighDatetime(LocalDateTime.now());
        }

        this.matUseService.saveOrUpdate(matUse);
        return matWeighList;
    }

    @Transactional
    public List<MatWeigh> deleteMatWeighOne(String matWeighId) throws Exception {
        MatWeigh matWeigh = this.getOne(new QueryWrapper<MatWeigh>().eq("mat_weigh_id", matWeighId));

        this.removeById(matWeigh.getMatWeighId());

        return sumWeighItems(matWeigh.getMatUseId());
    }
}
