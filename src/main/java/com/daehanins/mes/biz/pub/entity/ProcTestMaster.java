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
 * ProcTestMaster 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("proc_test_master")
public class ProcTestMaster extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String procTestMasterId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    private String workOrderItemId;

    private String areaCd;

    private String storageCd;

    private String chargeMember;

    private String note;

    private BigDecimal temperature1;

    private BigDecimal temperature2;

    private BigDecimal humidity1;

    private BigDecimal humidity2;

    private String measureAppearance;

    private String measureStiffness;

    private String measureWeight;

    private String lotMark;

    private String lotLocation1;

    private String lotLocation2;

    private String testState;

}
