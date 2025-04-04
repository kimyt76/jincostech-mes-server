package com.daehanins.mes.biz.mat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * MatStockItem 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mat_stock_item")
public class MatStockItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 재고조사상세id
     */
    @TableId
    private String matStockItemId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 재고조사id
     */
    private String matStockId;

    /**
     * 품목코드
     */
    private String itemCd;

    /**
     * 로트(제조)번호
     */
    private String lotNo;

    /**
     * 시험번호
     */
    private String testNo;

    /**
     * 장부수량
     */
    private BigDecimal accountQty;

    /**
     * 첵크수량
     */
    private BigDecimal checkQty;

    /**
     * 실사수량
     */
    private BigDecimal realQty;


    /**
     * 조정수량
     */
    private BigDecimal adjustQty;

    /**
     * 적요
     */
    private String memo;
}
