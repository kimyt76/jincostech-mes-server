package com.daehanins.mes.biz.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daehanins.mes.biz.mat.vo.MatPointStockRead;
import com.daehanins.mes.biz.work.entity.GoodsBomItem;
import com.daehanins.mes.biz.work.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 제품BOM품목 SQL Mapper 인터페이스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-23
 */
@Repository
public interface GoodsBomItemMapper extends BaseMapper<GoodsBomItem> {

    List<GoodsBomItemByCosts> getGoodsBomItemByCostsList (String itemCd);

    List<GoodsBomItemByCosts> getGoodsBomItemByCostsListSub (String itemCd);

    List<UsageByItems> getUsageByItemList (UsageByItemVo usageByItemVo);

    List<ConsumptionItem> getConsumptionList (List<ConsumptionProdItem> consumptionProdItemList);

    List<ConsumptionItem> getConsumptionSubList (List<ConsumptionProdItem> consumptionProdItemList);

}
