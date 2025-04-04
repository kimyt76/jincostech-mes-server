package com.daehanins.mes.biz.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daehanins.mes.biz.mat.vo.AdjustStockItem;
import com.daehanins.mes.biz.tag.vo.EquipRunTagValue;
import com.daehanins.mes.biz.tag.vo.EquipRunVo;
import com.daehanins.mes.biz.work.entity.WorkOrderItem;
import com.daehanins.mes.biz.work.vo.ItemQty;
import com.daehanins.mes.biz.work.vo.PeriodVo;
import com.daehanins.mes.biz.work.vo.ReqItemQty;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 작업지시상세 SQL Mapper 인터페이스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-28
 */
@Repository
public interface WorkOrderItemMapper extends BaseMapper<WorkOrderItem> {

    List<ReqItemQty> getItemQtyList(@Param("ps") Map<String, Object> param);

    List<Map<String, Object>> getProdPerformance(PeriodVo periodVo);

    List<Map<String, Object>> getProdPerformanceByCustomer(PeriodVo periodVo);

    List<EquipRunTagValue> getEquipRunValues(EquipRunVo equipRunVo);
}
