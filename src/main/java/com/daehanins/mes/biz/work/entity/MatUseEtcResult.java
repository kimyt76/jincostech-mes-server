package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * MatUseEtcResult 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2021-05-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("mat_use_etc_result")
public class MatUseEtcResult extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String matUseEtcResultId;

    private String matUseEtcId;

    private String storageCd;

    private String testNo;

    private String itemCd;

    private String lotNo;

    private BigDecimal useQty;

    private BigDecimal badMatQty;

    private BigDecimal badWorkQty;

    private String memo;

}
