package com.daehanins.mes.biz.work.entity;

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
 * 칭량용기WeighBag 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2020-09-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("weigh_bag")
public class WeighBag extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String weighBagId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    /**
     * 칭량용기명
     */
    private String weighBagName;

    /**
     * 무게
     */
    private BigDecimal weight;

}
