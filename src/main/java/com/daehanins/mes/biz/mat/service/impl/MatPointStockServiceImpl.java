package com.daehanins.mes.biz.mat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.mat.entity.MatPointStock;
import com.daehanins.mes.biz.mat.entity.MatPointStockItem;
import com.daehanins.mes.biz.mat.entity.MatPointStock;
import com.daehanins.mes.biz.mat.entity.MatPointStockItem;
import com.daehanins.mes.biz.mat.mapper.MatPointStockMapper;
import com.daehanins.mes.biz.mat.service.IMatPointStockItemService;
import com.daehanins.mes.biz.mat.service.IMatPointStockService;
import com.daehanins.mes.biz.mat.service.IMatStockItemService;
import com.daehanins.mes.biz.mat.vo.MatPointStockLast;
import com.daehanins.mes.biz.mat.vo.MatPointStockRead;
import com.daehanins.mes.biz.mat.vo.MatPointStockSaveWithItems;
import com.daehanins.mes.biz.pub.entity.Storage;
import com.daehanins.mes.biz.pub.service.IStorageService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * MatPointStock 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-08-27
 */
@Service
public class MatPointStockServiceImpl extends ServiceImpl<MatPointStockMapper, MatPointStock> implements IMatPointStockService {

    @Autowired
    IStorageService storageService;

    @Autowired
    IMatPointStockItemService matPointStockItemService;


    public LocalDate getLastStockDate(LocalDate stockDate, String storageCd) {

        return this.baseMapper.getLastStockDate(stockDate, storageCd);
    }

    public MatPointStockLast getLastStockIdAndDate(LocalDate calcDate, String storageCd) {
        return this.baseMapper.getLastStockIdAndDate(calcDate, storageCd);
    }

    public List<MatPointStockItem> getPointStockItemList(LocalDate calcDate, String storageCd) {

        MatPointStockLast stockLast = this.getLastStockIdAndDate(calcDate, storageCd);

        return this.baseMapper.getPointStockItemList(
                stockLast.getMatPointStockId(),
                stockLast.getStockDate(),
                calcDate,
                storageCd
                );
    }


    public Page<MatPointStockRead> getPointStockItemPage(Page<MatPointStockRead> myPage, Map<String, Object> param) {

        LocalDate calcDate = (LocalDate) param.get("calcDate");
        String storageCd = (String) param.get("storageCd");

        MatPointStockLast stockLast = this.getLastStockIdAndDate(calcDate, storageCd);

        param.put("matPointStockId",  stockLast.getMatPointStockId());
        param.put("stockDate",  stockLast.getStockDate());

        return this.baseMapper.getPointStockItemPage(myPage, param);
    }




    public boolean makeMatPointStock(LocalDate calcDate) {

        List<Storage> storages = this.storageService.list(new QueryWrapper<Storage>()
                .eq("use_yn", "Y")
                .orderByAsc("area_cd","storage_cd"));

        // 기존 시점재고작업을 모두 삭제
        List<MatPointStock> matPointStocks = this.list(new QueryWrapper<MatPointStock>()
                .eq("stock_date", calcDate));
        matPointStocks.forEach(item -> {
            this.matPointStockItemService.remove(new QueryWrapper<MatPointStockItem>()
                    .eq("mat_point_stock_id", item.getMatPointStockId())
            );
            this.removeById(item.getMatPointStockId());
        });

        // 장고별로 시점재고 다시 생성처리
        storages.forEach( storage -> {
            // 마스터를 등록 시작
            MatPointStock matPointStock = new MatPointStock();
            matPointStock.setAreaCd(storage.getAreaCd());
            matPointStock.setStorageCd(storage.getStorageCd());
            matPointStock.setStockDate(calcDate);
            matPointStock.setProcYn("N");
            this.save(matPointStock);

            List<MatPointStockItem> matPointStockItemList = this.getPointStockItemList(calcDate, storage.getStorageCd());
            matPointStockItemList.forEach(item -> {
                item.setMatPointStockId(matPointStock.getMatPointStockId());
                item.setStockDate(matPointStock.getStockDate());
                item.setAreaCd(matPointStock.getAreaCd());
            });

            try {
                this.matPointStockItemService.saveBatch(matPointStockItemList, 1000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // 마스터 등록 완료
            matPointStock.setProcYn("Y");
            this.saveOrUpdate(matPointStock);
        });

        return true;
    }

    public boolean renewMatPointStock(String matPointStockId) {

        // 기존 시점재고 내역을 삭제
        this.matPointStockItemService.remove(new QueryWrapper<MatPointStockItem>()
                .eq("mat_point_stock_id", matPointStockId)
        );

        MatPointStock matPointStock = this.getById(matPointStockId);
        matPointStock.setProcYn("N");
        this.saveOrUpdate(matPointStock);

        List<MatPointStockItem> matPointStockItemList = this.getPointStockItemList(matPointStock.getStockDate(), matPointStock.getStorageCd());
        matPointStockItemList.forEach(item -> {
            item.setMatPointStockId(matPointStock.getMatPointStockId());
            item.setStockDate(matPointStock.getStockDate());
            item.setAreaCd(matPointStock.getAreaCd());
        });

        this.matPointStockItemService.saveBatch(matPointStockItemList, 1000);

        // 마스터 등록 완료
        matPointStock.setProcYn("Y");
        this.saveOrUpdate(matPointStock);

        return true;
    }


    @Transactional
    public MatPointStockSaveWithItems saveWithItems(MatPointStock msr, List<MatPointStockItem> msrItems, List<MatPointStockItem> msrDeleteItems) {

        MatPointStockSaveWithItems data = new MatPointStockSaveWithItems();

        if (this.saveOrUpdate(msr)) {
            data.setMatPointStock(msr);
        }

        // item 삭제 처리
        List<String> deleteIds = new ArrayList<>();
        msrDeleteItems.forEach( item -> {
            if (StringUtils.isNotBlank(item.getMatPointStockItemId())) {
                deleteIds.add(item.getMatPointStockItemId());
            }
        });
        this.matPointStockItemService.removeByIds(deleteIds);

        // item 신규,수정
        List<MatPointStockItem> saveItems = new ArrayList<MatPointStockItem>();
        List<MatPointStockItem> updateItems = new ArrayList<MatPointStockItem>();

        msrItems.forEach( item -> {
            if (StringUtils.isNotBlank(item.getMatPointStockId())) {
                updateItems.add(item);
            } else {
                item.setMatPointStockId(msr.getMatPointStockId());
                saveItems.add(item);
            }
        });
        this.matPointStockItemService.saveBatch(saveItems, 1000);
        this.matPointStockItemService.updateBatchById(updateItems, 1000);

        data.setMatPointStockItems(msrItems);

        return data;
    }

    @Transactional
    public String deleteWithItems(List<String> idList) {
        String msg;

        // 시점재고 아이템 삭제
        for (String matPointStockId : idList) {
            matPointStockItemService.remove(new QueryWrapper<MatPointStockItem>().eq("mat_stock_real_id", matPointStockId));
        }
        // 시점재고 삭제
        if (this.removeByIds(idList)) {
            msg = "success";
        } else {
            msg = "fail";
        }
        return msg;
    }
    
}
