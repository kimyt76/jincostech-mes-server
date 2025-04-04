package com.daehanins.mes.biz.mat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
* <p>
* MatStockRealItemView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-05-14
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mat_stock_real_item_view")
public class MatStockRealItemView {

    @TableId
    private String matStockRealItemId;

    private String matStockRealId;

    private String itemCd;

    private String itemName;

    private String lotNo;

    private String testNo;

    private BigDecimal addQty;

    private BigDecimal realQty;

    private String memo;

    private String matStockItemId;

}
