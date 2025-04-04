package com.daehanins.mes.biz.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daehanins.mes.biz.pub.entity.PdrMatSubView;
import com.daehanins.mes.biz.pub.entity.PdrSellView;
import com.daehanins.mes.biz.work.entity.MatUseEtcView;
import com.daehanins.mes.biz.work.vo.MatSubItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  SQL Mapper 인터페이스
 * </p>
 *
 * @author jeonsj
 * @since 2021-05-03
 */
@Repository
public interface MatUseEtcViewMapper extends BaseMapper<MatUseEtcView> {

    List<MatSubItem> getMatSubList (String workOrderItemId);

    List<PdrMatSubView> getPdrMatSubByBom (String pdrSellId);

}
