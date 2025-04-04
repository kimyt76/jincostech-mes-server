package com.daehanins.mes.biz.pub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.pub.entity.PdrWorkerView;

import java.util.List;

/**
 * <p>
 * PdrWorkerView 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2023-01-19
 */
public interface IPdrWorkerViewService extends IService<PdrWorkerView> {

    List<PdrWorkerView> listBySellId (String pdrSellId);

}
