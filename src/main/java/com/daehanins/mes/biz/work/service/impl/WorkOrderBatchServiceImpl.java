package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.work.entity.WorkOrderBatch;
import com.daehanins.mes.biz.work.mapper.WorkOrderBatchMapper;
import com.daehanins.mes.biz.work.service.IWorkOrderBatchService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * WorkOrderBatch 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2021-02-22
 */
@Service
public class WorkOrderBatchServiceImpl extends ServiceImpl<WorkOrderBatchMapper, WorkOrderBatch> implements IWorkOrderBatchService {

    public List<WorkOrderBatch> getByOrderId (String workOrderId ) {

        QueryWrapper<WorkOrderBatch> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.camelToUnderline("workOrderId"), workOrderId)
                    .orderByAsc(StringUtils.camelToUnderline("batchSerNo"));
        List<WorkOrderBatch> resultList = this.list(queryWrapper);

        return resultList;

    }
}
