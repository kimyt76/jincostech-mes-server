package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.PdrExpense;
import com.daehanins.mes.biz.pub.entity.PdrLabor;
import com.daehanins.mes.biz.pub.mapper.PdrExpenseMapper;
import com.daehanins.mes.biz.pub.service.IPdrExpenseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * PdrExpense 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2022-12-21
 */
@Service
public class PdrExpenseServiceImpl extends ServiceImpl<PdrExpenseMapper, PdrExpense> implements IPdrExpenseService {

    public List<PdrExpense> listByMasterId (String pdrMasterId) {
        QueryWrapper<PdrExpense> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pdr_master_id", pdrMasterId);
        queryWrapper.orderByAsc("display_order");
        return this.list(queryWrapper);
    }

}
