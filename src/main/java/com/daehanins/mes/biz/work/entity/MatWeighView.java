package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
* <p>
* MatWeighView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-08-15
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mat_weigh_view")
public class MatWeighView {

    @TableId
    private String matWeighId;

    private String matUseId;

    private String workOrderItemId;

    private String itemCd;

    private String itemName;

    private String lotNo;
    private String testNo;

    private String prodState;
    private Integer serNo;

    private BigDecimal weighQty;

}
