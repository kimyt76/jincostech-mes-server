package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
* <p>
* MatUseEtcView 엔티티
* </p>
*
* @author jeonsj
* @since 2021-05-03
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mat_use_etc_view")
public class MatUseEtcView {

    @TableId
    private String matUseEtcId;

    private String workOrderItemId;

    private Integer serNo;

    private String storageCd;

    private String itemCd;

    private String itemName;

    private String matName;

    private String specInfo;

    private String exAppearance;

    private String packingSpecValue;

    private String packingSpecUnit;

    private String testNoStr;

    private BigDecimal reqQty;

    private BigDecimal useQtySum;

    private BigDecimal badMatQtySum;

    private BigDecimal badWorkQtySum;

    private BigDecimal totalQtySum;

    private String unit;

    private String memo;

}
