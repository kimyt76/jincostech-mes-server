package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.PdrSell;
import com.daehanins.mes.biz.pub.entity.PdrSellView;
import com.daehanins.mes.biz.pub.mapper.PdrSellViewMapper;
import com.daehanins.mes.biz.pub.service.IPdrSellViewService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * PdrSellView 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2023-01-18
 */
@Service
public class PdrSellViewServiceImpl extends ServiceImpl<PdrSellViewMapper, PdrSellView> implements IPdrSellViewService {

    public List<PdrSellView> listByMasterId (String pdrMasterId) {
        QueryWrapper<PdrSellView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pdr_master_id", pdrMasterId);
        queryWrapper.orderByAsc("item_cd");
        return this.list(queryWrapper);
    }

}
