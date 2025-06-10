package com.daehanins.mes.biz.qt.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* <p>
* ItemTestNoView 엔티티
* </p>
*
* @author jeonsj
* @since 2020-07-08
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("item_test_no_view")
public class ItemTestNoView {

    @TableId
    private String testNo;

    @JsonFormat(timezone = "GMT+9", pattern = "yyyy/MM/dd")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate createDate;

    private String areaGb;
    private String itemGb;
    private String itemCd;
    private String itemName;
    private String itemTypeCd;

    private Integer serNo;

    private String lotNo;
    private String prodNo;
    private BigDecimal qty;

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

    private String endYn;

    private BigDecimal strQty;

}
