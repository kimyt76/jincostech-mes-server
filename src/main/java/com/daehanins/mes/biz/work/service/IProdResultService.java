package com.daehanins.mes.biz.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.work.entity.ProdResult;

import java.util.List;

/**
 * <p>
 * 생산실적ProdResult 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-25
 */
public interface IProdResultService extends IService<ProdResult> {

    ProdResult saveData(ProdResult entity);

    String deleteData(List<String> idList);
}
