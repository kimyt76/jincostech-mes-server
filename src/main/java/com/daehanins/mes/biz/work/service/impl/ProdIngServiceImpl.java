package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.common.code.ProcessCd;
import com.daehanins.mes.biz.work.entity.MatUse;
import com.daehanins.mes.biz.work.entity.ProdIng;
import com.daehanins.mes.biz.work.entity.WorkOrder;
import com.daehanins.mes.biz.work.entity.WorkOrderItem;
import com.daehanins.mes.biz.work.mapper.ProdIngMapper;
import com.daehanins.mes.biz.work.service.IMatUseService;
import com.daehanins.mes.biz.work.service.IProdIngService;
import com.daehanins.mes.biz.work.service.IWorkOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 제조진행ProdIng 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-22
 */
@Service
public class ProdIngServiceImpl extends ServiceImpl<ProdIngMapper, ProdIng> implements IProdIngService {

    @Autowired
    private IMatUseService matUseService;

    @Autowired
    private IWorkOrderItemService workOrderItemService;



    /** 제조진행 생성 **/
    @Transactional
    public void saveByWorkOrderItemId (String workOrderItemId, String workOrderBatchId) {

        //칭량작업지시의 id 가져오기.
        QueryWrapper<WorkOrderItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_order_batch_id", workOrderBatchId)
                    .eq("process_cd", ProcessCd.칭량);
        WorkOrderItem weighItem = workOrderItemService.getOne(queryWrapper);

        //칭량작업지시의 상구분 생성.
        QueryWrapper<MatUse> wrapper = new QueryWrapper<>();
        wrapper.select("prod_state")
               .eq("work_order_item_id", weighItem.getWorkOrderItemId())
               .groupBy("prod_state");
        List<Map<String, Object>> mapList = matUseService.listMaps(wrapper);

        for (Map<String, Object> mp : mapList) {
            ProdIng prodIng = new ProdIng();
            prodIng.setWorkOrderItemId(workOrderItemId);
            prodIng.setProdState(mp.get("prod_state").toString());
            prodIng.setTemperature(0.0);
            prodIng.setRpm1(0.0);
            prodIng.setRpm2(0.0);
            prodIng.setRpm3(0.0);
            this.save(prodIng);
        }
    }

    /** 제조진행 삭제 **/
    @Transactional
    public void deleteByWorkOrderItemId (String workOrderItemId) {

        QueryWrapper<ProdIng> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_order_item_id", workOrderItemId);
        this.remove(queryWrapper);
    }
}
