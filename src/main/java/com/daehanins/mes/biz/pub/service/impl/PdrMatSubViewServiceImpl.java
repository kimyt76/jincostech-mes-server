package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.PdrMatSubView;
import com.daehanins.mes.biz.pub.entity.PdrWorkerView;
import com.daehanins.mes.biz.pub.mapper.PdrMatSubViewMapper;
import com.daehanins.mes.biz.pub.service.IPdrMatSubViewService;
import com.daehanins.mes.biz.work.service.IMatUseEtcViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * PdrMatSubView 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2023-01-19
 */
@Service
public class PdrMatSubViewServiceImpl extends ServiceImpl<PdrMatSubViewMapper, PdrMatSubView> implements IPdrMatSubViewService {

    @Autowired
    private IMatUseEtcViewService matUseEtcViewService;

    public List<PdrMatSubView> listBySellId (String pdrSellId) {

        QueryWrapper<PdrMatSubView> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pdr_sell_id", pdrSellId);
        queryWrapper.orderByAsc("display_order");
        List<PdrMatSubView> result = this.list(queryWrapper);

        if (result.isEmpty()) {
            result = matUseEtcViewService.getPdrMatSubByBom(pdrSellId);
        }
        return result;
    }

}
