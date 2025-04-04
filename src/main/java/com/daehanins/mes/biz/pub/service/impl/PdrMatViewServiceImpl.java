package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.PdrMatSubView;
import com.daehanins.mes.biz.pub.entity.PdrMatView;
import com.daehanins.mes.biz.pub.mapper.PdrMatViewMapper;
import com.daehanins.mes.biz.pub.service.IPdrMatViewService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * PdrMatView 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2023-01-19
 */
@Service
public class PdrMatViewServiceImpl extends ServiceImpl<PdrMatViewMapper, PdrMatView> implements IPdrMatViewService {

    public List<PdrMatView> listBySellId (String pdrSellId) {
        QueryWrapper<PdrMatView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pdr_sell_id", pdrSellId);
        queryWrapper.orderByAsc("display_order");
        return this.list(queryWrapper);
    }

}
