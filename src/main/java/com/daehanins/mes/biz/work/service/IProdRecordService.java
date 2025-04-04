package com.daehanins.mes.biz.work.service;

import com.daehanins.mes.biz.work.entity.ProdRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * ProdRecord 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2021-06-14
 */
public interface IProdRecordService extends IService<ProdRecord> {

    BigDecimal getTotalProdQty (String workOrderItemId);

    ProdRecord saveProdRecord (ProdRecord entity);
}
