package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
* <p>
* MatUseEtcResultView 엔티티
* </p>
*
* @author jeonsj
* @since 2021-05-03
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mat_use_etc_result_view")
public class MatUseEtcResultView {

    @TableId
    private String matUseEtcResultId;

    private String matUseEtcId;

    private String workOrderItemId;

    private Integer serNo;

    private String testNo;

    private String lotNo;

    private String itemTypeCd;

    private String itemCd;

    private String itemName;

    private BigDecimal useQty;

    private BigDecimal badMatQty;

    private BigDecimal badWorkQty;

    private String memo;

}
