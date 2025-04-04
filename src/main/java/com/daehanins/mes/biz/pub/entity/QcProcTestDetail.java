package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.daehanins.mes.base.BaseEntity;
import com.daehanins.mes.common.utils.SnowFlakeUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * QcProcTestDetail 엔티티
 * </p>
 *
 * @author jeonsj
 * @since 2023-05-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("qc_proc_test_detail")
public class QcProcTestDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId
    private String qcProcTestDetailId = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    private String qcProcTestMasterId;

    private String testType;

    private Integer displayOrder;

    private String testTime;

    private BigDecimal line1;

    private BigDecimal line2;

    private BigDecimal line3;

    private BigDecimal line4;

    private BigDecimal line5;

    private BigDecimal line6;

    private BigDecimal line7;

    private BigDecimal line8;

    private BigDecimal line9;

    private BigDecimal line10;

    private String passYn;

}
