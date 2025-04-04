package com.daehanins.mes.biz.mat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
* <p>
* MatOrderItemView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-06-25
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mat_order_item_view")
public class MatOrderItemView {

    @TableId
    private String matOrderItemId;

    private String matOrderId;
    private String tranCd;
    private String orderNo;

    private String itemTypeCd;

    private String itemCd;
    private String itemName;

    private String unit;
    private String spec;

    private BigDecimal orderQty;
    private BigDecimal price;

    private BigDecimal supplyAmt;
    private BigDecimal vat;

    private String customerCd;

    private String memo;

    private String orderState;

}
