package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.work.entity.WorkOrderView;
import com.daehanins.mes.biz.work.mapper.WorkOrderViewMapper;
import com.daehanins.mes.biz.work.service.IWorkOrderViewService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * WorkOrderView 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-28
 */
@Service
public class WorkOrderViewServiceImpl extends ServiceImpl<WorkOrderViewMapper, WorkOrderView> implements IWorkOrderViewService {

}
