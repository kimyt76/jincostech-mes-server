package com.daehanins.mes.biz.pub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.pub.entity.ProcTestMethod;

import java.util.List;

/**
 * <p>
 * ProcTestMethod 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2023-03-14
 */
public interface IProcTestMethodService extends IService<ProcTestMethod> {

    List<ProcTestMethod> initProcTestMethod (String testType, String procTestMasterId);

    List<ProcTestMethod> getByMasterId (String id);

}
