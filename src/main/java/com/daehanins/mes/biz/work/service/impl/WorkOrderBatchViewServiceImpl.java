package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.work.entity.WorkOrderBatchView;
import com.daehanins.mes.biz.work.mapper.WorkOrderBatchViewMapper;
import com.daehanins.mes.biz.work.service.IWorkOrderBatchViewService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * WorkOrderBatchView 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2021-02-22
 */
@Service
public class WorkOrderBatchViewServiceImpl extends ServiceImpl<WorkOrderBatchViewMapper, WorkOrderBatchView> implements IWorkOrderBatchViewService {

    public List<WorkOrderBatchView> getByWorkOrderId (String workOrderId ) {

        QueryWrapper<WorkOrderBatchView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.camelToUnderline("workOrderId"), workOrderId)
                    .orderByAsc(StringUtils.camelToUnderline("batchSerNo"));
        List<WorkOrderBatchView> resultList = this.list(queryWrapper);

        return resultList;
    }


}
