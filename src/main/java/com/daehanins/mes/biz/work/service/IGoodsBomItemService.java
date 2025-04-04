package com.daehanins.mes.biz.work.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.work.entity.GoodsBomItem;
import com.daehanins.mes.biz.work.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 제품BOM품목GoodsBomItem 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-23
 */
public interface IGoodsBomItemService extends IService<GoodsBomItem> {

    List<GoodsBomItemByCosts> getGoodsBomItemByCostsList (String itemCd);

    List<GoodsBomItemByCosts> getGoodsBomItemByCostsListSub (String itemCd);

    List<UsageByItems> getUsageByItemList (UsageByItemVo usageByItemVo);

    List<ConsumptionItem> getConsumptionList (List<ConsumptionProdItem> consumptionProdItemList);

    List<ConsumptionItem> getConsumptionSubList (List<ConsumptionProdItem> consumptionProdItemList);

    ResponseEntity<Resource> getConsumptionExcel (List<ConsumptionItem> consumptionItemList) throws Exception;

}
