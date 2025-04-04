package com.daehanins.mes.biz.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.work.entity.MatUseView;

import java.util.List;

/**
 * <p>
 * MatUseView 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-10
 */
public interface IMatUseViewService extends IService<MatUseView> {

    List<MatUseView> getByWorkOrderItemId (String workOrderItemId);

}
