package com.daehanins.mes.biz.mat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.mat.entity.MatOrder;
import com.daehanins.mes.biz.mat.entity.MatOrder;
import com.daehanins.mes.biz.mat.entity.MatOrderItem;
import com.daehanins.mes.biz.mat.mapper.MatOrderMapper;
import com.daehanins.mes.biz.mat.service.IMatOrderService;
import com.daehanins.mes.biz.mat.service.IMatOrderItemService;
import com.daehanins.mes.biz.mat.vo.MatOrderSaveWithItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 자재지시(요청)MatOrder 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-22
 */
@Service
public class MatOrderServiceImpl extends ServiceImpl<MatOrderMapper, MatOrder> implements IMatOrderService {

    @Autowired
    private IMatOrderItemService matOrderItemService;

    @Override
    public boolean saveOrUpdate(MatOrder entity) {
        if (entity.getSerNo() == null) {
            entity.setSerNo(this.baseMapper.getNextSeq(entity.getOrderDate(), entity.getTranCd()));
        }
        return super.saveOrUpdate(entity);
    }

    @Transactional
    public MatOrderSaveWithItems saveWithItems(MatOrder po, List<MatOrderItem> poItems, List<MatOrderItem> poDeleteItems) {

        MatOrderSaveWithItems data = new MatOrderSaveWithItems();

        if (this.saveOrUpdate(po)) {
            data.setMatOrder(po);
        }

        // item 삭제 처리
        poDeleteItems.forEach( item -> {
            if (StringUtils.isNotBlank(item.getMatOrderItemId())) {
                this.matOrderItemService.removeById(item.getMatOrderItemId());
            }
        });
        // item 신규,수정 처리
        poItems.forEach( item -> {
            item.setMatOrderId(po.getMatOrderId());
            this.matOrderItemService.saveOrUpdate(item);
            data.getMatOrderItems().add(item);
        });

        return data;
    }

    @Transactional
    public String deleteWithItems(List<String> idList) {
        String msg;

        // 발주서 아이템 삭제
        for (String matOrderId : idList) {
            matOrderItemService.remove(new QueryWrapper<MatOrderItem>().eq("mat_order_id", matOrderId));
        }
        // 발주서 삭제
        if (this.removeByIds(idList)) {
            msg = "success";
        } else {
            msg = "fail";
        }
        return msg;
    }
}
