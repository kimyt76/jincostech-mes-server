package com.daehanins.mes.biz.pub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.pub.entity.ProcTestWorker;

import java.util.List;

/**
 * <p>
 * ProcTestWorker 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2023-03-14
 */
public interface IProcTestWorkerService extends IService<ProcTestWorker> {

    List<ProcTestWorker> getByMasterId (String id);

}
