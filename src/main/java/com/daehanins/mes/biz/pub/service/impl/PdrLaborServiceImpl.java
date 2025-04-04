package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.PdrLabor;
import com.daehanins.mes.biz.pub.entity.PdrSell;
import com.daehanins.mes.biz.pub.mapper.PdrLaborMapper;
import com.daehanins.mes.biz.pub.service.IPdrLaborService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * PdrLabor 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2022-12-21
 */
@Service
public class PdrLaborServiceImpl extends ServiceImpl<PdrLaborMapper, PdrLabor> implements IPdrLaborService {

    public List<PdrLabor> listByMasterId (String pdrMasterId) {
        QueryWrapper<PdrLabor> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pdr_master_id", pdrMasterId);
        queryWrapper.orderByAsc("display_order");
        return this.list(queryWrapper);
    }

}
