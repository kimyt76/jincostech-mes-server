package com.daehanins.mes.biz.qt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daehanins.mes.biz.qt.entity.ItemTestNo;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

/**
 * <p>
 * 품목시험번호 SQL Mapper 인터페이스
 * </p>
 *
 * @author jeonsj
 * @since 2020-07-03
 */
@Repository
public interface ItemTestNoMapper extends BaseMapper<ItemTestNo> {

    // 일자,구역,품목구분별로 일련번호 채번
    Integer getNextSeq(LocalDate createDate, String areaGb, String itemGb);
}
