package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * PdrMatSub 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2022-12-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pdr_mat_sub")
public class PdrMatSub extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String pdrMatSubId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    private String pdrSellId;

    private Integer displayOrder;

    private String itemCd;

    private String itemName;

    private BigDecimal amt;

    private BigDecimal price;

}
