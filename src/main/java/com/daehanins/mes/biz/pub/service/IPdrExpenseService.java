package com.daehanins.mes.biz.pub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.pub.entity.PdrExpense;
import com.daehanins.mes.biz.pub.entity.PdrLabor;

import java.util.List;

/**
 * <p>
 * PdrExpense 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2022-12-21
 */
public interface IPdrExpenseService extends IService<PdrExpense> {

    List<PdrExpense> listByMasterId (String pdrMasterId);

}
