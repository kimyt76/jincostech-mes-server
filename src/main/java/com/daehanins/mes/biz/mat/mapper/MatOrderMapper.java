package com.daehanins.mes.biz.mat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daehanins.mes.biz.mat.entity.MatOrder;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * <p>
 * 자재지시(요청) SQL Mapper 인터페이스
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-22
 */
@Repository
public interface MatOrderMapper extends BaseMapper<MatOrder> {

    // 자재요청(지시)의 Tran별 연번 채번
    Integer getNextSeq(LocalDate orderDate, String tranCd);
}
