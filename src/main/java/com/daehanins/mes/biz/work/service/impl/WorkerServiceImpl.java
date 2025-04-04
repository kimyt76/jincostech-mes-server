package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.work.entity.Worker;
import com.daehanins.mes.biz.work.mapper.WorkerMapper;
import com.daehanins.mes.biz.work.service.IWorkerService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Worker 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-09-09
 */
@Service
public class WorkerServiceImpl extends ServiceImpl<WorkerMapper, Worker> implements IWorkerService {

}
