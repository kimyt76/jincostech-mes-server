package com.daehanins.mes.biz.work.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.PdrMatSubView;
import com.daehanins.mes.biz.pub.entity.PdrSellView;
import com.daehanins.mes.biz.work.entity.MatUseEtcView;
import com.daehanins.mes.biz.work.mapper.MatUseEtcViewMapper;
import com.daehanins.mes.biz.work.service.IMatUseEtcViewService;
import com.daehanins.mes.biz.work.vo.MatSubItem;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * MatUseEtcView 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2021-05-03
 */
@Service
public class MatUseEtcViewServiceImpl extends ServiceImpl<MatUseEtcViewMapper, MatUseEtcView> implements IMatUseEtcViewService {

    public List<MatSubItem> getMatSubList (String workOrderItemId) {
        return this.baseMapper.getMatSubList(workOrderItemId);
    }

    public List<PdrMatSubView> getPdrMatSubByBom (String pdrSellId) {
        return this.baseMapper.getPdrMatSubByBom(pdrSellId);
    }

}
