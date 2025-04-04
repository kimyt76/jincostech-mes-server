package com.daehanins.mes.biz.mat.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.daehanins.mes.biz.mat.entity.MatPointStock;
import com.daehanins.mes.biz.mat.entity.MatPointStockItem;
import com.daehanins.mes.biz.mat.vo.MatPointStockLast;
import com.daehanins.mes.biz.mat.vo.MatPointStockRead;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  SQL Mapper 인터페이스
 * </p>
 *
 * @author jeonsj
 * @since 2020-08-27
 */
@Repository
public interface MatPointStockMapper extends BaseMapper<MatPointStock> {

    LocalDate getLastStockDate(LocalDate stockDate, String storageCd);

    MatPointStockLast getLastStockIdAndDate(LocalDate calcDate, String storageCd);

    List<MatPointStockItem> getPointStockItemList(String matPointStockId, LocalDate stockDate, LocalDate calcDate, String storageCd);

    Page<MatPointStockRead> getPointStockItemPage(@Param("pg") Page<MatPointStockRead> myPage, @Param("ps") Map<String, Object> param);

}
