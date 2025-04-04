package com.daehanins.mes.biz.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.pub.entity.PdrMatSubView;
import com.daehanins.mes.biz.pub.entity.PdrSellView;
import com.daehanins.mes.biz.work.entity.MatUseEtcView;
import com.daehanins.mes.biz.work.vo.MatSubItem;

import java.util.List;

/**
 * <p>
 * MatUseEtcView 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2021-05-03
 */
public interface IMatUseEtcViewService extends IService<MatUseEtcView> {

    List<MatSubItem> getMatSubList (String workOrderItemId);

    List<PdrMatSubView> getPdrMatSubByBom (String pdrSellId);

}
