package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daehanins.mes.biz.common.code.ProcessCd;
import com.daehanins.mes.biz.pub.entity.ItemMaster;
import com.daehanins.mes.biz.pub.service.IItemMasterService;
import com.daehanins.mes.biz.work.entity.ProdRecord;
import com.daehanins.mes.biz.work.entity.WorkOrderItem;
import com.daehanins.mes.biz.work.mapper.ProdRecordMapper;
import com.daehanins.mes.biz.work.service.IProdRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.work.service.IWorkOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * ProdRecord 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2021-06-14
 */
@Service
public class ProdRecordServiceImpl extends ServiceImpl<ProdRecordMapper, ProdRecord> implements IProdRecordService {

    @Autowired
    private IWorkOrderItemService workOrderItemService;

    @Autowired
    private IItemMasterService itemMasterService;


    public BigDecimal getTotalProdQty (String workOrderItemId) {

        BigDecimal result = BigDecimal.ZERO;
        List<ProdRecord> prodRecordList = this.list(
                new QueryWrapper<ProdRecord>().eq("work_order_item_id", workOrderItemId)
        );
        for (ProdRecord prodRecord : prodRecordList) {
            if(prodRecord.getProdQty().compareTo(BigDecimal.ZERO) > 0) {
                result = result.add(prodRecord.getProdQty());
            }
        }
        return result;
    }

    public ProdRecord saveProdRecord (ProdRecord entity) {

        //record 저장
        this.saveOrUpdate(entity);
        String workOrderItemId = entity.getWorkOrderItemId();
        WorkOrderItem workOrderItem = workOrderItemService.getById(workOrderItemId);

        if(!workOrderItem.getProcessCd().equals(ProcessCd.제조)) {
            QueryWrapper<ProdRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("work_order_item_id", workOrderItemId);
            List<ProdRecord> prodRecordList = this.list(queryWrapper);

            BigDecimal matUseQty = BigDecimal.ZERO;
            BigDecimal prodQty = BigDecimal.ZERO;
            BigDecimal yieldRate = BigDecimal.ZERO;

            for ( ProdRecord prodRecord : prodRecordList ) {
                matUseQty = matUseQty.add(prodRecord.getInputQty());
                prodQty = prodQty.add(prodRecord.getProdQty());
            }
            if(matUseQty.compareTo(BigDecimal.ZERO) > 0 && prodQty.compareTo(BigDecimal.ZERO) > 0) {
                ItemMaster itemMaster = itemMasterService.getById(workOrderItem.getItemCd());
                BigDecimal theoryNumber = (itemMaster.getTheoryProdNumber1() != null && !itemMaster.getTheoryProdNumber1().equals(BigDecimal.ZERO))?
                        itemMaster.getTheoryProdNumber1() : BigDecimal.ONE;
                yieldRate = prodQty.divide(matUseQty.multiply(theoryNumber), 6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                workOrderItem.setMatUseQty(matUseQty);
                workOrderItem.setProdQty(prodQty);
                workOrderItem.setYieldRate(yieldRate);
                workOrderItemService.saveOrUpdate(workOrderItem);
            }
        }

        return entity;
    }
}
