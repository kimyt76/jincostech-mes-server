package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * PdrSell 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2022-12-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("pdr_sell")
public class PdrSell extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String pdrSellId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    private String pdrMasterId;

    private String itemCd;

    private String customerCd;

    private String salesMemberCd;

    private BigDecimal orderQty;

    private BigDecimal prodQty;

    private BigDecimal sellPrice;

    private String itemGb1;

    private String itemGb2;

}
