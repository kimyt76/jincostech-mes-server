package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.common.code.ProcessCd;
import com.daehanins.mes.biz.work.entity.MatUseView;
import com.daehanins.mes.biz.work.entity.WorkOrderItem;
import com.daehanins.mes.biz.work.mapper.MatUseViewMapper;
import com.daehanins.mes.biz.work.service.IMatUseViewService;
import com.daehanins.mes.biz.work.service.IWorkOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * MatUseView 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-10
 */
@Service
public class MatUseViewServiceImpl extends ServiceImpl<MatUseViewMapper, MatUseView> implements IMatUseViewService {

    @Autowired
    private IWorkOrderItemService workOrderItemService;

    public List<MatUseView> getByWorkOrderItemId (String workOrderItemId) {

        WorkOrderItem prodItem = workOrderItemService.getById(workOrderItemId);
        QueryWrapper<WorkOrderItem> woiQueryWrapper = new QueryWrapper<>();
        woiQueryWrapper.eq("work_order_batch_id", prodItem.getWorkOrderBatchId()).eq("process_cd", ProcessCd.칭량);
        WorkOrderItem weighItem = workOrderItemService.getOne(woiQueryWrapper);

        QueryWrapper<MatUseView> queryWrapper = new QueryWrapper<MatUseView>();
        queryWrapper.eq("work_order_item_id", weighItem.getWorkOrderItemId());
        queryWrapper.orderByAsc("prod_state");

        List<MatUseView> resultList = this.list(queryWrapper);

        return resultList;
    }
}
