package com.daehanins.mes.biz.qt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.qt.entity.QualityTestMethodView;

import java.util.List;

/**
 * <p>
 * QualityTestMethodView 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-09
 */
public interface IQualityTestMethodViewService extends IService<QualityTestMethodView> {

    List<QualityTestMethodView> getByQualityTestId (String qualityTestId);

}
