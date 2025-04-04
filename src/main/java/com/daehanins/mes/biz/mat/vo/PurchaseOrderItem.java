package com.daehanins.mes.biz.mat.vo;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.common.utils.SnowFlakeUtil;

/**
 * <p>
 * 구매발주품목PurchaseOrderItem 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("purchase_order_item")
public class PurchaseOrderItem extends BaseEntity {

    private static final long serialVersionUID = -1555546657333937592L;

    /**
     * 구매발주품목ID
     */
    @TableId
    private String purchaseOrderItemId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 구매발주ID
     */
    private String purchaseOrderId;

    /**
     * 품목코드
     */
    private String itemCd;

    /**
     * 품목코드명
     */
    private String itemName;

    /**
     * 규격
     */
    private String spec;

    /**
     * 발주단위
     */
    private String unit;

    /**
     * 발주수량
     */
    private BigDecimal orderQty;

    /**
     * 단가
     */
    private BigDecimal price;

    /**
     * 공급가액
     */
    private BigDecimal supplyAmt;

    /**
     * 부가세
     */
    private BigDecimal vat;

    /**
     * 적요
     */
    private String memo;

    /**
     * 입고량
     */
    private BigDecimal inQty;

}
