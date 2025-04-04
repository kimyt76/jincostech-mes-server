package com.daehanins.mes.biz.qt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.qt.entity.QualityTestMethodView;
import com.daehanins.mes.biz.qt.mapper.QualityTestMethodViewMapper;
import com.daehanins.mes.biz.qt.service.IQualityTestMethodViewService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * QualityTestMethodView 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-09
 */
@Service
public class QualityTestMethodViewServiceImpl extends ServiceImpl<QualityTestMethodViewMapper, QualityTestMethodView> implements IQualityTestMethodViewService {

    public List<QualityTestMethodView> getByQualityTestId (String qualityTestId) {
        QueryWrapper<QualityTestMethodView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("quality_test_id", qualityTestId).orderByAsc("display_order");
        return this.list(queryWrapper);
    }

}
