package com.daehanins.mes.biz.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.work.entity.ProdIngView;

import java.util.List;

/**
 * <p>
 * ProdIngView 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-28
 */
public interface IProdIngViewService extends IService<ProdIngView> {

    List<ProdIngView> getByWorkOrderItemId (String workOrderItemId);

}
