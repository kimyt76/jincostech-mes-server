package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.ProcTestWorker;
import com.daehanins.mes.biz.pub.mapper.ProcTestWorkerMapper;
import com.daehanins.mes.biz.pub.service.IProcTestWorkerService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * ProcTestWorker 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2023-03-14
 */
@Service
public class ProcTestWorkerServiceImpl extends ServiceImpl<ProcTestWorkerMapper, ProcTestWorker> implements IProcTestWorkerService {

    public List<ProcTestWorker> getByMasterId (String id) {
        return this.list(new QueryWrapper<ProcTestWorker>().eq("proc_test_master_id", id).orderByAsc("display_order"));
    }

}
