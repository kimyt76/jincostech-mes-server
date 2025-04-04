package com.daehanins.mes.biz.mat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.mat.entity.MatPointStock;
import com.daehanins.mes.biz.mat.entity.MatPointStockItem;
import com.daehanins.mes.biz.mat.vo.MatPointStockLast;
import com.daehanins.mes.biz.mat.vo.MatPointStockRead;
import com.daehanins.mes.biz.mat.vo.MatPointStockSaveWithItems;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * MatPointStock 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-08-27
 */
public interface IMatPointStockService extends IService<MatPointStock> {

    LocalDate getLastStockDate(LocalDate stockDate, String storageCd);

    MatPointStockLast getLastStockIdAndDate(LocalDate stockDate, String storageCd);

    List<MatPointStockItem> getPointStockItemList(LocalDate stockDate, String storageCd);

    Page<MatPointStockRead> getPointStockItemPage(Page<MatPointStockRead> myPage, Map<String, Object> param);

    boolean makeMatPointStock(LocalDate calcDate);

    boolean renewMatPointStock(String matPointStockId);

    MatPointStockSaveWithItems saveWithItems(MatPointStock matPointStock, List<MatPointStockItem> matPointStockItems, List<MatPointStockItem> matPointStockDeleteItems);

    String deleteWithItems(List<String> idList);

}
