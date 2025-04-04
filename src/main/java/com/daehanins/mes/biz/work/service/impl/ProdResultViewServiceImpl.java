package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.work.entity.ProdResultView;
import com.daehanins.mes.biz.work.mapper.ProdResultViewMapper;
import com.daehanins.mes.biz.work.service.IProdResultViewService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * ProdResultView 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-25
 */
@Service
public class ProdResultViewServiceImpl extends ServiceImpl<ProdResultViewMapper, ProdResultView> implements IProdResultViewService {

    public ProdResultView getByWorkOrderItemId(String id) {
        return this.baseMapper.selectOne(new QueryWrapper<ProdResultView>().eq("work_order_item_id", id));
    }
}
