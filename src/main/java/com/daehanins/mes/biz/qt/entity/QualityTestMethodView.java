package com.daehanins.mes.biz.qt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* <p>
* QualityTestMethodView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-07-09
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("quality_test_method_view")
public class QualityTestMethodView {

    @TableId
    private String qualityTestMethodId;

    private String qualityTestId;

    private String passState;

    private String testItem;

    private String testMethod;

    private String testMemberName;

    private String testResult;

    private String testDateString;

    private String testSpec;

    private Integer displayOrder;

    private String testNo;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate testDate;

    private String testState;

    private String itemCd;

    private String itemName;

}
