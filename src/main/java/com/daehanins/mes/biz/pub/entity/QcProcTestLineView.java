package com.daehanins.mes.biz.pub.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
* <p>
* QcProcTestLineView 엔티티
* </p>
*
* @author jeonsj
* @since 2023-05-17
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("qc_proc_test_line_view")
public class QcProcTestLineView {


    @TableId
    private String qcProcTestLineId;

    private String qcProcTestMasterId;

    private String testType;

    private Integer displayOrder;

    private String lineName;

    private String scaleCd;

    private String scaleNickname;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime lastTime;

    private BigDecimal lastValue;


}
