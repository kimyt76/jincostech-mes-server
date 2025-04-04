package com.daehanins.mes.biz.mat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daehanins.mes.biz.mat.entity.MatStockReal;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * <p>
 *  SQL Mapper 인터페이스
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-13
 */
@Repository
public interface MatStockRealMapper extends BaseMapper<MatStockReal> {

    // 발주일자의 연번 채번
    Integer getNextSeq(LocalDate stockDate);

}
