package com.daehanins.mes.biz.mat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
* <p>
* MatOrderItemSum 엔티티
* </p>
*
* @author jeonsj
* @since 2020-06-25
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mat_order_item_sum")
public class MatOrderItemSum {

    @TableId
    private String matOrderId;

    private String itemName;
    private String itemNameJoin;
    private String itemCdJoin;

    private BigDecimal sumOrderQty;
    private BigDecimal sumSupplyAmt;
    private BigDecimal sumVat;

    private Integer cnt;

}
