package com.daehanins.mes.biz.mat.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 자재지시(요청)품목MatOrderItem 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-06-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mat_order_item")
public class MatOrderItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 자재지시(요청)품목id
     */
    @TableId
    private String matOrderItemId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 자재지시(요청)id
     */
    private String matOrderId;

    /**
     * 품목코드
     */
    private String itemCd;

    /**
     * 품목명
     */
    private String itemName;

    /**
     * 규격
     */
    private String spec;

    /**
     * 단위
     */
    private String unit;

    /**
     * 지시수량
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


}
