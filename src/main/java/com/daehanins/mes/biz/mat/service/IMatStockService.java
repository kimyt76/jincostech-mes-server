package com.daehanins.mes.biz.mat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.mat.entity.MatStock;
import com.daehanins.mes.biz.mat.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * MatStock 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-13
 */
public interface IMatStockService extends IService<MatStock> {

    boolean checkStockExist (MatStock matStock);

    boolean initMatStockWithItems (MatStock entity);

    MatStockReadWithItems getWithItems(String matStockId);

    MatStockReadWithItems saveLedgerStock(String matStockId);

    MatStockReadWithItems saveStockRealQty(String matStockId);

    MatStockReadWithItems saveStockAdjustQty(String matStockId);

    List<MatPointStockRead> getCurrentStockItemList(@Param("ps") Map<String, Object> param);

    List<MatPointStockRead> getCurrentStockItemList2(@Param("ps") Map<String, Object> param);

    List<MatSingleItemStock> getCurrentItemStockList(MatStockMoveVo matStockMoveVo);

//    List<MatStockStorageRead> getCurrentStockStorageList(@Param("ps") Map<String, Object> param);

    String deleteWithItems(List<String> idList);

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
