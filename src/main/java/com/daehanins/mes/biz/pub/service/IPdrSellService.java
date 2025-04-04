package com.daehanins.mes.biz.pub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.daehanins.mes.biz.pub.entity.PdrSell;

import java.util.List;

/**
 * <p>
 * PdrSell 서비스
 * </p>
 *
 * @author jeonsj
 * @since 2022-12-21
 */
public interface IPdrSellService extends IService<PdrSell> {

    /** MasterId로 조회 */
    List<PdrSell> listByMasterId (String pdrMasterId);

    /** 판매금액 및 하위 삭제 **/
    Boolean removeBySellId (String id);
    
    /** MasterId로 판매금액 및 하위 일괄삭제 */
    Boolean removeByMasterId (String pdrMasterId);

}
