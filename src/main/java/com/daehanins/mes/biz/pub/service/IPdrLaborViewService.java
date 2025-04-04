package com.daehanins.mes.biz.pub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.pub.entity.PdrLabor;
import com.daehanins.mes.biz.pub.entity.PdrLaborView;

import java.util.List;

/**
 * <p>
 * PdrLaborView 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2023-01-18
 */
public interface IPdrLaborViewService extends IService<PdrLaborView> {

    List<PdrLaborView> listByMasterId (String pdrMasterId);

}
