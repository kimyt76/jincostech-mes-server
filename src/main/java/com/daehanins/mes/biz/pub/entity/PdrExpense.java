package com.daehanins.mes.biz.pub.entity;

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
 * PdrExpense 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2022-12-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pdr_expense")
public class PdrExpense extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String pdrExpenseId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    private String pdrMasterId;

    private Integer displayOrder;

    private String gb1;

    private String gb2;

    private BigDecimal directCost;

    private BigDecimal indirectCost;

}
