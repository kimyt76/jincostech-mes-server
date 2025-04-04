package com.daehanins.mes.biz.mat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.common.code.ConfirmState;
import com.daehanins.mes.biz.mat.entity.MatTran;
import com.daehanins.mes.biz.mat.entity.MatTranItem;
import com.daehanins.mes.biz.mat.mapper.MatTranItemMapper;
import com.daehanins.mes.biz.mat.service.IMatTranItemService;
import com.daehanins.mes.biz.mat.vo.MatTranMobileItem;
import com.daehanins.mes.biz.mobile.vo.MatTranSearchHistory;
import com.daehanins.mes.biz.pub.entity.ItemMasterView;
import com.daehanins.mes.biz.pub.service.IItemMasterViewService;
import com.daehanins.mes.biz.work.entity.WorkOrderItemView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 자재거래품목MatTranItem 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-09
 */
@Service
public class MatTranItemServiceImpl extends ServiceImpl<MatTranItemMapper, MatTranItem> implements IMatTranItemService {

    @Autowired
    private IItemMasterViewService itemMasterViewService;

    public List<MatTranMobileItem> getMobileItemList (String testNo ) {
        return this.baseMapper.getMobileItemList( testNo );
    }

    public List<MatTranSearchHistory> getMatTranSearchHistory (String testNo ) {
        return this.baseMapper.getMatTranSearchHistory( testNo );
    }

    public List<MatTranSearchHistory> getMatTranHistory (String tranCd, String tranDate ) {
        return this.baseMapper.getMatTranHistory( tranCd, tranDate );
    }

    public MatTranItem saveMatTranItemByWorkEnd (MatTran matTran, WorkOrderItemView workOrderItemView) {

        MatTranItem matTranItem = new MatTranItem();
        matTranItem.setMatTranId(matTran.getMatTranId());
        matTranItem.setItemCd(workOrderItemView.getItemCd());
        matTranItem.setTestNo(workOrderItemView.getItemTestNo());
        ItemMasterView itemMaster = itemMasterViewService.getById(workOrderItemView.getItemCd());
        matTranItem.setItemName(itemMaster.getItemName());
        matTranItem.setItemTypeCd(itemMaster.getItemTypeCd());
        matTranItem.setSpec(itemMaster.getSpec());
        matTranItem.setPrice(itemMaster.getOutPrice());
        matTranItem.setLotNo(workOrderItemView.getLotNo());
        matTranItem.setConfirmState(ConfirmState.OK);
        matTranItem.setMemo(workOrderItemView.getMemo());
        matTranItem.setQty(workOrderItemView.getProdQty());

        //TODO 사용기한 업데이트 할것.
        //matTranItem.setExpiryDate(entity.getExpiryDate());

        this.saveOrUpdate(matTranItem);

        return matTranItem;
    }
}
