package com.daehanins.mes.biz.pub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.pub.entity.PdrSell;
import com.daehanins.mes.biz.pub.entity.PdrSellView;

import java.util.List;

/**
 * <p>
 * PdrSellView 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2023-01-18
 */
public interface IPdrSellViewService extends IService<PdrSellView> {

    List<PdrSellView> listByMasterId (String pdrMasterId);

}
