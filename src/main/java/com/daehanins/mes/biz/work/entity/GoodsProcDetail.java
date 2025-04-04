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
 * GoodsProcDetail 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2023-02-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_proc_detail")
public class GoodsProcDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String goodsProcDetailId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    private String goodsProcId;

    private Integer prodOrder;

    private String prodState;

    private String detail;

    private String memo;

}
