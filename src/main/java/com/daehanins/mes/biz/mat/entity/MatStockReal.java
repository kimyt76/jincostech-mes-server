package com.daehanins.mes.biz.mat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * <p>
 * MatStockReal 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mat_stock_real")
public class MatStockReal extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 실사재고id
     */
    @TableId
    private String matStockRealId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 재고조사id
     */
    private String matStockId;

    /**
     * 재고일자
     */
    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate stockDate;

    /**
     * 일련번호
     */
    private Integer serNo;

    /**
     * 담당자
     */
    private String memberCd;

    /**
     * 반영여부
     */
    private String endYn;

}
