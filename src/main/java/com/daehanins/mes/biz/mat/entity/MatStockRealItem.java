package com.daehanins.mes.biz.mat.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * MatStockRealItem 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mat_stock_real_item")
public class MatStockRealItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 실사재고상세id
     */
    @TableId
    private String matStockRealItemId  = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 실사재고id
     */
    private String matStockRealId;

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
     * 실사수량
     */
    private BigDecimal realQty;

    /**
     * 추가수량
     */
    private BigDecimal addQty;

    /**
     * 적요
     */
    private String memo;

    /**
     * 재고조사상세id
     */
    private String matStockItemId;

    @TableField(exist = false)
    private String itemName;

    @TableField(exist = false)
    private String spec;

}
