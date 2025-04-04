package com.daehanins.mes.biz.work.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * CosFormula 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-08-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("cos_formula")
public class CosFormula extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String cosFormulaId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    private String cosFormulaName;

    private Integer useDays;

    private Integer maxDays;

}
