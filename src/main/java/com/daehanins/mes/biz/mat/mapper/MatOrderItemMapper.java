package com.daehanins.mes.biz.mat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daehanins.mes.biz.mat.entity.MatOrderItem;
import com.daehanins.mes.biz.mat.vo.MatOrderItemInfoVo;
import com.daehanins.mes.biz.mat.vo.MatOrderTranItemSum;
import com.daehanins.mes.biz.mobile.vo.MatTranSearchHistory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 자재지시(요청)품목 SQL Mapper 인터페이스
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-22
 */
@Repository
public interface MatOrderItemMapper extends BaseMapper<MatOrderItem> {

    List<MatOrderTranItemSum> getMatOrderTranItemSum(String matOrderId );

    boolean updateOrderItemEndYn(String id, String itemEndYn);
    HashMap<String, Object> getItemEndYnInfo(String id);

    List<MatOrderItemInfoVo> getMatOrderItemList(String id);
}
