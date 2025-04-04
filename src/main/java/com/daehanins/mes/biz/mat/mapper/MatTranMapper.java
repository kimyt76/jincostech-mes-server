package com.daehanins.mes.biz.mat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daehanins.mes.biz.mat.entity.MatTran;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * <p>
 * 자재거래 SQL Mapper 인터페이스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-09
 */
@Repository
public interface MatTranMapper extends BaseMapper<MatTran> {

    // 거래유형별로 거래일자 연번 채번
    Integer getNextSeq(String tranCd, LocalDate tranDate);
}
