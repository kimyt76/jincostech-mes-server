package com.daehanins.mes.biz.mat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.daehanins.mes.biz.mat.entity.MatTranItem;
import com.daehanins.mes.biz.mat.vo.MatTranMobileItem;
import com.daehanins.mes.biz.mobile.vo.MatTranSearchHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 자재거래품목 SQL Mapper 인터페이스
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-09
 */
@Repository
public interface MatTranItemMapper extends BaseMapper<MatTranItem> {

    List<MatTranMobileItem> getMobileItemList ( String testNo );

    List<MatTranSearchHistory> getMatTranSearchHistory( String testNo );

    List<MatTranSearchHistory> getMatTranHistory( String tranCd, String tranDate );

}
