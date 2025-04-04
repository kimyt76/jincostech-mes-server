package com.daehanins.mes.biz.mat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.mat.entity.MatOrderItem;
import com.daehanins.mes.biz.mat.mapper.MatOrderItemMapper;
import com.daehanins.mes.biz.mat.service.IMatOrderItemService;
import com.daehanins.mes.biz.mat.vo.MatOrderItemInfoVo;
import com.daehanins.mes.biz.mat.vo.MatOrderTranItemSum;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 자재지시(요청)품목MatOrderItem 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-22
 */
@Service
public class MatOrderItemServiceImpl extends ServiceImpl<MatOrderItemMapper, MatOrderItem> implements IMatOrderItemService {

    public List<MatOrderTranItemSum> getMatOrderTranItemSum(String matOrderId ) {
        return this.baseMapper.getMatOrderTranItemSum(matOrderId);
    }

    public HashMap<String, Object> getItemEndYnInfo(String id){
        return this.baseMapper.getItemEndYnInfo(id);
    }

    public boolean updateOrderItemEndYn(String id, String itemEndYn)  {
        return baseMapper.updateOrderItemEndYn(id, itemEndYn);
    }

    public List<MatOrderItemInfoVo> getMatOrderItemList(String id){ return this.baseMapper.getMatOrderItemList(id); }
}
