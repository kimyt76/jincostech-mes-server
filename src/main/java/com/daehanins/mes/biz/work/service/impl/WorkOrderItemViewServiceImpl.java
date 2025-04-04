package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.common.code.WorkOrderItemStatus;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;
import com.daehanins.mes.biz.work.mapper.WorkOrderItemViewMapper;
import com.daehanins.mes.biz.work.service.IWorkOrderItemViewService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * WorkOrderItemView 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-28
 */
@Service
public class WorkOrderItemViewServiceImpl extends ServiceImpl<WorkOrderItemViewMapper, WorkOrderItemView> implements IWorkOrderItemViewService {

    public List<WorkOrderItemView> getByWorkOrderId ( String workOrderId ) {

        QueryWrapper<WorkOrderItemView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.camelToUnderline("workOrderId"), workOrderId)
                .orderByAsc(StringUtils.camelToUnderline("batchSerNo"));
        return this.list(queryWrapper);
    }

    public int countUnmodifiableItem ( String workOrderId ) {
        QueryWrapper<WorkOrderItemView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.camelToUnderline("workOrderId"), workOrderId)
                .notIn(StringUtils.camelToUnderline("workOrderItemStatus"), WorkOrderItemStatus.작업지시, WorkOrderItemStatus.작업취소);
        return this.count(queryWrapper);
    }
}
