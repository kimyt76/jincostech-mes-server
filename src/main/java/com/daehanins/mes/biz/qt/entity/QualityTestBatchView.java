package com.daehanins.mes.biz.qt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
* <p>
* QualityTestBatchView 엔티티
* </p>
*
* @author jeonsj
* @since 2021-05-25
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("quality_test_batch_view")
public class QualityTestBatchView {

    @TableId
    private String qualityTestId;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate reqDate;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate testDate;

    private String areaCd;

    private String itemCd;

    private String testNo;

    private String testResultJoin;

    private String qualityTestMethodIdJoin;

    private String passState;
    private String passStateName;

    private String testState;
    private String testStateName;

    private Integer colCount;


}
