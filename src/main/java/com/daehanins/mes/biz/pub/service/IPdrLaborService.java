package com.daehanins.mes.biz.pub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.pub.entity.PdrLabor;
import com.daehanins.mes.biz.pub.entity.PdrSell;

import java.util.List;

/**
 * <p>
 * PdrLabor 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2022-12-21
 */
public interface IPdrLaborService extends IService<PdrLabor> {

    List<PdrLabor> listByMasterId (String pdrMasterId);

}
