package com.daehanins.mes.biz.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.work.entity.GoodsBom;
import com.daehanins.mes.biz.work.vo.GoodsBomExcel;
import com.daehanins.mes.biz.work.vo.GoodsBomReadWithItems;

import java.util.List;

/**
 * <p>
 * 제품BOMGoodsBom 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-23
 */
public interface IGoodsBomService extends IService<GoodsBom> {

    GoodsBomReadWithItems getWithItems (String id);
    GoodsBomReadWithItems uploadItems (List<GoodsBomExcel> goodsBomExcelList);
}
