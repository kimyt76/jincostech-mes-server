package com.daehanins.mes.biz.pub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.pub.entity.PdrMatSubView;

import java.util.List;

/**
 * <p>
 * PdrMatSubView 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2023-01-19
 */
public interface IPdrMatSubViewService extends IService<PdrMatSubView> {

    List<PdrMatSubView> listBySellId (String pdrSellId);

}
