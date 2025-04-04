package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
* <p>
* PdrSellView 엔티티
* </p>
*
* @author jeonsj
* @since 2023-01-18
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pdr_sell_view")
public class PdrSellView {

    @TableId
    private String pdrSellId;

    private String pdrMasterId;

    private String itemCd;

    private String itemName;

    private String customerCd;

    private String customerName;

    private String salesMemberCd;

    private BigDecimal orderQty;

    private BigDecimal prodQty;

    private BigDecimal sellPrice;

    private BigDecimal prodPrice;

    private BigDecimal sellAmt;

    private String itemGb1;

    private String itemGb2;

    private BigDecimal m1Cost;

    private BigDecimal m2Cost;

    private BigDecimal directCost;

}
