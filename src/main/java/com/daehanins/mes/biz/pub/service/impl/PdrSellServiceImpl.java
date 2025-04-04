package com.daehanins.mes.biz.pub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.daehanins.mes.biz.pub.entity.PdrMat;
import com.daehanins.mes.biz.pub.entity.PdrMatSub;
import com.daehanins.mes.biz.pub.entity.PdrSell;
import com.daehanins.mes.biz.pub.entity.PdrWorker;
import com.daehanins.mes.biz.pub.mapper.PdrSellMapper;
import com.daehanins.mes.biz.pub.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * PdrSell 서비스 구현 클래스
 * </p>
 *
 * @author jeonsj
 * @since 2022-12-21
 */
@Service
public class PdrSellServiceImpl extends ServiceImpl<PdrSellMapper, PdrSell> implements IPdrSellService {

    @Autowired
    private IPdrMatService pdrMatService;

    @Autowired
    private IPdrMatSubService pdrMatSubService;

    @Autowired
    private IPdrWorkerService pdrWorkerService;

    /** MasterId로 조회 */
    public List<PdrSell> listByMasterId (String pdrMasterId) {
        QueryWrapper<PdrSell> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pdr_master_id", pdrMasterId);
        queryWrapper.orderByAsc("item_cd");
        return this.list(queryWrapper);
    }

    /** 판매금액 및 하위 삭제 **/
    @Transactional
    public Boolean removeBySellId (String id) {
        boolean isDelMat = pdrMatService.remove(new QueryWrapper<PdrMat>().eq("pdr_sell_id", id));
        boolean isDelMatSub = pdrMatSubService.remove(new QueryWrapper<PdrMatSub>().eq("pdr_sell_id", id));
        boolean isDelWorker = pdrWorkerService.remove(new QueryWrapper<PdrWorker>().eq("pdr_sell_id", id));
        boolean isDelSell = this.removeById(id);
        return isDelMat && isDelMatSub && isDelWorker && isDelSell;
    }

    /** MasterId로 판매금액 및 하위 일괄 */
    @Transactional
    public Boolean removeByMasterId (String pdrMasterId) {
        int result = 0;
        List<PdrSell> pdrSellList = this.listByMasterId(pdrMasterId);
        for(PdrSell pdrSell : pdrSellList){
            result += (this.removeBySellId(pdrSell.getPdrSellId()))? 0 : 1;
        }
        return result == 0;
    }

}
