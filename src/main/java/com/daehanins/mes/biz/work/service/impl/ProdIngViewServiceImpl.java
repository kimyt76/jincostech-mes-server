package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.work.entity.MatUseView;
import com.daehanins.mes.biz.work.entity.ProdIngView;
import com.daehanins.mes.biz.work.mapper.ProdIngViewMapper;
import com.daehanins.mes.biz.work.service.IProdIngViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * ProdIngView 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-28
 */
@Service
public class ProdIngViewServiceImpl extends ServiceImpl<ProdIngViewMapper, ProdIngView> implements IProdIngViewService {

    @Autowired
    private IProdIngViewService prodIngViewService;

    public List<ProdIngView> getByWorkOrderItemId (String workOrderItemId) {

        QueryWrapper<ProdIngView> PIVQueryWrapper = new QueryWrapper<>();
        PIVQueryWrapper.eq(StringUtils.camelToUnderline("workOrderItemId"), workOrderItemId);

        return prodIngViewService.list(PIVQueryWrapper);
    };


}
