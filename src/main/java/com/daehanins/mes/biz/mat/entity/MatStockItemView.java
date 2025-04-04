package com.daehanins.mes.biz.mat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
* <p>
* MatStockItemView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-05-14
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mat_stock_item_view")
public class MatStockItemView {
    @TableId
    private String matStockItemId;

    private String matStockId;

    private String itemCd;

    private String itemName;

    private String lotNo;

    private String testNo;

    private BigDecimal accountQty;

    private BigDecimal checkQty;

    private BigDecimal realQty;

    private BigDecimal diffQty;

    private BigDecimal adjustQty;

    private String memo;

}
