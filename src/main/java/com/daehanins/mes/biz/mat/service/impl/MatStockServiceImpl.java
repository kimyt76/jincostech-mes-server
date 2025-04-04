package com.daehanins.mes.biz.mat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.common.code.StockState;
import com.daehanins.mes.biz.common.code.TranCd;
import com.daehanins.mes.biz.mat.entity.*;
import com.daehanins.mes.biz.mat.mapper.MatStockMapper;
import com.daehanins.mes.biz.mat.service.*;
import com.daehanins.mes.biz.mat.vo.*;
import com.daehanins.mes.biz.pub.entity.Storage;
import com.daehanins.mes.biz.pub.service.IAreaService;
import com.daehanins.mes.biz.pub.service.IStorageService;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * <p>
 * MatStock 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-13
 */
@Service
public class MatStockServiceImpl extends ServiceImpl<MatStockMapper, MatStock> implements IMatStockService {

    @Autowired
    IMatPointStockService matPointStockService;

    @Autowired
    IMatStockItemService matStockItemService;

    @Autowired
    IMatStockItemViewService matStockItemViewService;

    @Autowired
    IMatStockRealService matStockRealService;

    @Autowired
    IMatStockViewService matStockViewService;

    @Autowired
    IMatStockRealItemService matStockRealItemService;

    @Autowired
    IMatTranService matTranService;

    @Autowired
    IStorageService storageService;

    @Autowired
    IAreaService areaService;

    @Override
    public boolean saveOrUpdate(MatStock entity) {
        Storage storage = this.storageService.getOne(new QueryWrapper<Storage>().eq("storage_cd", entity.getStorageCd()));
        entity.setAreaCd(storage.getAreaCd());
        return super.saveOrUpdate(entity);
    }

    public MatStockReadWithItems getWithItems(String matStockId) {
        MatStockReadWithItems mtItems = new MatStockReadWithItems();
        MatStockView matStock = this.matStockViewService.getById(matStockId);

        QueryWrapper<MatStockItemView> queryWrapper = new QueryWrapper<MatStockItemView>()
                .eq("mat_stock_id", matStockId)
                .orderByAsc("item_cd");
        List<MatStockItemView> matStockItems = this.matStockItemViewService.list(queryWrapper);
        mtItems.setMatStock(matStock);
        mtItems.setMatStockItems(matStockItems);
        return mtItems;
    }

    /** 신규생성 & 장부재고 생성 **/
    @Transactional
    public boolean initMatStockWithItems (MatStock entity) {
        entity.setMatStockId(String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId()));
        return this.saveOrUpdate(entity) && saveMatStockItemList(entity);
    }

    /** 장부재고 생성 **/
    @Transactional
    public boolean saveMatStockItemList (MatStock entity) {
        List<MatStockItem> matStockItems = this.baseMapper.initMatStockItem(entity);
        for (MatStockItem matStockItem : matStockItems) {
            matStockItem.setMatStockItemId(String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId()));
            matStockItem.setMatStockId(entity.getMatStockId());
        }
        return matStockItemService.saveBatch(matStockItems);
    }

    /** 동일날짜, 동일창고 존재여부 체크 **/
    public boolean checkStockExist (MatStock matStock) {
        QueryWrapper<MatStock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stock_date", matStock.getStockDate());
        queryWrapper.eq("storage_cd", matStock.getStorageCd());
        return this.count(queryWrapper) > 0;
    }





    public MatStockReadWithItems saveLedgerStock(String matStockId) {
        MatStock matStock = this.getById(matStockId);
        // 가장 최근의 시점재고 일자 구함
        LocalDate pointStockDate = matPointStockService.getLastStockDate(matStock.getStockDate(), matStock.getStorageCd());
        // 장부재고 조회
        List<LedgerStockItem> ledgerStockItemList = this.baseMapper.getLedgerStockList(matStock.getStorageCd(), pointStockDate, matStock.getStockDate() );

        // 재고조사상세를 먼저 삭제처리
        matStockItemService.remove(new QueryWrapper<MatStockItem>().eq("mat_stock_id", matStockId));

        // 장부재고를 재고조사상세로 등록
        List<MatStockItem> matStockItemList = new ArrayList<>();
        for (LedgerStockItem ledgerStockItem : ledgerStockItemList) {
            MatStockItem matStockItem = new MatStockItem();
            matStockItem.setMatStockId(matStockId);
            matStockItem.setItemCd(ledgerStockItem.getItemCd());
            matStockItem.setLotNo(ledgerStockItem.getLotNo());
            matStockItem.setTestNo(ledgerStockItem.getTestNo());
            // 장부재고, 실사재고 0, 조정수량 0
            matStockItem.setAccountQty(ledgerStockItem.getResultQTy());
            matStockItem.setRealQty(BigDecimal.ZERO);
            matStockItem.setAdjustQty(BigDecimal.ZERO);
            matStockItemList.add(matStockItem);
        }
        matStockItemService.saveBatch(matStockItemList);

        // 진행상태 (장부재고생성) 수정 반영
        matStock.setStockState(StockState.장부);
        this.updateById(matStock);

        // 재고조사 내용을 다시 읽음
        return this.getWithItems(matStockId);
    }

    public MatStockReadWithItems saveStockRealQty(String matStockId) {
        MatStock matStock = this.getById(matStockId);
        // 매칭한 실사재고수량 조회
        List<MatchStockRealItem> matchStockItemList = this.baseMapper.getMatchStockRealItemList(matStock.getMatStockId());

        // 매칭 실사재고수량 재고조사 상세에 반영
        for (MatchStockRealItem matchItem: matchStockItemList) {
            // 매칭된 재고가 있는 경우
            if (!matchItem.getMatStockItemId().isEmpty()) {
                UpdateWrapper<MatStockItem> matStockItemWrapper = new UpdateWrapper<>();
                matStockItemWrapper.set("check_qty", matchItem.getCheckQty());
                matStockItemWrapper.set("real_qty", matchItem.getRealQty());
                matStockItemWrapper.set("memo", matchItem.getMemo());
                matStockItemWrapper.eq("mat_stock_item_id", matchItem.getMatStockItemId());
                this.matStockItemService.update(matStockItemWrapper);

            } else {
                MatStockItem newItem = new MatStockItem();
                newItem.setMatStockId(matStockId);
                newItem.setItemCd(matchItem.getItemCd());
                newItem.setLotNo(matchItem.getLotNo());
                newItem.setTestNo(matchItem.getTestNo());
                newItem.setAccountQty(BigDecimal.ZERO);
                newItem.setCheckQty(BigDecimal.ZERO);
                newItem.setRealQty(matchItem.getRealQty());
                newItem.setAdjustQty(BigDecimal.ZERO);
                newItem.setMemo(matchItem.getMemo());
                this.matStockItemService.save(newItem);
            }
        }
        // 실사재고 처리상태 반영
        UpdateWrapper<MatStockReal> matStockRealWrapper = new UpdateWrapper<>();
        matStockRealWrapper.set("end_yn", "Y");
        matStockRealWrapper.eq("mat_stock_id", matStock.getMatStockId());
        this.matStockRealService.update(matStockRealWrapper);

        // 진행상태 (실사재고반영) 수정 반영
        matStock.setStockState(StockState.실사);
        this.updateById(matStock);

        // 재고조사 내용을 다시 읽음
        return this.getWithItems(matStockId);
    }

    public MatStockReadWithItems saveStockAdjustQty(String matStockId) {
        MatStock matStock = this.getById(matStockId);

        // 기존 전표를 먼저 삭제처리
        String matTranId = matStock.getMatTranId();
        if (!matTranId.isEmpty()) {
            List<String> tranIdList = new ArrayList();
            tranIdList.add(matTranId);
            matTranService.deleteWithItems(tranIdList);
        }

        // 조정 대상 목록 조회
        List<AdjustStockItem> adjustStockItemList = this.baseMapper.getAdjustStockItemList(matStock.getMatStockId());

        // 자재거래에 조정 등록
        MatTran matTran = new MatTran();
        matTran.setTranCd(TranCd.S);  // 실사재고조정
        matTran.setTranDate(matStock.getStockDate());     // <== 조정전표생성일자를 재고조사일자로 처리한다.
//        matTran.setTranDate(LocalDate.now());           // <== 조정전표생성일자를 저장한 당일로 처리한다.
        matTran.setMemo(matStock.getStockDate() + "실사재고조정");
        matTran.setSrcStorageCd(matStock.getStorageCd());
        matTran.setMemberCd(matStock.getMemberCd());

        List<MatTranItem> matTranItems = new ArrayList<>();
        // 자재거래 상세 등록 & 매칭 실사재고수량 재고조사 상세에 반영
        for (AdjustStockItem adjustStockItem: adjustStockItemList) {
            UpdateWrapper<MatStockItem> matStockItemWrapper = new UpdateWrapper<>();
            matStockItemWrapper.set("adjust_qty", adjustStockItem.getAdjustQty());
            matStockItemWrapper.set("memo", adjustStockItem.getMemo());
            matStockItemWrapper.eq("mat_stock_item_id", adjustStockItem.getMatStockItemId());
            this.matStockItemService.update(matStockItemWrapper);

            MatTranItem matTranItem = new MatTranItem();
            matTranItem.setItemCd(adjustStockItem.getItemCd());
            matTranItem.setItemName(adjustStockItem.getItemName());
            matTranItem.setLotNo(adjustStockItem.getLotNo());
            matTranItem.setQty(adjustStockItem.getAdjustQty());
            matTranItem.setTestNo(adjustStockItem.getTestNo());
            matTranItem.setMemo(adjustStockItem.getMemo());
            matTranItems.add(matTranItem);
        }
        // 자재거래에 등록처리
        this.matTranService.saveWithItems(matTran, matTranItems, new ArrayList<>());

        // 진행상태 (조정전표생성) 수정 반영
        matStock.setStockState(StockState.조정);
        matStock.setAdjYn("Y");
        matStock.setMatTranId(matTran.getMatTranId());
        this.updateById(matStock);

        // 재고조사 내용을 다시 읽음
        return this.getWithItems(matStockId);
    }

    public List<MatPointStockRead> getCurrentStockItemList(@Param("ps") Map<String, Object> param) {

        LocalDate calcDate = (LocalDate) param.get("calcDate");
        String areaCd = (String) param.get("areaCd");

        List<MatPointStockRead> resultList = new ArrayList<>();

//        List<Storage> storageList = this.storageService.list(new QueryWrapper<Storage>().eq("area_cd", areaCd));
        // QQQ  조회기준 전반적으로 정리 필요    2020/09/08 jeonsj
        List<String> storageList = new ArrayList<>();
        if (areaCd.equals("A001")) {
            storageList.add("S001");
            storageList.add("T001");
        } else if (areaCd.equals("A002")) {
            storageList.add("S101");
            storageList.add("T101");
        } else if  (areaCd.equals("A009")) {
            storageList.add("S901");
            storageList.add("S902");
        }

        for (String storageCd : storageList) {

            MatPointStockLast stockLast = this.matPointStockService.getLastStockIdAndDate(calcDate, storageCd);

            param.put("matPointStockId",  stockLast.getMatPointStockId());
            param.put("stockDate",  stockLast.getStockDate());
            param.put("storageCd",  storageCd);
            resultList.addAll(this.baseMapper.getCurrentStockItemList(param));
        }

        return resultList;
    }

    public List<MatPointStockRead> getCurrentStockItemList2(@Param("ps") Map<String, Object> param) {

        LocalDate calcDate = (LocalDate) param.get("calcDate");
        String areaCd = (String) param.get("areaCd");
        if (areaCd == null || areaCd.equals("")) {
            areaCd = "A001";
            param.put("areaCd", areaCd);
        }

        // 가장 최근의 시점재고ID 값을 구함
        MatPointStockLast stockLast = this.matPointStockService.getLastStockIdAndDate(calcDate, "WS001");

        param.put("matPointStockId",  stockLast.getMatPointStockId());
        //param.put("storageCdInList",  storageCdInList);
        param.put("stockDate",  stockLast.getStockDate());


        List<MatPointStockRead> result = new ArrayList<>();

        if (param.get("storageList") == null) {
            result = this.baseMapper.getCurrentStockItemList2(param);
        } else {
            result = this.baseMapper.getCurrentStockItemList3(param);
        }

        return result;
    }

    public List<MatSingleItemStock> getCurrentItemStockList(MatStockMoveVo matStockMoveVo) {
        return this.baseMapper.getCurrentItemStockList(matStockMoveVo);
    }

    @Transactional
    public String deleteWithItems(List<String> idList) {
        String msg;

        // 재고조사 아이템 삭제
        for (String matStockId : idList) {
            matStockItemService.remove(new QueryWrapper<MatStockItem>().eq("mat_stock_id", matStockId));

            List<MatStockReal> stockRealList = matStockRealService.list(new QueryWrapper<MatStockReal>().eq("mat_stock_id", matStockId));

            // 실사재고 삭제
            for (MatStockReal stockReal : stockRealList ) {
                matStockRealItemService.remove(new QueryWrapper<MatStockRealItem>().eq("mat_stock_real_id", stockReal.getMatStockRealId()));
                matStockRealService.removeById(stockReal.getMatStockRealId());
            }

            // tran에 조정전표 등록된 경우 삭제처리
            MatStock matStock = this.getById(matStockId);
            if (!matStock.getMatTranId().isEmpty()) {
                List<String> tranIdList = Arrays.asList(matStock.getMatTranId());
                this.matTranService.deleteWithItems(tranIdList);
            }
        }
        // 재고조사 삭제
        if (this.removeByIds(idList)) {
            msg = "success";
        } else {
            msg = "fail";
        }
        return msg;
    }

    /** 재고조회용 창고목록 조회 **/
    public List<StockStorageVo> getTargetStorageList(StockStorageVo stockStorageVo) {
        return this.baseMapper.getTargetStorageList(stockStorageVo);
    }

    /** 일일마감 조회 **/
    public List<WeighClosingItems> getWeighClosingDay(WeighClosingVo weighClosingVo) {
        return this.baseMapper.getWeighClosingDay(weighClosingVo);
    }

    /** 월간마감 조회 **/
    public List<WeighClosingItems> getWeighClosingMonth(WeighClosingVo weighClosingVo) {
        return this.baseMapper.getWeighClosingMonth(weighClosingVo);
    }

    /** 일일(월간)마감 상세 조회 **/
    public List<WeighClosingDetailItem> getWeighClosingDetail(WeighClosingDetailVo weighClosingDetailVo) {
        return this.baseMapper.getWeighClosingDetail(weighClosingDetailVo);
    }

    /** 재고조회 : 시험번호별 **/
    public List<Map<String, Object>> getStockItemListByTestNo(MatStockResultItems matStockResultItems) {
        return this.baseMapper.getStockItemListByTestNo(matStockResultItems);
    }
    
    /** 재고조회 : 품목별 **/
    public List<Map<String, Object>> getStockItemListByItemCd(MatStockResultItems matStockResultItems) {
        return this.baseMapper.getStockItemListByItemCd(matStockResultItems);
    }

    /** 수불부 조회 **/
    public List<MatSupplyRegisterVo> getSupplyRegister(MatSupplyRegisterReqVo matSupplyRegisterReqVo) {
        return this.baseMapper.getSupplyRegister(matSupplyRegisterReqVo);
    }

    public List<Map<String, Object>> getStockItemListByExpiryDate(List<StockStorageVo> storageList, SearchDate searchDate) {
        return this.baseMapper.getStockItemListByExpiryDate(storageList, searchDate);
    }

    public List<Map<String, Object>> getStockItemListByCreateDate(List<StockStorageVo> storageList, SearchDate searchDate) {
        return this.baseMapper.getStockItemListByCreateDate(storageList, searchDate);
    }

}
