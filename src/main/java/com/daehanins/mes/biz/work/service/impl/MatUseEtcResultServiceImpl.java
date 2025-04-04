package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.work.entity.MatUseEtcResult;
import com.daehanins.mes.biz.work.mapper.MatUseEtcResultMapper;
import com.daehanins.mes.biz.work.service.IMatUseEtcResultService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * MatUseEtcResult 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2021-05-03
 */
@Service
public class MatUseEtcResultServiceImpl extends ServiceImpl<MatUseEtcResultMapper, MatUseEtcResult> implements IMatUseEtcResultService {

    @Transactional
    public List<MatUseEtcResult> saveItems( List<MatUseEtcResult> matUseEtcResultList ) throws Exception {
        this.saveOrUpdateBatch(matUseEtcResultList);
        return matUseEtcResultList;
    }
}
