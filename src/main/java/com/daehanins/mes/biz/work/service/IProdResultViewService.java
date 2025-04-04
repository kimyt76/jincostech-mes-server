package com.daehanins.mes.biz.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.work.entity.ProdResultView;

/**
 * <p>
 * ProdResultView 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-25
 */
public interface IProdResultViewService extends IService<ProdResultView> {

    ProdResultView getByWorkOrderItemId(String id);
}
