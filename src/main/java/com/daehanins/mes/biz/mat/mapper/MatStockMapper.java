package com.daehanins.mes.biz.mat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daehanins.mes.biz.mat.entity.MatStock;
import com.daehanins.mes.biz.mat.entity.MatStockItem;
import com.daehanins.mes.biz.mat.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  SQL Mapper 인터페이스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-13
 */
@Repository
public interface MatStockMapper extends BaseMapper<MatStock> {

    List<LedgerStockItem> getLedgerStockList(String storageCd, LocalDate pointStockDate, LocalDate stdStockDate);

    List<MatStockItem> initMatStockItem (MatStock matStock);

    List<MatchStockRealItem> getMatchStockRealItemList(String matStockId);

    List<AdjustStockItem> getAdjustStockItemList(String matStockId);

    List<MatPointStockRead> getCurrentStockItemList(@Param("ps") Map<String, Object> param);

    List<MatPointStockRead> getCurrentStockItemList2(@Param("ps") Map<String, Object> param);

    List<MatSingleItemStock> getCurrentItemStockList(MatStockMoveVo matStockMoveVo);

    List<MatPointStockRead> getCurrentStockItemList3(@Param("ps") Map<String, Object> param);

    List<StockStorageVo> getTargetStorageList(StockStorageVo stockStorageVo);

    List<WeighClosingItems> getWeighClosingDay(WeighClosingVo weighClosingVo);

    List<WeighClosingItems> getWeighClosingMonth(WeighClosingVo weighClosingVo);

    List<WeighClosingDetailItem> getWeighClosingDetail(WeighClosingDetailVo weighClosingDetailVo);

    List<MatSupplyRegisterVo> getSupplyRegister(MatSupplyRegisterReqVo matSupplyRegisterReqVo);

    List<Map<String, Object>> getStockItemListByTestNo(MatStockResultItems matStockResultItems);

    List<Map<String, Object>> getStockItemListByItemCd(MatStockResultItems matStockResultItems);

    List<Map<String, Object>> getStockItemListByExpiryDate(List<StockStorageVo> storageList, SearchDate searchDate);

    List<Map<String, Object>> getStockItemListByCreateDate(List<StockStorageVo> storageList, SearchDate searchDate);

}
