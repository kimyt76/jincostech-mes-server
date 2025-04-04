package com.daehanins.mes.biz.pub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.pub.entity.PdrMatView;

import java.util.List;

/**
 * <p>
 * PdrMatView 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2023-01-19
 */
public interface IPdrMatViewService extends IService<PdrMatView> {

    List<PdrMatView> listBySellId (String pdrSellId);

}
