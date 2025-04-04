package com.daehanins.mes.biz.tag.entity;

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
 * UserDateLog 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2022-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("user_date_log")
public class UserDateLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String userDateLogId;

    private BigDecimal loginTotal;

    private BigDecimal loginAvg;

    private BigDecimal timeTotal;

    private BigDecimal timeAvg;

}
