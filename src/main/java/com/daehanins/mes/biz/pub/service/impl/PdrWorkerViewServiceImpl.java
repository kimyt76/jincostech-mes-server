package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.PdrWorkerView;
import com.daehanins.mes.biz.pub.mapper.PdrWorkerViewMapper;
import com.daehanins.mes.biz.pub.service.IPdrWorkerViewService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * PdrWorkerView 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2023-01-19
 */
@Service
public class PdrWorkerViewServiceImpl extends ServiceImpl<PdrWorkerViewMapper, PdrWorkerView> implements IPdrWorkerViewService {

    public List<PdrWorkerView> listBySellId (String pdrSellId) {
        QueryWrapper<PdrWorkerView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pdr_sell_id", pdrSellId);
        queryWrapper.orderByAsc("display_order");
        return this.list(queryWrapper);
    }

}
