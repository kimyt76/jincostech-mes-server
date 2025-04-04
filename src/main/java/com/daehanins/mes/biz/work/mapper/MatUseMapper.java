package com.daehanins.mes.biz.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daehanins.mes.biz.work.entity.MatUse;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 자재소요량 SQL Mapper 인터페이스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-09
 */
@Repository
public interface MatUseMapper extends BaseMapper<MatUse> {

    void updateProdIngTime(String workOrderItemId);
}
