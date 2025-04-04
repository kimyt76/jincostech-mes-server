package com.daehanins.mes.biz.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daehanins.mes.biz.work.entity.WorkOrder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * <p>
 * 작업지시 SQL Mapper 인터페이스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-28
 */
@Repository
public interface WorkOrderMapper extends BaseMapper<WorkOrder> {

    // 작업지시일자의 연번 채번
    Integer getNextSeq(LocalDate workDate);

}
