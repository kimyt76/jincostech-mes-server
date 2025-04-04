package com.daehanins.mes.biz.qt.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.daehanins.mes.biz.common.code.ItemGb;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
* <p>
* QualityTestView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-07-07
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("quality_test_view")
public class QualityTestView {

    @TableId
    private String qualityTestId;

    private String testNo;

    private String retestYn;
    private String retestSerNo;

    private String itemGb;
    private String itemCd;
    private String itemName;

    private String lotNo;
    private String prodNo;

    private String areaGb;
    private String areaCd;

    private String storageCd;
    private String storageName;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate reqDate;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate testDate;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate confirmDate;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate createDate;

    private String reqMemberCd;
    private String reqMemberName;

    private String sampleMemberCd;
    private String sampleMemberName;

    private String orderMemberCd;
    private String orderMemberName;

    private String testMemberCd;
    private String testMemberName;

    private String confirmMemberCd;

    private BigDecimal reqQty;
    private BigDecimal sampleQty;

    private BigDecimal testQty;

    private String customerCd;
    private String customerName;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate expiryDate;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate shelfLife;

    private String testState;
    private String testStateName;

    private String passState;
    private String passStateName;

    private String tranYn;
    private String matTranId;

    private String memo;

    private String lotProdNo;

}
