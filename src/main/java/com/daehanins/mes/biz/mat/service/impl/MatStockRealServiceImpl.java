package com.daehanins.mes.biz.mat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.mat.entity.MatStockReal;
import com.daehanins.mes.biz.mat.entity.MatStockReal;
import com.daehanins.mes.biz.mat.entity.MatStockRealItem;
import com.daehanins.mes.biz.mat.mapper.MatStockRealMapper;
import com.daehanins.mes.biz.mat.service.IMatStockRealItemService;
import com.daehanins.mes.biz.mat.service.IMatStockRealService;
import com.daehanins.mes.biz.mat.vo.MatStockRealSaveWithItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * MatStockReal 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-13
 */
@Service
public class MatStockRealServiceImpl extends ServiceImpl<MatStockRealMapper, MatStockReal> implements IMatStockRealService {

    @Autowired
    IMatStockRealItemService matStockRealItemService;
    
    @Override
    public boolean saveOrUpdate(MatStockReal entity) {
        if (entity.getSerNo() == null) {
            entity.setSerNo(this.baseMapper.getNextSeq(entity.getStockDate()));
        }
        return super.saveOrUpdate(entity);
    }

    @Transactional
    public MatStockRealSaveWithItems saveWithItems(MatStockReal msr, List<MatStockRealItem> msrItems, List<MatStockRealItem> msrDeleteItems) {

        MatStockRealSaveWithItems data = new MatStockRealSaveWithItems();

        if (this.saveOrUpdate(msr)) {
            data.setMatStockReal(msr);
        }

        // item 삭제 처리
        List<String> deleteIds = new ArrayList<>();
        msrDeleteItems.forEach( item -> {
            if (StringUtils.isNotBlank(item.getMatStockRealItemId())) {
                deleteIds.add(item.getMatStockRealItemId());
            }
        });
        this.matStockRealItemService.removeByIds(deleteIds);

        // item 신규,수정
        List<MatStockRealItem> saveItems = new ArrayList<MatStockRealItem>();
        List<MatStockRealItem> updateItems = new ArrayList<MatStockRealItem>();

        msrItems.forEach( item -> {
            if (StringUtils.isNotBlank(item.getMatStockRealId())) {
                updateItems.add(item);
            } else {
                item.setMatStockRealId(msr.getMatStockRealId());
                saveItems.add(item);
            }
        });
        this.matStockRealItemService.saveBatch(saveItems, 1000);
        this.matStockRealItemService.updateBatchById(updateItems, 1000);

        data.setMatStockRealItems(msrItems);

        return data;
    }

    @Transactional
    public String deleteWithItems(List<String> idList) {
        String msg;

        // 실사재고 아이템 삭제
        for (String matStockRealId : idList) {
            matStockRealItemService.remove(new QueryWrapper<MatStockRealItem>().eq("mat_stock_real_id", matStockRealId));
        }
        // 실사재고 삭제
        if (this.removeByIds(idList)) {
            msg = "success";
        } else {
            msg = "fail";
        }
        return msg;
    }
    
}
