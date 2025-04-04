package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.PdrLabor;
import com.daehanins.mes.biz.pub.entity.PdrLaborView;
import com.daehanins.mes.biz.pub.mapper.PdrLaborViewMapper;
import com.daehanins.mes.biz.pub.service.IPdrLaborViewService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * PdrLaborView 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2023-01-18
 */
@Service
public class PdrLaborViewServiceImpl extends ServiceImpl<PdrLaborViewMapper, PdrLaborView> implements IPdrLaborViewService {

    public List<PdrLaborView> listByMasterId (String pdrMasterId) {
        QueryWrapper<PdrLaborView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pdr_master_id", pdrMasterId);
        queryWrapper.orderByAsc("display_order");
        return this.list(queryWrapper);
    }

}
