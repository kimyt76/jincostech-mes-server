package com.daehanins.mes.biz.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.work.entity.MatUseEtcResult;

import java.util.List;

/**
 * <p>
 * MatUseEtcResult 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2021-05-03
 */
public interface IMatUseEtcResultService extends IService<MatUseEtcResult> {

    List<MatUseEtcResult> saveItems(List<MatUseEtcResult> matUseEtcResultList) throws Exception;

}
